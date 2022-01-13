package net.zargum.zlib.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.zargum.zlib.zLib;
import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.TreeMap;

@Getter
public abstract class ConfigJSON extends Configuration {

    protected JSONObject json;
    private final JSONParser parser = new JSONParser();
    protected final HashMap<String, Object> data = new HashMap<>();

    public ConfigJSON(File dataFolder, String fileName) {
        super(dataFolder, fileName + ".json");
    }

    @Override
    public void load() {
        try {
            json = (JSONObject) new JSONParser().parse(new FileReader(file));
            onLoad();
            zLib.log(ChatColor.GREEN + "Configuration " + fileName + " loaded.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean save() {
        beforeSave();
        try {
            JSONObject toSave = new JSONObject();

            for (String s : data.keySet()) {
                Object o = data.get(s);
                if (o instanceof String) {
                    toSave.put(s, getString(s));
                } else if (o instanceof Double) {
                    toSave.put(s, getDouble(s));
                } else if (o instanceof Integer) {
                    toSave.put(s, getInteger(s));
                } else if (o instanceof JSONObject) {
                    toSave.put(s, getObject(s));
                } else if (o instanceof JSONArray) {
                    toSave.put(s, getArray(s));
                }
            }

            TreeMap<String, Object> treeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            treeMap.putAll(toSave);

            Gson g = new GsonBuilder().setPrettyPrinting().create();
            String prettyJsonString = g.toJson(treeMap);

            FileWriter fw = new FileWriter(file);
            fw.write(prettyJsonString);
            fw.flush();
            fw.close();
            onSave();
            zLib.log(ChatColor.GREEN + "Configuration " + fileName + " saved.");

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public String getRawData(String key) {
        return json.containsKey(key) ? json.get(key).toString()
                : (data.containsKey(key) ? data.get(key).toString() : key);
    }

    public String getString(String key) {
        return ChatColor.translateAlternateColorCodes('&', getRawData(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(getRawData(key));
    }

    public double getDouble(String key) {
        try {
            return Double.parseDouble(getRawData(key));
        } catch (Exception ignored) {
        }
        return -1;
    }

    public double getInteger(String key) {
        try {
            return Integer.parseInt(getRawData(key));
        } catch (Exception ignored) {
        }
        return -1;
    }

    public JSONObject getObject(String key) {
        return json.containsKey(key) ? (JSONObject) json.get(key)
                : (data.containsKey(key) ? (JSONObject) data.get(key) : new JSONObject());
    }

    public JSONArray getArray(String key) {
        return json.containsKey(key) ? (JSONArray) json.get(key)
                : (data.containsKey(key) ? (JSONArray) data.get(key) : new JSONArray());
    }
}
