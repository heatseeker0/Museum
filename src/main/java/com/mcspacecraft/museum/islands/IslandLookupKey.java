package com.mcspacecraft.museum.islands;

/**
 * Implements a fast tuple based on two integers that can be used as a
 * lookup key for a HashMap.
 * 
 * The hash code is simply the product of the two integers.
 * 
 * @author Catalin Ionescu
 * 
 */
public class IslandLookupKey {
    public IslandLookupKey(int islandX, int islandZ) {
        this.islandX = islandX;
        this.islandZ = islandZ;
        hashCode = islandX * islandZ;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof IslandLookupKey))
            return false;
        return ((IslandLookupKey) o).islandX == islandX &&
                ((IslandLookupKey) o).islandZ == islandZ;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    private int islandX;
    private int islandZ;
    private int hashCode;
}
