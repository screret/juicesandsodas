package io.screret.github.juicesandsodas.handlers;

import io.screret.github.juicesandsodas.Base;
import io.screret.github.juicesandsodas.init.ModBlocks;
import io.screret.github.juicesandsodas.init.ModItems;
import io.screret.github.juicesandsodas.items.ItemKoolAid;
import io.screret.github.juicesandsodas.items.ItemLemonade;
import io.screret.github.juicesandsodas.items.armor.ModArmor;
import io.screret.github.juicesandsodas.materials.ModMaterials;
import io.screret.github.juicesandsodas.util.RegistryUtil;
import io.screret.github.juicesandsodas.items.ItemMagicAid;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class RegistrationHandler {

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event){
        final Block[] blocks = {
                RegistryUtil.setBlockName(new Block(AbstractBlock.Properties.create(Material.ROCK)), "test_block")
        };
        event.getRegistry().registerAll(blocks);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event){
        final Item[] items = {
                RegistryUtil.setItemName(new ItemKoolAid(new Item.Properties().group(Base.MOD_TAB)), "kool_aid"),
                RegistryUtil.setItemName(new ItemLemonade(new Item.Properties().group(Base.MOD_TAB)), "lemonade"),
                RegistryUtil.setItemName(new Item(new Item.Properties().group(Base.MOD_TAB)), "jello"),
                RegistryUtil.setItemName(new Item(new Item.Properties().group(Base.MOD_TAB)), "kool_aid_empty"),
                RegistryUtil.setItemName(new Item(new Item.Properties().group(Base.MOD_TAB)), "lemonade_empty"),
                RegistryUtil.setItemName(new ItemMagicAid(new Item.Properties().group(Base.MOD_TAB)), "magic_aid"),

                RegistryUtil.setItemName(new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.HEAD, new Item.Properties().group(Base.MOD_TAB)), "jello_helmet"),
                RegistryUtil.setItemName(new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.CHEST, new Item.Properties().group(Base.MOD_TAB)), "jello_chestplate"),
                RegistryUtil.setItemName(new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.LEGS, new Item.Properties().group(Base.MOD_TAB)), "jello_leggings"),
                RegistryUtil.setItemName(new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.FEET, new Item.Properties().group(Base.MOD_TAB)), "jello_boots")
        };
        final Item[] itemBlocks = {
                new BlockItem(ModBlocks.TEST_BLOCK, new Item.Properties().group(Base.MOD_TAB)).setRegistryName(ModBlocks.TEST_BLOCK.getRegistryName())

        };

        event.getRegistry().registerAll(items);
        event.getRegistry().registerAll(itemBlocks);
    }
}
