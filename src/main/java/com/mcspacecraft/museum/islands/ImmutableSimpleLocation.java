package com.mcspacecraft.museum.islands;

public class ImmutableSimpleLocation {
	public ImmutableSimpleLocation(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public int getBlockX() {
		return (int) x;
	}
	
	public int getBlockY() {
		return (int) y;
	}
	
	public int getBlockZ() {
		return (int) z;
	}
	
	private double x;
	private double y;
	private double z;
}
