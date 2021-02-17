package io.screret.github.juicesandsodas;

import net.minecraft.world.World;
import org.jetbrains.annotations.Range;

public final class FruitsConfig {



    public enum DropMode {
        NO_DROP,
        INDEPENDENT,
        ONE_BY_ONE
    }

    @Range(from = 0, to = 100)
    public static int growingSpeed = 5;
    public static DropMode fruitDropModeSingleplayer = DropMode.INDEPENDENT;
    public static DropMode fruitDropModeMultiplayer = DropMode.ONE_BY_ONE;

    public static DropMode getDropMode(World world) {
        return world.getServer().isDedicatedServer() ? fruitDropModeMultiplayer : fruitDropModeSingleplayer;
    }
    @Range(from = 0, to = 1)
    public static double oakLeavesDropsAppleSapling = 0.2;
    public static boolean worldGen = true;

}
