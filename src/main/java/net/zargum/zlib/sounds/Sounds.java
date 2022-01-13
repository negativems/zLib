package net.zargum.zlib.sounds;

import lombok.Getter;
import org.bukkit.Sound;

public enum Sounds {

    SUCCESS(Sound.NOTE_PLING, 1F),
    CLICK(Sound.CLICK, 1F),
    ERROR(Sound.NOTE_PLING, 0.1F);

    @Getter private final Sound sound;
    @Getter private final float pitch;

    Sounds(Sound sound, float pitch) {
        this.sound = sound;
        this.pitch = pitch;
    }
}
