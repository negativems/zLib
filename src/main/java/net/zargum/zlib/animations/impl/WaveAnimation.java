package net.zargum.zlib.animations.impl;

import net.zargum.zlib.animations.TextAnimation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class WaveAnimation extends TextAnimation {

    private final int delay;
    private final String text, primary, secondary;

    public WaveAnimation(int delay, String text, String primary, String secondary) {
        this.delay = delay;
        this.text = text;
        this.primary = primary;
        this.secondary = secondary;
    }

    public String[] getCalculatedFrames() {
        List<String> frames = new ArrayList<>();

        for (int i = 0; i < text.length() + 1; i++) {
            frames.addAll(Collections.nCopies(delay, "§f" + primary + new StringBuilder(text).insert(i, secondary).toString()));
        }

        for (int i = 0; i < text.length() + 1; i++) {
            frames.addAll(Collections.nCopies(delay, "§f" + secondary + new StringBuilder(text).insert(i, primary).toString()));
        }

        return frames.toArray(new String[frames.size()]);
    }
}