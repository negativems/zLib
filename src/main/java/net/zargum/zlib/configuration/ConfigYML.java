package net.zargum.zlib.configuration;

import lombok.Getter;
import net.zargum.zlib.zLib;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public abstract class ConfigYML extends Configuration {

    protected FileConfiguration config;

    public ConfigYML(File dataFolder, String fileName) {
        super(dataFolder, fileName + ".yml");
    }

    @Override
    public void load() {
        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException | NullPointerException e) {
            e.printStackTrace();
        }
        zLib.log(ChatColor.GREEN + "Configuration " + fileName + " loaded.");
        onLoad();
    }

    @Override
    public boolean save() {
        beforeSave();
        try {
            config.save(file);
            zLib.log(ChatColor.GREEN + fileName + " saved.");
            onSave();
            return true;
        } catch (IOException e) {
            zLib.log(ChatColor.RED + "Error saving " + config.getName() + ".");
            e.printStackTrace();
            return false;
        }
    }
}
