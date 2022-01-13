package net.zargum.zlib.proxy;

import lombok.Getter;

import java.util.HashMap;

public enum ServerStatusType {

    INITIALIZING(-1),
    OFFLINE(0),
    WHITELISTED(1),
    ONLINE(2);

    public static final HashMap<Integer, ServerStatusType> ENUM_VALUES = new HashMap<>();

    @Getter private final int code;
    ServerStatusType(int code) {
        this.code = code;
    }

    static {
        for (ServerStatusType type : values()) {
            ENUM_VALUES.put(type.toInteger(), type);
        }
    }

    public int toInteger() {
        return code;
    }
}
