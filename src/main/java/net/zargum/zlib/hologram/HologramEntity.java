package net.zargum.zlib.hologram;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.WorldServer;

public class HologramEntity extends EntityArmorStand {

    public HologramEntity(WorldServer world, double x, double y, double z) {
        super(world, x ,y, z);
        setCustomNameVisible(true);
        setGravity(false);
        setSmall(true);
        setInvisible(true);
        setBasePlate(false);
        setArms(false);
    }
}
