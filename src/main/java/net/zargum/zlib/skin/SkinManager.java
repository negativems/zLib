package net.zargum.zlib.skin;

import lombok.Getter;
import net.zargum.zlib.zLib;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SkinManager {

    private final zLib plugin;
    @Getter private Map<String, String[]> skins = new HashMap<>();
    @Getter private File file;
    @Getter private FileConfiguration configuration;

    public SkinManager(zLib plugin) {
        this.plugin = plugin;
        this.loadFile();
        this.load();
    }

    public void loadFile() {
        file = new File(plugin.getDataFolder(), "skins.yml");
        configuration = new YamlConfiguration();
        try {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                zLib.log(ChatColor.RED + "Can not create " + file.getName() + " folder.");
            }
            if (!file.exists() && !file.createNewFile()) {
                zLib.log(ChatColor.RED + "Can not create " + file.getName() + " file.");
            }
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Set<String> skinNames = configuration.getConfigurationSection("").getValues(false).keySet();

        for (String username : skinNames) {
            String[] properties = configuration.getStringList(username).toArray(new String[0]);
            skins.put(username, properties);
        }
    }

    public void save() {
        for (String username : skins.keySet()) configuration.set(username, skins.get(username));
        try {
            this.configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
