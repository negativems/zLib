package net.zargum.zlib.utils;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import net.zargum.zlib.textures.Texture;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class BukkitUtils {

    public static final String STRAIGHT_LINE_TEMPLATE = ChatColor.STRIKETHROUGH + Strings.repeat("-", 256);
    public static final String STRAIGHT_CHAT_LINE = STRAIGHT_LINE_TEMPLATE.substring(0, 55);
    public static final String ITEM_LORE_STRAIGHT_LINE = STRAIGHT_LINE_TEMPLATE.substring(0, 33);

    public static final ChatColor[] SORTED_COLORS = {ChatColor.BLACK, ChatColor.DARK_GRAY, ChatColor.GRAY, ChatColor.DARK_GREEN, ChatColor.GREEN, ChatColor.DARK_AQUA, ChatColor.DARK_BLUE, ChatColor.BLUE, ChatColor.AQUA, ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE, ChatColor.DARK_RED, ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.WHITE};
    private static final ImmutableMap<ChatColor, ItemStack> SKULL_COLORS_MAP = ImmutableMap.<ChatColor, ItemStack>builder().put(ChatColor.AQUA, Texture.AQUA_COLOR.asSkull()).put(ChatColor.BLACK, Texture.BLACK_COLOR.asSkull()).put(ChatColor.BLUE, Texture.BLUE_COLOR.asSkull()).put(ChatColor.DARK_AQUA, Texture.DARK_AQUA_COLOR.asSkull()).put(ChatColor.DARK_BLUE, Texture.DARK_BLUE_COLOR.asSkull()).put(ChatColor.DARK_GRAY, Texture.DARK_GRAY_COLOR.asSkull()).put(ChatColor.DARK_GREEN, Texture.DARK_GREEN_COLOR.asSkull()).put(ChatColor.DARK_PURPLE, Texture.DARK_PURPLE_COLOR.asSkull()).put(ChatColor.DARK_RED, Texture.DARK_RED_COLOR.asSkull()).put(ChatColor.GOLD, Texture.GOLD_COLOR.asSkull()).put(ChatColor.GRAY, Texture.GRAY_COLOR.asSkull()).put(ChatColor.GREEN, Texture.GREEN_COLOR.asSkull()).put(ChatColor.LIGHT_PURPLE, Texture.LIGHT_PURPLE_COLOR.asSkull()).put(ChatColor.RED, Texture.RED_COLOR.asSkull()).put(ChatColor.WHITE, Texture.WHITE_COLOR.asSkull()).put(ChatColor.YELLOW, Texture.YELLOW_COLOR.asSkull()).build();

    public static ItemStack toHeadSkull(ChatColor color) {
        return SKULL_COLORS_MAP.get(color);
    }

}
