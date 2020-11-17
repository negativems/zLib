package net.zargum.zlib.permissions;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.query.QueryOptions;
import net.zargum.zlib.zLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class PermissionsManager {

    private static LuckPerms API = zLib.getInstance().getLuckPermsApi();

    private static User loadUser(Player player) {
        if (!player.isOnline()) throw new IllegalStateException("Player is offline");
        return API.getUserManager().getUser(player.getUniqueId());
    }

    private static User loadUser(String username) {
        Player player = Bukkit.getPlayer(username);
        if (player == null) throw new IllegalStateException("Player is offline");
        return API.getUserManager().getUser(player.getUniqueId());
    }

    public static boolean existsGroup(String group) {
        return API.getGroupManager().getGroup(group) != null;
    }

    public static boolean hasMoreOrSamePriority(String g1, String g2) {
        Group group1 = API.getGroupManager().getGroup(g1);
        Group group2 = API.getGroupManager().getGroup(g2);
        if (group1 != null && group2 != null) {
            int weight1 = group1.getWeight().orElse(0);
            int weight2 = group2.getWeight().orElse(0);
            return weight1 >= weight2;
        }
        zLib.log(ChatColor.RED + "Group " + group1 + " or " + group2 + " is not exist.");
        return false;
    }

    public static String getPrimaryGroup(Player player) {
        User user = loadUser(player);
        return user.getPrimaryGroup();
    }

    public static String getPrimaryGroup(String username) {
        User user = loadUser(username);
        return user.getPrimaryGroup();
    }

    public static List<String> getPlayerGroups(Player player) {
        User user = loadUser(player);
        List<String> list = user.resolveInheritedNodes(QueryOptions.nonContextual()).stream()
                .filter(NodeType.INHERITANCE::matches)
                .map(NodeType.INHERITANCE::cast)
                .map(InheritanceNode::getGroupName).collect(Collectors.toList());
        return list;
    }

    private static CachedMetaData getGroupMeta(Group group) {
        QueryOptions contexts = API.getContextManager().getStaticQueryOptions();
        return group.getCachedData().getMetaData(contexts);
    }

    private static CachedMetaData getUserMeta(User user, Player player) {
        QueryOptions contexts = API.getContextManager().getQueryOptions(player);
        return user.getCachedData().getMetaData(contexts);
    }

    public static String getUserPrefix(Player player) {
        User user = loadUser(player);
        CachedMetaData metaData = getUserMeta(user, player);
        return metaData.getPrefix() == null ? "" : metaData.getPrefix();
    }

    public static String getUserSuffix(Player player) {
        User user = loadUser(player);
        CachedMetaData metaData = getUserMeta(user, player);
        return metaData.getSuffix() == null ? "" : metaData.getSuffix();
    }

    public static void setUserPrefix(Player player, String prefix) {
        User user = loadUser(player);
        PrefixNode node;
        if (prefix == null) {
            String pre = getUserPrefix(player);
            node = PrefixNode.builder(pre, 99).build();
            user.data().remove(node);
        } else {
            node = PrefixNode.builder(prefix, 99).build();
            user.data().add(node);
        }
        API.getUserManager().saveUser(user);
    }

    public static String getGroupDisplayName(String groupName) throws NullPointerException {
        Group group = PermissionsManager.getGroup(groupName);
        if (group == null) return null;
        return group.getDisplayName();
    }

    public static String getDisplayname(String username) {
        User user = loadUser(username);
        Player player = Bukkit.getPlayer(username);
        return getUserPrefix(player) + user.getUsername() + getUserSuffix(player);
    }

    public static String getGroupPrefix(String grp) {
        Group group = API.getGroupManager().getGroup(grp);
        if (group == null) return "";
        CachedMetaData metaData = getGroupMeta(group);
        return metaData.getPrefix() == null ? "" : metaData.getPrefix();
    }

    public static String getGroupSuffix(String grp) {
        Group group = API.getGroupManager().getGroup(grp);
        if (group == null) return "";
        CachedMetaData metaData = getGroupMeta(group);
        return metaData.getSuffix() == null ? "" : metaData.getSuffix();
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

    public static boolean isPlayerInGroup(Player player, String group) {
        return player.hasPermission("group." + group);
    }
}
