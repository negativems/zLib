package net.zargum.zlib.proxy;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ServerInfo {

    private final String serverName;
    private final ServerStatusType status;
    private final int onlineCount;

    public ServerInfo(String serverName, ServerStatusType status, int onlinePlayers) {
        this.serverName = serverName;
        this.status = status;
        this.onlineCount = onlinePlayers;
    }

}
