package net.zargum.zlib.configuration;

import net.zargum.zlib.zLib;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigDir {

    protected final File file;
    protected final Map<String, FileConfiguration> configsMap = new HashMap<>();

    public ConfigDir(File dataFolder, String directoryName) {
        file = new File(dataFolder, directoryName);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                zLib.log(ChatColor.RED + "Can not create " + dataFolder.getName() + "/" + file.getName() + " folder.");
            }
            if (!file.exists() && !file.createNewFile()) {
                zLib.log(ChatColor.RED + "Can not create " + file.getName() + " file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        load();
    }

    public String[] getAllFilesName() {
        return file.list();
    }

    private void load() {
        String[] files = file.list();
        if (files == null) {
            zLib.log(ChatColor.RED + "[CONFIG DIR] There was a problem loading " + file.getName());
            return;
        }
        if (files.length == 0) {
            zLib.log("[CONFIG DIR] Folder " + file.getName() + " is empty.");
            return;
        }
        for (String file : files) {
            if (file.endsWith(".yml")) {
                try {
                    YamlConfiguration config = new YamlConfiguration();
                    config.load(file);
                    configsMap.put(file, config);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addFile(String fileName) {
        configsMap.put(fileName, new YamlConfiguration());
    }

    public boolean existsConfig(String configName) {
        return configsMap.containsKey(configName);
    }

    public void save() {
        configsMap.values().forEach(config -> {
            try {
                zLib.log(ChatColor.GREEN + config.getName() + " saved.");
                config.save(file);
            } catch (IOException e) {
                zLib.log(ChatColor.RED + "Error saving " + config.getName() + ".");
                e.printStackTrace();
            }
        });
    }

}
