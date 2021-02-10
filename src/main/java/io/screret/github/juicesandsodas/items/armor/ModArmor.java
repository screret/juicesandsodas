package io.screret.github.juicesandsodas.items.armor;

import io.screret.github.juicesandsodas.materials.ModArmorMaterial;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public class ModArmor extends ArmorItem {
    public ModArmor(ModArmorMaterial armorMaterial, EquipmentSlotType equipmentSlot, Properties properties){
        super(armorMaterial, equipmentSlot, properties);
    }
}
