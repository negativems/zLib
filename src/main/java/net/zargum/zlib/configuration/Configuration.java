package net.zargum.zlib.configuration;

import lombok.Getter;
import net.zargum.zlib.zLib;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.IOException;

@Getter
public abstract class Configuration {

    protected final File dataFolder;
    protected final File file;
    protected final String fileName;

    public Configuration(File datafolder, String fileName) {
        this.dataFolder = datafolder;
        if (!datafolder.exists() && !datafolder.mkdirs()) {
            zLib.log(ChatColor.RED + "Could not create " + dataFolder.getName() + "/" + datafolder.getName() + " folder.");
        }
        this.file = new File(dataFolder, fileName);
        try {
            if (!file.exists() && !file.createNewFile()) {
                zLib.log(ChatColor.RED + "Can not create " + file.getName() + " file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.fileName = fileName;
        ConfigurationRegister.register(this);
    }

    public void load() {
        onLoad();
    }

    public boolean save() {
        beforeSave();
        onSave();
        return true;
    }

    public abstract void onLoad();
    public abstract void beforeSave();
    public abstract void onSave();
}
