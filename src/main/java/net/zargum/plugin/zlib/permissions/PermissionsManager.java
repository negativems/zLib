package net.zargum.plugin.zlib.permissions;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.zargum.plugin.zlib.zLib;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class PermissionsManager {

    private static LuckPerms API = zLib.getInstance().getLuckPermsApi();

    private static User getUser(Player player) {
        if (!player.isOnline()) throw new IllegalStateException("Player is offline");
        return API.getUserManager().getUser(player.getUniqueId());
    }

    public static boolean existsGroup(String group) {
        return API.getGroupManager().getGroup(group) != null;
    }

    public static boolean hasMoreOrSamePriority(String group1, String group2) {
        if (existsGroup(group1) && existsGroup(group2)) {
            int weight1 = API.getGroupManager().getGroup(group1).getWeight().orElse(0);
            int weight2 = API.getGroupManager().getGroup(group2).getWeight().orElse(0);
            return weight1 >= weight2;
        }
        zLib.log(ChatColor.RED + "Group " + group1 + " or " + group2 + " is not exist.");
        return false;
    }

    public static String getPrimaryGroup(Player player) {
        if (API.getUserManager().getUser(player.getUniqueId()) == null) return null;
        return API.getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();
    }

    public static String getPrimaryGroup(String username) {
        if (API.getUserManager().getUser(username) == null) return null;
        return API.getUserManager().getUser(username).getPrimaryGroup();
    }

    public static String getUserPrefix(String username) {
        User user = API.getUserManager().getUser(username);
        Group group = getGroup(getPrimaryGroup(username));
        String prefix = "";
        if (user != null && group != null) {
            Optional<ImmutableContextSet> optional = API.getContextManager().getContext(user);
            if (optional.isPresent()) prefix = user.getCachedData().getMetaData(QueryOptions.nonContextual()).getPrefix();
            else prefix = group.getCachedData().getMetaData(QueryOptions.nonContextual()).getPrefix();
        }
        return prefix;
    }

    public static String getUserSuffix(String username) {
        User user = API.getUserManager().getUser(username);
        Group group = getGroup(username);
        String suffix = "";
        if (user != null && group != null) {
            Optional<ImmutableContextSet> optional = API.getContextManager().getContext(user);
            if (optional.isPresent()) suffix = user.getCachedData().getMetaData(QueryOptions.nonContextual()).getSuffix();
            else suffix = group.getCachedData().getMetaData(QueryOptions.nonContextual()).getSuffix();
        }
        return suffix;
    }

    public static String getDisplayname(String username) {
        User user = API.getUserManager().getUser(username);
        if (user == null) return null;
        return getUserPrefix(username) + user.getUsername() + getUserSuffix(username);
    }

    public static String getGroupPrefix(String group) {
        if (!existsGroup(group)) return null;
        return getGroup(group).getCachedData().getMetaData(QueryOptions.nonContextual()).getPrefix();
    }

    public static String getGroupSuffix(String group) {
        if (!existsGroup(group)) return null;
        return getGroup(group).getCachedData().getMetaData(QueryOptions.nonContextual()).getSuffix();
    }

    public static List<String> getGroups() {
        Set<Group> list = API.getGroupManager().getLoadedGroups();
        List<String> groups = new ArrayList<>();
        for (Group group : list) {
            String parent = group.getName();
            groups.add(parent);
        }
        return groups;
    }

    public static Group getGroup(String group) {
        List<String> groups = getGroups();
        if (!groups.contains(group)) return null;
        return API.getGroupManager().getGroup(group);
    }

    public static List<String> getOrderedGroups() {
        Map<String, Integer> groups = new HashMap<>();
        List<String> orderedGroups = new ArrayList<>();
        List<String> disorderedGroups = getGroups(); // List<String> of all groups

        for (String groupName : disorderedGroups) {
            int weight;

            if (getGroup(groupName).getWeight().isPresent()) weight = getGroup(groupName).getWeight().getAsInt();
            else weight = 0;

            groups.put(groupName, weight);
        }

        // Order groups by weight
        groups.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(stringIntegerEntry -> orderedGroups.add(stringIntegerEntry.getKey()));
        return orderedGroups;
    }

    public static ChatColor getPrimaryColor(String group) {
        if (!existsGroup(group)) return null;
        if (!getGroupPrefix(group).contains("[")) return ChatColor.WHITE;

        String prefix = getGroupPrefix(group);
        return ChatColor.getByChar(prefix.substring(prefix.indexOf('[') + 2, prefix.indexOf('[') + 3));
    }

}
