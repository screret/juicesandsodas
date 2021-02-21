package io.screret.github.juicesandsodas.tileentities;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IBlenderRod {
    float getRodAngle(float partialTicks);
}
