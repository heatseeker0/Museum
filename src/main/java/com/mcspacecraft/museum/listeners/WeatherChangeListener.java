package com.mcspacecraft.museum.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * Prevents weather from turning to rain.
 * 
 * @author Catalin Ionescu
 *
 */
public class WeatherChangeListener implements Listener {
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState() == true) {
            event.setCancelled(true);
        }
    }
}
