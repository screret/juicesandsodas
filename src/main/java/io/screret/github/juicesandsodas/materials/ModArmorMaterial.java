package io.screret.github.juicesandsodas.materials;

import io.screret.github.juicesandsodas.Base;
import io.screret.github.juicesandsodas.init.Registration;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import java.util.function.Supplier;

public enum ModArmorMaterial implements IArmorMaterial {

                    JELLO(Base.MODID + ":jello", 50, new int[]{10, 20, 30, 10}, 10, SoundEvents.ARMOR_EQUIP_GENERIC, 10.0F, 10.0F, () -> { return Ingredient.of(Registration.JELLO.get()); });

    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyValue<Ingredient> repairMaterial;

    ModArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) {
        this.name = name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductionAmountArray;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = new LazyValue<>(repairMaterial);
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlotType slotIn) {
        return 1;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType slotIn) {
        return 20;
    }

    @Override
    public int getEnchantmentValue() {
        return 10;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_GENERIC;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(Registration.JELLO.get());
    }

    @Override
    public String getName() {
        return Base.MODID + ":jello";
    }

    @Override
    public float getToughness() {
        return 10;
    }

    @Override
    public float getKnockbackResistance() {
        return 10;
    }
}
