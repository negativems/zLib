package net.zargum.zlib.servers;

import lombok.Getter;

public enum ServerStatus {

    LOADING(-1),
    OFFLINE(0),
    WHITELISTED(1),
    ONLINE(2);

    @Getter int statusId;
    
    ServerStatus(int i) {
        this.statusId = i;
    }

    public static ServerStatus getById(int statusId) {
        for (ServerStatus status : values()) {
            if (status.getStatusId() == statusId) {
                return status;
            }
        }
        return null;
    }
}
