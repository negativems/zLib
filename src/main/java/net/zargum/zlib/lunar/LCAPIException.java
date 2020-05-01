package net.zargum.zlib.lunar;

public class LCAPIException extends UnsupportedOperationException {

    public LCAPIException() {
        super("Lunar Client operation was executed while API was not loaded.");
    }
}
