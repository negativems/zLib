package net.zargum.zlib.animations.impl;

import net.zargum.zlib.animations.TextAnimation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class FlashAnimation extends TextAnimation {

    private final int primaryDelay, secondaryDelay;
    private final String text, primary, secondary;

    public FlashAnimation(String text, int primaryDelay, String primary, int secondaryDelay, String secondary) {
        this.text = text;
        this.primaryDelay = primaryDelay;
        this.primary = primary;
        this.secondaryDelay = secondaryDelay;
        this.secondary = secondary;
    }

    @Override
    public String[] getCalculatedFrames() {
        List<String> frames = new ArrayList<>();
        frames.addAll(Collections.nCopies(primaryDelay, primary + text));
        frames.addAll(Collections.nCopies(secondaryDelay, secondary + text));
        return frames.toArray(new String[primaryDelay + secondaryDelay]);
    }
}