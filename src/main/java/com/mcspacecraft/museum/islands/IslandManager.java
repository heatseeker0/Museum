package com.mcspacecraft.museum.islands;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;

import com.mcspacecraft.islandworld.entity.SimpleIslandV4;
import com.mcspacecraft.museum.Museum;
import com.mcspacecraft.museum.util.SLAPI;

public class IslandManager {
    public static int ISLAND_SIZE = 100;
    public static int ISLAND_HEIGHT = 20;

    public static int MAX_HEIGHT = 255;

    public static String FREEISLAND_FILE = "freelistV2.dat";
    public static String TAKENISLAND_FILE = "islelistV2.dat";
    public static String RESERVEDISLAND_FILE = "reservedlistV2.dat";

    private Map<String, SimpleIslandV4> islandList = new HashMap<>();
    private HashMap<String, SimpleIslandV4> helperList = new HashMap<>();

    private boolean islandsLoaded = false;

    private Map<IslandLookupKey, SimpleIslandV4> islandLookupList = new HashMap<>();

    @SuppressWarnings("unchecked")
    public void load() {
        final File dataFolder = Museum.getInstance().getDataFolder();

        if (islandList.isEmpty() && (new File(dataFolder, TAKENISLAND_FILE)).exists()) {
            try {
                islandList = (Map<String, SimpleIslandV4>) SLAPI.load(dataFolder + "/" + TAKENISLAND_FILE);
                islandsLoaded = true;
                Museum.getInstance().logInfoMessage("Loaded %d islands from disk.", islandList.size());

                buildIslandLookupMap();

                buildHelperLookupMap();
                Museum.getInstance().logInfoMessage("Loaded %d helpers from disk.", helperList.size());
            } catch (Exception e) {
                Museum.getInstance().logErrorMessage("Error loading %s island list.", TAKENISLAND_FILE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the islandList map.
     * 
     * @return
     */
    public Map<String, SimpleIslandV4> getIslandList() {
        return islandList;
    }

    public static <T> Set<T> mergeSet(Set<T> a, Set<T> b) {
        return Stream.concat(a.stream(), b.stream()).collect(Collectors.toSet());
    }

    public Collection<String> getOwnerList() {
        return mergeSet(islandList.keySet(), helperList.keySet());
    }

    public boolean ownsIsland(String playerName) {
        return islandList.containsKey(playerName) || helperList.containsKey(playerName);
    }

    public SimpleIslandV4 getOwnedIsland(String playerName) {
        return islandList.getOrDefault(playerName, helperList.get(playerName));
    }

    /**
     * Builds the island from coordinates quick lookup map.
     */
    private void buildIslandLookupMap() {
        if (!islandsLoaded || islandList.isEmpty()) {
            return;
        }

        for (SimpleIslandV4 island : islandList.values()) {
            final IslandLookupKey key = new IslandLookupKey(island.getIslandGridX(), island.getIslandGridZ());
            islandLookupList.put(key, island);
        }
    }

    /**
     * Build a cache map of all helpers and the islands they belong to.
     */
    private void buildHelperLookupMap() {
        helperList.clear();

        if (islandList == null) {
            return;
        }

        for (SimpleIslandV4 island : islandList.values()) {
            final List<String> members = island.getMembers();
            if (members == null || members.isEmpty()) {
                continue;
            }

            for (String member : members) {
                helperList.put(member.toLowerCase(), island);
            }
        }
    }

    /**
     * Converts block locations to grid map coordinates, then returns the island at that grid coordinates.
     * 
     * @param loc Location to query
     * @return Island at the specified location, or null if none found
     */
    public SimpleIslandV4 getIslandFromLocation(Location loc) {
        if (loc == null) {
            return null;
        }

        final int locBlockX = loc.getBlockX();
        final int locBlockZ = loc.getBlockZ();

        // Factor in the 1 block gap between islands. Island coords are 1 .. 99, gap is 100.
        if (locBlockX <= 0 || locBlockZ <= 0 || locBlockX % ISLAND_SIZE == 0 || locBlockZ % ISLAND_SIZE == 0) {
            return null;
        }

        // TODO: Refactor this to use location factory with caching for old query locations.
        IslandLookupKey key = new IslandLookupKey(locBlockX / ISLAND_SIZE, locBlockZ / ISLAND_SIZE);

        return islandLookupList.get(key);
    }
}
