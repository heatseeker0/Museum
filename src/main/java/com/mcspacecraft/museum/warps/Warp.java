package com.mcspacecraft.museum.warps;

import org.bukkit.Location;
import org.bukkit.World;

public class Warp {
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    private final String name;

    private transient Location loc;

    public Warp(String name, double x, double y, double z, float yaw, float pitch) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public String getName() {
        return name;
    }

    public Location getLocation(World world) {
        if (loc != null && loc.getWorld().equals(world)) {
            return loc;
        }

        loc = new Location(world, x, y, z, yaw, pitch);
        return loc;
    }
}
