package net.zargum.zlib.tab;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TabTask extends BukkitRunnable {

    private final TabManager manager;

    public TabTask(TabManager manager) {
        this.manager = manager;
        runTaskTimerAsynchronously(manager.getPlugin(), 2L, 2L);
    }

    @Override
    public void run() {
        TabAdapter adapter = manager.getAdapter();
        if (adapter != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Tab tab = manager.getTabByPlayer(player);
                if (tab != null) {
                    TabTemplate template = adapter.getTemplate(player);

                    if (template == null || (template.getLeft().isEmpty() && template.getMiddle().isEmpty() && template.getRight().isEmpty())) {
                        for (Tab.TabEntryPosition position : tab.getPositions()) {
                            Team team = player.getScoreboard().getTeam(position.getKey());
                            if (team != null) {
                                if (team.getPrefix() != null && !team.getPrefix().isEmpty()) {
                                    team.setPrefix("");
                                }
                                if (team.getSuffix() != null && !team.getSuffix().isEmpty()) {
                                    team.setSuffix("");
                                }
                            }
                        }
                        continue;
                    }

                    for (int i = 0; i < 20 - template.getLeft().size(); i++) {
                        template.left("");
                    }

                    for (int i = 0; i < 20 - template.getMiddle().size(); i++) {
                        template.middle("");
                    }

                    for (int i = 0; i < 20 - template.getRight().size(); i++) {
                        template.right("");
                    }

                    List<List<String>> rows = Arrays.asList(template.getLeft(), template.getMiddle(), template.getRight(), template.getFarRight());
                    for (int l = 0; l < rows.size(); l++) {
                        for (int i = 0; i < rows.get(l).size(); i++) {
                            Team team = tab.getByLocation(l, i);
                            if (team != null) {
                                Map.Entry<String, String> prefixAndSuffix = getPrefixAndSuffix(rows.get(l).get(i));
                                String prefix = prefixAndSuffix.getKey();
                                String suffix = prefixAndSuffix.getValue();

                                if (team.getPrefix().equals(prefix) && team.getSuffix().equals(suffix)) {
                                    continue;
                                }

                                team.setPrefix(prefix);
                                team.setSuffix(suffix);
                            }
                        }
                    }
                }
            }
        }
    }

    private Map.Entry<String, String> getPrefixAndSuffix(String text) {
        String prefix, suffix;

        text = ChatColor.translateAlternateColorCodes('&', text);

        if (text.length() > 16){
            int splitAt = text.charAt(15) == ChatColor.COLOR_CHAR ? 15 : 16;
            prefix = text.substring(0, splitAt);
            String suffixTemp = ChatColor.getLastColors(prefix) + text.substring(splitAt);
            suffix = (suffixTemp.substring(0, Math.min(suffixTemp.length(), 16)));
        } else {
            prefix = text;
            suffix = "";
        }

        return new AbstractMap.SimpleEntry<>(prefix, suffix);
    }

}
