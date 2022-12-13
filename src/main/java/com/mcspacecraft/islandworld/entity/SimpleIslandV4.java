package com.mcspacecraft.islandworld.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.bukkit.Location;

import com.mcspacecraft.museum.islands.ImmutableSimpleLocation;
import com.mcspacecraft.museum.islands.IslandWorld;

public class SimpleIslandV4 implements Serializable {
    private transient ImmutableSimpleLocation islandCornerA = null;
    private transient ImmutableSimpleLocation islandCornerB = null;
    private transient int hashCode = 0;

    private static final long serialVersionUID = 4L;
    private String ownerName;
    private List<String> members;
    private int isle_x;
    private int isle_z;
    public double spawn_x;
    public double spawn_z;
    public double spawn_y;
    public float spawn_yaw;
    public float spawn_pitch;
    public long createTime;
    public long ownerLoginTime;
    public String schematic;
    /*
     * Protection flags that user can toggle.
     */
    private BitSet flags;

    public enum IslandFlags {
        INTERACT_REDSTONE(0),
        INTERACT_ANVILS(1),
        INTERACT_RIDEABLE(2),
        INTERACT_DOORS(3);

        private IslandFlags(final int id) {
            this.id = id;
        }

        public int id() {
            return this.id;
        }

        final private int id;
    }

    public SimpleIslandV4(int x, int z, String ownerName) {
        this.ownerName = ownerName;
        members = new ArrayList<>();
        isle_x = x;
        isle_z = z;
        initDefaultIslandData();
    }

    /**
     * Initializes and sets common island data to default values.
     */
    private void initDefaultIslandData() {
        flags = new BitSet();
        setFlag(IslandFlags.INTERACT_REDSTONE, false);
        setFlag(IslandFlags.INTERACT_ANVILS, false);
        setFlag(IslandFlags.INTERACT_RIDEABLE, false);
        setFlag(IslandFlags.INTERACT_DOORS, false);
    }

    public void setSpawn(Location loc) {
        spawn_x = loc.getX();
        spawn_y = loc.getY();
        spawn_z = loc.getZ();
        spawn_yaw = loc.getYaw();
        spawn_pitch = loc.getPitch();
    }

    public void setCreateTime(long time) {
        createTime = time;
        setOwnerLoginTime(time);
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setOwnerLoginTime(long time) {
        ownerLoginTime = time;
    }

    public long getOwnerLoginTime() {
        return ownerLoginTime;
    }

    public void setSchematic(String schema) {
        schematic = schema;
    }

    public String getSchematic() {
        return schematic;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(final String ownerName) {
        this.ownerName = ownerName;
    }

    public List<String> getMembers() {
        return members;
    }

    public void removeMember(final String memberName) {
        members.remove(memberName);
    }

    public void addMember(final String memberName) {
        members.add(memberName);
    }

    public void addMembers(List<String> memberNames) {
        members.addAll(memberNames);
    }

    public boolean hasMember(final String memberName) {
        return members.contains(memberName);
    }

    /**
     * Returns true if the location is within the bounds of the island.
     * 
     * @param loc
     * @return
     */
    public boolean isInsideIsland(Location loc) {
        // Initialize the transient fields when they're first needed.
        if (islandCornerA == null || islandCornerB == null) {
            final int x1 = isle_x * IslandWorld.ISLAND_SIZE + 1;
            final int z1 = isle_z * IslandWorld.ISLAND_SIZE + 1;
            islandCornerA = new ImmutableSimpleLocation(x1, 0, z1);

            final int x2 = x1 + IslandWorld.ISLAND_SIZE - 2;
            final int z2 = z1 + IslandWorld.ISLAND_SIZE - 2;

            islandCornerB = new ImmutableSimpleLocation(x2, IslandWorld.MAX_HEIGHT - 1, z2);
        }

        return (loc.getX() >= islandCornerA.getX() && loc.getX() <= islandCornerB.getX() &&
                // loc.getY() >= islandCornerA.getY() && loc.getY() <= islandCornerB.getY() &&
                loc.getZ() >= islandCornerA.getZ() && loc.getZ() <= islandCornerB.getZ());
    }

    public int getIslandGridX() {
        return isle_x;
    }

    public int getIslandGridZ() {
        return isle_z;
    }

    public boolean getFlag(final IslandFlags flag) {
        return flags.get(flag.id());
    }

    public void setFlag(final IslandFlags flag, final boolean allowVisitorsInteract) {
        flags.set(flag.id(), allowVisitorsInteract);
    }

    public boolean toggleFlag(final IslandFlags flag) {
        flags.flip(flag.id());
        return getFlag(flag);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof SimpleIslandV4))
            return false;
        return ((SimpleIslandV4) o).isle_x == isle_x &&
                ((SimpleIslandV4) o).isle_z == isle_z;
    }

    @Override
    public int hashCode() {
        if (hashCode == 0)
            hashCode = isle_x * isle_z;
        return hashCode;
    }
}
