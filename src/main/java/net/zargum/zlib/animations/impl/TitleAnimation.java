package net.zargum.zlib.animations.impl;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import net.zargum.zlib.animations.TextAnimation;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public final class TitleAnimation extends TextAnimation {

    private final String title;
    private final ChatColor primaryColor, secondaryColor;

    @Override
    public String[] getCalculatedFrames() {
        List<String> frames = new ArrayList<>();
        frames.addAll(Collections.nCopies(30, primaryColor.toString() + ChatColor.BOLD + title));

        StringBuilder base = new StringBuilder(primaryColor.toString() + ChatColor.BOLD + "");

        int length = title.length();
        for (int index = 0; index < length; index++) {
            char character = title.charAt(index);

            int diff = (length - index - 1);
            for (int left = diff; left >= 0; left--) {
                frames.add(base + Strings.repeat(" ", left) + secondaryColor.toString() + ChatColor.BOLD + character + Strings.repeat(" ", diff - left));

                if (left <= 0) {
                    base.append(character);
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            int delay = 4;
            frames.addAll(Collections.nCopies(delay, secondaryColor.toString() + ChatColor.BOLD + title));
            frames.addAll(Collections.nCopies(delay, ChatColor.WHITE.toString() + ChatColor.BOLD + title));
        }

        frames.addAll(Collections.nCopies(30, primaryColor.toString() + ChatColor.BOLD + title));
        return frames.toArray(new String[frames.size()]);
    }
}