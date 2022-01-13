package net.zargum.zlib.configuration;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationRegister {

    public static final List<Configuration> configurationList = new ArrayList<>();

    public static void register(Configuration config) {
        configurationList.add(config);
    }

    public static void unregister(Configuration config) {
        configurationList.remove(config);
    }
}
