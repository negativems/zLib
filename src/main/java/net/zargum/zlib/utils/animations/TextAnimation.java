package net.zargum.zlib.utils.animations;

public abstract class TextAnimation {

    public abstract String[] getCalculatedFrames();

    public String getCurrentFrame() {
        String[] frames = getCalculatedFrames();
        int currentFrame = (int) (System.currentTimeMillis() / 50L % frames.length);
        return frames[currentFrame];
    }

    @Override
    public String toString() {
        return getCurrentFrame();
    }
}