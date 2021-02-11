package io.screret.github.juicesandsodas.handlers;

import io.screret.github.juicesandsodas.Base;
import io.screret.github.juicesandsodas.init.ModBlocks;
import io.screret.github.juicesandsodas.init.ModItems;
import io.screret.github.juicesandsodas.items.ItemKoolAid;
import io.screret.github.juicesandsodas.items.ItemLemonade;
import io.screret.github.juicesandsodas.items.armor.ModArmor;
import io.screret.github.juicesandsodas.materials.ModMaterials;
import io.screret.github.juicesandsodas.plants.LemonTree;
import io.screret.github.juicesandsodas.util.RegistryUtil;
import io.screret.github.juicesandsodas.items.ItemMagicAid;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Base.MODID)
public class RegistrationHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){
        final Block[] blocks = {
                RegistryUtil.setBlockName(new Block(AbstractBlock.Properties.create(Material.ROCK)), "test_block"),
                RegistryUtil.setBlockName(new SaplingBlock(new LemonTree(), AbstractBlock.Properties.create(Material.PLANTS)), "lemon_sapling"),
                RegistryUtil.setBlockName(new LeavesBlock(AbstractBlock.Properties.create(Material.LEAVES)), "lemon_leaves"),
        };
        event.getRegistry().registerAll(blocks);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        final Item[] items = {
                RegistryUtil.setItemName(new ItemKoolAid(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1)), "kool_aid"),
                RegistryUtil.setItemName(new ItemLemonade(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1)), "lemonade"),
                RegistryUtil.setItemName(new Item(new Item.Properties().group(Base.MOD_TAB)), "jello"),
                RegistryUtil.setItemName(new Item(new Item.Properties().group(Base.MOD_TAB)), "kool_aid_empty"),
                RegistryUtil.setItemName(new Item(new Item.Properties().group(Base.MOD_TAB)), "lemonade_empty"),
                RegistryUtil.setItemName(new ItemMagicAid(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1)), "magic_aid"),

                RegistryUtil.setItemName(new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.HEAD, new Item.Properties().group(Base.MOD_TAB)), "jello_helmet"),
                RegistryUtil.setItemName(new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.CHEST, new Item.Properties().group(Base.MOD_TAB)), "jello_chestplate"),
                RegistryUtil.setItemName(new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.LEGS, new Item.Properties().group(Base.MOD_TAB)), "jello_leggings"),
                RegistryUtil.setItemName(new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.FEET, new Item.Properties().group(Base.MOD_TAB)), "jello_boots")
        };
        final Item[] blockItems = {
                new BlockItem(ModBlocks.TEST_BLOCK, new Item.Properties().group(Base.MOD_TAB)).setRegistryName(ModBlocks.TEST_BLOCK.getRegistryName()),
                new BlockItem(ModBlocks.LEMON_LEAVES, new Item.Properties().group(Base.MOD_TAB)).setRegistryName(ModBlocks.LEMON_LEAVES.getRegistryName()),
                new BlockItem(ModBlocks.LEMON_SAPLING, new Item.Properties().group(Base.MOD_TAB)).setRegistryName(ModBlocks.LEMON_SAPLING.getRegistryName()),

        };

        event.getRegistry().registerAll(items);
        event.getRegistry().registerAll(blockItems);
    }
}
