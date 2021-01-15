package net.zargum.zlib.database;

import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.zargum.zlib.zLib;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class MongoConnection {

    private final JavaPlugin plugin;

    private final MongoClient client;
    private final MongoDatabase database;
    private boolean verified;

    public MongoConnection(JavaPlugin plugin, String host, String port, String database, boolean auth, String username, String password) {
        this.plugin = plugin;

        StringBuilder uriBuilder = new StringBuilder("mongodb://").append(host).append(":").append(port).append("/").append(database);
        if (auth) {
            if (username == null || password == null) throw new NullPointerException("Tried to authenticate with null username or password");
            uriBuilder.insert(1, username + ":").insert(2,password + "@");
            System.out.println("[DEBUG] Authenticated: " + uriBuilder.toString());
        }

        client = MongoClients.create(uriBuilder.toString());
        this.database = client.getDatabase(database);
    }

    public MongoConnection(JavaPlugin plugin, ConfigurationSection config) {
        this(
                plugin,
                config.getString("host", "localhost"),
                config.getString("port", "27017"),
                config.getString("database", "admin"),
                config.getBoolean("authentication.enabled", false),
                config.getString("authentication.username", null),
                config.getString("authentication.password", null)
        );
    }

    // Specified host with no auth
    public MongoConnection(JavaPlugin plugin, String host, String database) {
        this(plugin, host, "27017", database, false, null, null);
    }

    // Localhost with no auth
    public MongoConnection(JavaPlugin plugin, String database) {
        this(plugin, "localhost", "27017", database, false, null, null);
    }

    // Localhost with auth
    public MongoConnection(JavaPlugin plugin, String database, String username, String password) {
        this(plugin, "localhost", "27017", database, false, null, null);
    }

    public void close() {
        client.close();
        zLib.log(ChatColor.RED + "Mongo connection closed.");
    }

    private void verify() {
        try {
            String firstCollection = database.listCollectionNames().first();
            if (firstCollection == null) throw new NullPointerException("Tried to verify with no collections in the database");
            database.getCollection(firstCollection).countDocuments();
            verified = true;
            zLib.log(ChatColor.GREEN + "Mongo connection successfully.");
        } catch (MongoCommandException e) {
            if (e.getErrorCode() == 13) {
                zLib.log(ChatColor.DARK_RED + "Authentication is required for the database setted in the config file.");
                zLib.log(ChatColor.DARK_RED + "Disabling '" + plugin.getName() + "'.");
                zLib.getInstance().getServer().getPluginManager().disablePlugin(plugin);
            }
            //More checks soon...
            e.printStackTrace();
        }
    }
}