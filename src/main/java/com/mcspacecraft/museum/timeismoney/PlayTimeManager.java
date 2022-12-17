package com.mcspacecraft.museum.timeismoney;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.mcspacecraft.museum.Museum;
import com.mcspacecraft.museum.util.SLAPI;

public class PlayTimeManager {
    private Map<String, Long> playedTime = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    @SuppressWarnings("unchecked")
    public void load() {
        if (playedTime.isEmpty() && (new File(Museum.getInstance().getDataFolder().toString() + "/PlayedTime.dat")).exists()) {
            try {
                playedTime = (Map<String, Long>) SLAPI.load(Museum.getInstance().getDataFolder().toString() + "/PlayedTime.dat");

                Museum.getInstance().logInfoMessage("Loaded %d players play time from disk.", playedTime.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Collection<String> getPlayers() {
        return playedTime.keySet();
    }

    public boolean hasPlayTime(String name) {
        return playedTime.containsKey(name);
    }

    public long getPlayTime(String name) {
        return playedTime.get(name);
    }

    /**
     * Converts a timestamp to a human readable string translation.
     * 
     * @param timestamp The timestamp to convert
     * @return Human readable String (hours min seconds) of the given timestamp
     */
    public static String timestampToString(Long timestamp) {
        Long tempStamp = timestamp;
        Long hours = tempStamp / (1000 * 60 * 60);
        tempStamp -= hours * 1000 * 60 * 60;
        Long mins = tempStamp / (1000 * 60);
        tempStamp -= mins * 1000 * 60;
        Long secs = tempStamp / 1000;

        return String.format("%d hours %d min %d seconds ", hours, mins, secs);
    }
}
