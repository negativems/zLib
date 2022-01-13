package net.zargum.zlib.animations.impl;

import com.google.common.base.Strings;
import net.zargum.zlib.animations.TextAnimation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ScrollAnimation extends TextAnimation {

    private final String text;
    private final int width, delay;

    public ScrollAnimation(String text, int width, int delay) {
        StringBuilder builder = new StringBuilder(text);

        if (text.length() < width) {
            while (builder.length() < width) {
                builder.append(" ");
            }
        }

        this.text = builder.toString();
        this.width = width;
        this.delay = delay;
    }

    @Override
    public String[] getCalculatedFrames() {
        List<String> frames = new ArrayList<>();

        for (int i = 0; i < text.length() - width; i++) {
            frames.addAll(Collections.nCopies(delay, text.substring(i, i + width)));
        }

        StringBuilder space = new StringBuilder();

        int spaceBetween = 1;
        for (int i = 0; i < spaceBetween; ++i) {
            frames.addAll(Collections.nCopies(delay, text.substring(text.length() - width) + space));

            if (space.length() < width) {
                space.append(" ");
            }
        }

        for (int i = 0; i < width - spaceBetween; ++i) {
            frames.addAll(Collections.nCopies(delay, text.substring(text.length() - width + spaceBetween + i) + space + Strings.repeat(" ", text.substring(0, i).length())));
        }

        return frames.toArray(new String[frames.size()]);
    }
}