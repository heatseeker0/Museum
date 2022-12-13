package com.mcspacecraft.museum.islands;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.mcspacecraft.islandworld.entity.SimpleIslandV4;
import com.mcspacecraft.museum.Museum;
import com.mcspacecraft.museum.util.SLAPI;

public class IslandWorld {
    public static int ISLAND_SIZE = 100;
    public static int ISLAND_HEIGHT = 20;

    public static int MAX_HEIGHT = 255;

    public static String FREEISLAND_FILE = "freelistV2.dat";
    public static String TAKENISLAND_FILE = "islelistV2.dat";
    public static String RESERVEDISLAND_FILE = "reservedlistV2.dat";

    private Map<String, SimpleIslandV4> islandList = new HashMap<>();
    private boolean islandsLoaded = false;

    private Map<IslandLookupKey, SimpleIslandV4> islandLookupList = new HashMap<>();

    @SuppressWarnings("unchecked")
    public void loadIslandList() {
        final File dataFolder = Museum.getInstance().getDataFolder();

        if (islandList.isEmpty() && (new File(dataFolder, TAKENISLAND_FILE)).exists()) {
            try {
                islandList = (Map<String, SimpleIslandV4>) SLAPI.load(dataFolder + "/" + TAKENISLAND_FILE);
                islandsLoaded = true;
                Museum.getInstance().logInfoMessage("Loaded %d islands from disk.", islandList.size());
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

    public Collection<String> getOwnerList() {
        return islandList.keySet();
    }

    public boolean ownsIsland(String playerName) {
        return islandList.containsKey(playerName);
    }

    public SimpleIslandV4 getOwnedIsland(String playerName) {
        return islandList.get(playerName);
    }

    /**
     * Builds the island from coordinates quick lookup map.
     */
    public void buildIslandLookupMap() {
        if (!islandsLoaded || islandList.isEmpty()) {
            return;
        }

        for (SimpleIslandV4 island : islandList.values()) {
            final IslandLookupKey key = new IslandLookupKey(island.getIslandGridX(), island.getIslandGridZ());
            islandLookupList.put(key, island);
        }
    }
}
