package net.zargum.zlib.animations.impl;

import lombok.AllArgsConstructor;
import net.zargum.zlib.animations.TextAnimation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public final class DotsAnimation extends TextAnimation {

    public static final DotsAnimation DEFAULT = new DotsAnimation(5, "");

    private final int delay;
    private final String primary, secondary;

    public DotsAnimation(int delay, String primary) {
        this(delay, primary, null);
    }

    @Override
    public String[] getCalculatedFrames() {
        List<String> frames = new ArrayList<>();
        frames.addAll(Collections.nCopies(delay, primary + "." + (secondary != null ? secondary + ".." : "")));
        frames.addAll(Collections.nCopies(delay, secondary == null ? primary + ".." : secondary + '.' + primary + '.' + secondary + '.'));
        frames.addAll(Collections.nCopies(delay, secondary == null ? "..." : secondary + ".." + primary + '.'));
        return frames.toArray(new String[frames.size()]);
    }
}