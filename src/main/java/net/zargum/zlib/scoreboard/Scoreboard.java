package net.zargum.zlib.scoreboard;

import net.zargum.zlib.utils.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Simple Bukkit ScoreBoard API with 1.7 to 1.15 support !
 * Everything is at packet level so you don't need to use it in the main server thread.
 * <p>
 * You can find the project on <a href="https://github.com/MrMicky-FR/FastBoard">GitHub</a>
 *
 * @author MrMicky
 */
public class Scoreboard {

    // Packets sending
    private static final Field PLAYER_CONNECTION;
    private static final Method SEND_PACKET;
    private static final Method PLAYER_GET_HANDLE;

    // Chat components
    private static final Class<?> CHAT_COMPONENT_CLASS;
    private static final Method MESSAGE_FROM_STRING;

    // Scoreboard packets
    private static final Constructor<?> PACKET_SB_OBJ;
    private static final Constructor<?> PACKET_SB_DISPLAY_OBJ;
    private static final Constructor<?> PACKET_SB_SCORE;
    private static final Constructor<?> PACKET_SB_TEAM;

    // Scoreboard enums
    private static final Class<?> ENUM_SB_HEALTH_DISPLAY;
    private static final Class<?> ENUM_SB_ACTION;
    private static final Object ENUM_SB_HEALTH_DISPLAY_INTEGER;
    private static final Object ENUM_SB_ACTION_CHANGE;
    private static final Object ENUM_SB_ACTION_REMOVE;

    static {
        try {
            Class<?> craftChatMessageClass = Reflection.obcClass("util.CraftChatMessage");
            Class<?> entityPlayerClass = Reflection.nmsClass("EntityPlayer");
            Class<?> playerConnectionClass = Reflection.nmsClass("PlayerConnection");
            Class<?> craftPlayerClass = Reflection.obcClass("entity.CraftPlayer");

            MESSAGE_FROM_STRING = craftChatMessageClass.getDeclaredMethod("fromString", String.class);
            CHAT_COMPONENT_CLASS = Reflection.nmsClass("IChatBaseComponent");

            PLAYER_GET_HANDLE = craftPlayerClass.getDeclaredMethod("getHandle");
            PLAYER_CONNECTION = entityPlayerClass.getDeclaredField("playerConnection");
            SEND_PACKET = playerConnectionClass.getDeclaredMethod("sendPacket", Reflection.nmsClass("Packet"));

            PACKET_SB_OBJ = Reflection.nmsClass("PacketPlayOutScoreboardObjective").getConstructor();
            PACKET_SB_DISPLAY_OBJ = Reflection.nmsClass("PacketPlayOutScoreboardDisplayObjective").getConstructor();
            PACKET_SB_SCORE = Reflection.nmsClass("PacketPlayOutScoreboardScore").getConstructor();
            PACKET_SB_TEAM = Reflection.nmsClass("PacketPlayOutScoreboardTeam").getConstructor();

            ENUM_SB_HEALTH_DISPLAY = Reflection.nmsClass("IScoreboardCriteria$EnumScoreboardHealthDisplay");
            ENUM_SB_ACTION = Reflection.nmsClass("PacketPlayOutScoreboardScore$EnumScoreboardAction");
            ENUM_SB_HEALTH_DISPLAY_INTEGER = Reflection.enumValueOf(ENUM_SB_HEALTH_DISPLAY, "INTEGER");
            ENUM_SB_ACTION_CHANGE = Reflection.enumValueOf(ENUM_SB_ACTION, "CHANGE");
            ENUM_SB_ACTION_REMOVE = Reflection.enumValueOf(ENUM_SB_ACTION, "REMOVE");
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final Player player;
    private final String id;

    private String title = ChatColor.RESET.toString();
    private final List<String> lines = new ArrayList<>();

    private boolean deleted = false;

    public Scoreboard(Player player) {
        this.player = Objects.requireNonNull(player, "player");

        id = "fb-" + Double.toString(Math.random()).substring(2, 10);

        try {
            sendObjectivePacket(ObjectiveMode.CREATE);
            sendDisplayObjectivePacket();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTitle() {
        return title;
    }

    public void updateTitle(String title) {
        if (this.title.equals(Objects.requireNonNull(title, "title"))) return;
        if (title.length() > 32) throw new IllegalArgumentException("Title is longer than 32 chars");

        this.title = ChatColor.translateAlternateColorCodes('&', title);

        try {
            sendObjectivePacket(ObjectiveMode.UPDATE);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getLines() {
        return new ArrayList<>(lines);
    }

    public void updateLines(String... lines) {
        updateLines(Arrays.asList(lines));
    }

    public void updateLines(Collection<String> l) {
        Objects.requireNonNull(lines, "lines");

        List<String> lines = new ArrayList<>(l);

        int lineCount = 0;
        for (String line : lines) {
            if (line != null && line.length() > 32) {
//                throw new IllegalArgumentException("Line " + lineCount + " is longer than 32 chars");
                lines.set(lineCount, line.substring(0, 32));
                System.out.println("Line " + (lineCount + 1) + " is longer than 32 chars: " + line);
            }
            lineCount++;
        }

        List<String> oldLines = new ArrayList<>(this.lines);
        this.lines.clear();
        this.lines.addAll(lines);

        int linesSize = this.lines.size();

        try {
            if (oldLines.size() != linesSize) {
                List<String> oldLinesCopy = new ArrayList<>(oldLines);

                if (oldLines.size() > linesSize) {
                    for (int i = oldLinesCopy.size(); i > linesSize; i--) {
                        sendTeamPacket(i - 1, TeamMode.REMOVE);

                        sendScorePacket(i - 1, ScoreboardAction.REMOVE);

                        oldLines.remove(0);
                    }
                } else {
                    for (int i = oldLinesCopy.size(); i < linesSize; i++) {
                        sendScorePacket(i, ScoreboardAction.CHANGE);

                        sendTeamPacket(i, TeamMode.CREATE);

                        oldLines.add(oldLines.size() - i, getLineByScore(i));
                    }
                }
            }

            for (int i = 0; i < linesSize; i++) {
                if (!Objects.equals(getLineByScore(oldLines, i), getLineByScore(i))) {
                    sendTeamPacket(i, TeamMode.UPDATE);
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLine(int line) {
        return lines.get(line);
    }

    public Player getPlayer() {
        return player;
    }

    public String getId() {
        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void delete() {
        try {
            for (int i = 0; i < lines.size(); i++) {
                sendTeamPacket(i, TeamMode.REMOVE);
            }

            sendObjectivePacket(ObjectiveMode.REMOVE);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        deleted = true;
    }

    private String getLineByScore(int score) {
        return getLineByScore(lines, score);
    }

    private String getLineByScore(List<String> lines, int score) {
        return lines.get(lines.size() - score - 1);
    }

    private void sendObjectivePacket(ObjectiveMode mode) throws ReflectiveOperationException {
        Object packet = PACKET_SB_OBJ.newInstance();

        setField(packet, String.class, id);
        setField(packet, int.class, mode.ordinal());

        if (mode != ObjectiveMode.REMOVE) {
            setField(packet, String.class, title, 1);
            setField(packet, ENUM_SB_HEALTH_DISPLAY, ENUM_SB_HEALTH_DISPLAY_INTEGER);
        }

        sendPacket(packet);
    }

    private void sendDisplayObjectivePacket() throws ReflectiveOperationException {
        Object packet = PACKET_SB_DISPLAY_OBJ.newInstance();

        setField(packet, int.class, 1);
        setField(packet, String.class, id);

        sendPacket(packet);
    }

    private void sendScorePacket(int score, ScoreboardAction action) throws ReflectiveOperationException {
        Object packet = PACKET_SB_SCORE.newInstance();
        setField(packet, String.class, getColorCode(score), 0);
        setField(packet, ENUM_SB_ACTION, action == ScoreboardAction.REMOVE ? ENUM_SB_ACTION_REMOVE : ENUM_SB_ACTION_CHANGE);

        if (action == ScoreboardAction.CHANGE) {
            setField(packet, String.class, id, 1);
            setField(packet, int.class, score);
        }

        sendPacket(packet);
    }

    private void sendTeamPacket(int score, TeamMode mode) throws ReflectiveOperationException {
        if (mode == TeamMode.ADD_PLAYERS || mode == TeamMode.REMOVE_PLAYERS) {
            throw new UnsupportedOperationException();
        }

        Object packet = PACKET_SB_TEAM.newInstance();

        setField(packet, String.class, id + ':' + score); // Team name
        setField(packet, int.class, mode.ordinal(), 1); // Update mode

        if (mode == TeamMode.CREATE || mode == TeamMode.UPDATE) {
            String line = getLineByScore(score);
            String prefix;
            String suffix = null;

            if (line == null || line.isEmpty()) {
                prefix = getColorCode(score) + ChatColor.RESET;
            } else if (line.length() <= 16) {
                prefix = line;
            } else {
                // Prevent splitting color codes
                int index = line.charAt(15) == ChatColor.COLOR_CHAR ? 15 : 16;
                prefix = line.substring(0, index);
                String suffixTmp = line.substring(index);
//                ChatColor startSuffixColor = null;

//                if (suffixTmp.length() >= 2 && suffixTmp.charAt(0) == ChatColor.COLOR_CHAR) {
//                    startSuffixColor = ChatColor.getByChar(suffixTmp.charAt(1));
//                }

                String lastColors = ChatColor.getLastColors(prefix);

                StringBuilder colorAndFormatFromPrefix = new StringBuilder();
                if (ColorUtils.getLastColor(lastColors) != null) colorAndFormatFromPrefix.append(ColorUtils.getLastColor(lastColors));
                else colorAndFormatFromPrefix.append(ChatColor.RESET);
                if (ColorUtils.getLastFormat(lastColors) != null) colorAndFormatFromPrefix.append(ColorUtils.getLastFormat(lastColors));

                suffix = colorAndFormatFromPrefix.toString() + suffixTmp;
            }

            // Something went wrong, just cut to prevent client crash/kick
            if (prefix.length() > 16) {
                prefix = prefix.substring(0, 16);
            }

            if (suffix != null && suffix.length() > 16) {
                suffix = suffix.substring(0, 16);
            }

            setComponentField(packet, prefix, 2); // Prefix
            setComponentField(packet, suffix == null ? "" : suffix, 3); // Suffix
            setField(packet, String.class, "always", 4); // Visibility for 1.8+

            if (mode == TeamMode.CREATE) {
                setField(packet, Collection.class, Collections.singletonList(getColorCode(score))); // Players in the team
            }
        }

        sendPacket(packet);
    }


    private String getColorCode(int score) {
        return ChatColor.values()[score].toString();
    }

    private void sendPacket(Object packet) throws ReflectiveOperationException {
        if (deleted) throw new IllegalStateException("This FastBoard is deleted");

        if (player.isOnline()) {
            Object entityPlayer = PLAYER_GET_HANDLE.invoke(player);
            Object playerConnection = PLAYER_CONNECTION.get(entityPlayer);
            SEND_PACKET.invoke(playerConnection, packet);
        }
    }

    private void setField(Object object, Class<?> fieldType, Object value) throws ReflectiveOperationException {
        setField(object, fieldType, value, 0);
    }

    private void setField(Object object, Class<?> fieldType, Object value, int count) throws ReflectiveOperationException {
        int i = 0;

        for (Field f : object.getClass().getDeclaredFields()) {
            if (f.getType() == fieldType && i++ == count) {
                f.setAccessible(true);
                f.set(object, value);
            }
        }
    }

    private void setComponentField(Object object, String value, int count) throws ReflectiveOperationException {
        setField(object, String.class, value, count);
    }

    enum ObjectiveMode {

        CREATE, REMOVE, UPDATE

    }

    enum TeamMode {

        CREATE, REMOVE, UPDATE, ADD_PLAYERS, REMOVE_PLAYERS

    }

    enum ScoreboardAction {

        CHANGE, REMOVE

    }
}