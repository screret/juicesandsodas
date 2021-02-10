package io.screret.github.juicesandsodas.init;

import io.screret.github.juicesandsodas.Base;
import io.screret.github.juicesandsodas.potions.CustomPotion;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Base.MODID)
public class ModPotions {
    public ModPotions(){super();}
    public static final Potion KoolAid = new Potion("potion_koolaid", new EffectInstance[Effect.getId(Effect.get(9))]);
}
