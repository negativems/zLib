package net.zargum.zlib.sounds;

import lombok.Getter;
import org.bukkit.Sound;

public enum Sounds {

    SUCCESS(Sound.NOTE_PLING, 1F),
    ERROR(Sound.NOTE_PLING, 0.1F);

    @Getter private Sound sound;
    @Getter private float pitch;

    Sounds(Sound sound, float pitch) {
        this.sound = sound;
        this.pitch = pitch;
    }
}
