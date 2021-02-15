package io.screret.github.juicesandsodas.init;

import io.screret.github.juicesandsodas.Base;
import io.screret.github.juicesandsodas.blocks.BlenderBlock;
import io.screret.github.juicesandsodas.blocks.BlenderTile;
import io.screret.github.juicesandsodas.entities.KoolaidMan;
import io.screret.github.juicesandsodas.items.ItemKoolAid;
import io.screret.github.juicesandsodas.items.ItemLemonade;
import io.screret.github.juicesandsodas.items.ItemMagicAid;
import io.screret.github.juicesandsodas.items.armor.ModArmor;
import io.screret.github.juicesandsodas.materials.ModMaterials;
import io.screret.github.juicesandsodas.plants.LemonTree;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Base.MODID)
public class ModStuff {

    public static void init() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModStuff.BLOCKS.register(modEventBus);
        ModStuff.ITEMS.register(modEventBus);
        ModStuff.ENTITIES.register(modEventBus);
        ModStuff.TILES.register(modEventBus);
        ModStuff.CONTAINERS.register(modEventBus);
    }

    //registries
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Base.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Base.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Base.MODID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Base.MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Base.MODID);

    //blocks
    public static final RegistryObject<Block> LEMON_LEAVES = BLOCKS.register("lemon_leaves", () -> new LeavesBlock(AbstractBlock.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()));
    public static final RegistryObject<Block> LEMON_SAPLING = BLOCKS.register("lemon_sapling", () -> new SaplingBlock(new LemonTree(), AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().zeroHardnessAndResistance().sound(SoundType.PLANT)));
    public static final RegistryObject<Block> BLENDER = BLOCKS.register("blender", () -> new BlenderBlock(AbstractBlock.Properties.create(Material.IRON)));

    //tile entities
    public static final RegistryObject<TileEntityType<BlenderTile>> BLENDER_TILE = TILES.register("blender", () -> TileEntityType.Builder.create(BlenderTile::new, BLENDER.get()).build(null));


    //Items
    //drinks
    public static final RegistryObject<Item> KOOL_AID = ITEMS.register("kool_aid", () -> new ItemKoolAid(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1)));
    public static final RegistryObject<Item> MAGIC_AID = ITEMS.register("magic_aid", () -> new ItemMagicAid(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1)));
    public static final RegistryObject<Item> LEMONADE = ITEMS.register("lemonade", () -> new ItemLemonade(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1)));
    //empty bottles
    public static final RegistryObject<Item> KOOL_AID_EMPTY = ITEMS.register("kool_aid_empty", () -> new Item(new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> LEMONADE_EMPTY = ITEMS.register("lemonade_empty", () -> new Item(new Item.Properties().group(Base.MOD_TAB)));
    //normies
    public static final RegistryObject<Item> JELLO = ITEMS.register("jello", () -> new Item(new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> LEMON = ITEMS.register("lemon", () -> new Item(new Item.Properties().group(Base.MOD_TAB)));
    //armor
    public static final RegistryObject<Item> JELLO_HELMET = ITEMS.register("jello_helmet", () -> new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.HEAD, new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> JELLO_CHESTPLATE = ITEMS.register("jello_chestplate", () -> new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.CHEST, new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> JELLO_LEGGINGS = ITEMS.register("jello_leggings", () -> new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.LEGS, new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> JELLO_BOOTS = ITEMS.register("jello_boots", () -> new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.FEET, new Item.Properties().group(Base.MOD_TAB)));
    //BlockItems
    public static final RegistryObject<Item> LEMON_LEAVES_ITEM = ITEMS.register("lemon_leaves", () -> new BlockItem(ModStuff.LEMON_LEAVES.get(), new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> LEMON_SAPLING_ITEM = ITEMS.register("lemon_sapling", () -> new BlockItem(ModStuff.LEMON_SAPLING.get(), new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> BLENDER_ITEM = ITEMS.register("blender", () -> new BlockItem(ModStuff.BLENDER.get(), new Item.Properties().group(Base.MOD_TAB)));



    //entities
    //bosses
    public static final RegistryObject<EntityType<KoolaidMan>> KOOLAIDMAN = ENTITIES.register("koolaid_man", () -> EntityType.Builder.create(KoolaidMan::new, EntityClassification.MONSTER).size(1f, 1.5f).setTrackingRange(64).build("koolaid_man"));


    //containers

}
