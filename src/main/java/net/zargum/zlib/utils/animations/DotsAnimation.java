package net.zargum.zlib.utils.animations;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public final class DotsAnimation extends TextAnimation {

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
        return frames.toArray(new String[0]);
    }
}