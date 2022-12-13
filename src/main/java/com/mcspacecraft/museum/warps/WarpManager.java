package com.mcspacecraft.museum.warps;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.configuration.file.YamlConfiguration;

import com.mcspacecraft.museum.Museum;

public class WarpManager {
    private final Map<String, Warp> warps = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    enum RequiredFields {
        X("x"),
        Y("y"),
        Z("z"),
        YAW("yaw"),
        PITCH("pitch"),
        NAME("name");

        private String name;

        RequiredFields(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public void load() {
        final File dataFolder = Museum.getInstance().getDataFolder();
        final File warpFolder = new File(dataFolder, "warps");

        if (!warpFolder.isDirectory()) {
            Museum.getInstance().logErrorMessage("WarpManager#load: couldn't find warps folder!");
            return;
        }

        File[] warpFiles = warpFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".yml");
            }
        });

        for (File warpFile : warpFiles) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(warpFile);
            
            boolean isValid = true;

            // Validate it contains all required fields
            for (RequiredFields field : RequiredFields.values()) {
                if (!yaml.contains(field.getName())) {
                    Museum.getInstance().logErrorMessage("WarpManager#load: invalid warp file %s, missing %s.", warpFile.getName(), field.getName());
                    isValid = false;
                    break;
                }
            }

            if (!isValid) {
                continue;
            }

            Warp warp = new Warp(yaml.getString("name"), yaml.getDouble("x"), yaml.getDouble("y"), yaml.getDouble("z"), (float) yaml.getDouble("yaw"), (float) yaml.getDouble("pitch"));
            warps.put(yaml.getString("name"), warp);
        }

        Museum.getInstance().logInfoMessage("Loaded %d warps from disk.", warps.size());
    }

    public Collection<String> getWarpList() {
        return warps.keySet();
    }

    public boolean hasWarp(String name) {
        return warps.containsKey(name);
    }

    public Warp getWarp(String name) {
        return warps.get(name);
    }
}
