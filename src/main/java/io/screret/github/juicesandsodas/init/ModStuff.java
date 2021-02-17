package io.screret.github.juicesandsodas.init;

import io.screret.github.juicesandsodas.Base;
import io.screret.github.juicesandsodas.blocks.BlenderBlock;
import io.screret.github.juicesandsodas.containers.BlenderBlockContainer;
import io.screret.github.juicesandsodas.tileentities.BlenderTile;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
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
    public static final RegistryObject<Block> CITRON_LEAVES = BLOCKS.register("citron_leaves", () -> new FruitLeavesBlock(() -> FruitType.CITRON, blockProp(Blocks.OAK_LEAVES));
    public static final RegistryObject<Block> LIME_LEAVES = BLOCKS.register("lime_leaves", () -> new FruitLeavesBlock(() -> FruitType.LIME, blockProp(Blocks.OAK_LEAVES));
    public static final RegistryObject<Block> POMELO_LEAVES = BLOCKS.register("pomelo_leaves", () -> new FruitLeavesBlock(() -> FruitType.POMELO, blockProp(Blocks.OAK_LEAVES));
    public static final RegistryObject<Block> ORANGE_LEAVES = BLOCKS.register("orange_leaves", () -> new FruitLeavesBlock(() -> FruitType.ORANGE, blockProp(Blocks.OAK_LEAVES));
    public static final RegistryObject<Block> LEMON_LEAVES = BLOCKS.register("lemon_leaves", () -> new FruitLeavesBlock(() -> FruitType.LEMON, blockProp(Blocks.OAK_LEAVES));
    public static final RegistryObject<Block> GRAPEFRUIT_LEAVES = BLOCKS.register("grapefruit_leaves", () -> new FruitLeavesBlock(() -> FruitType.GRAPEFRUIT, blockProp(Blocks.OAK_LEAVES));
    public static final RegistryObject<Block> APPLE_LEAVES = BLOCKS.register("apple_leaves", () -> new FruitLeavesBlock(() -> FruitType.APPLE, blockProp(Blocks.OAK_LEAVES));
    public static final RegistryObject<Block> MANDARIN_LEAVES = BLOCKS.register("mandarin_leaves", () -> new FruitLeavesBlock(() -> FruitType.MANDARIN, blockProp(Blocks.OAK_LEAVES));
                                                                                
    public static final RegistryObject<Block> MANDARIN_SAPLING = BLOCKS.register("mandarin_sapling", () -> new SaplingBlock(new FruitTree(() -> FruitType.MANDARIN), blockProp(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> LIME_SAPLING = BLOCKS.register("lime_sapling", () -> new SaplingBlock(new FruitTree(() -> FruitType.LIME), blockProp(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> CITRON_SAPLING = BLOCKS.register("citron_sapling", () -> new SaplingBlock(new FruitTree(() -> FruitType.CITRON), blockProp(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> POMELO_SAPLING = BLOCKS.register("pomelo_sapling", () -> new SaplingBlock(new FruitTree(() -> FruitType.POMELO), blockProp(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> ORANGE_SAPLING = BLOCKS.register("orange_sapling", () -> new SaplingBlock(new FruitTree(() -> FruitType.ORANGE), blockProp(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> LEMON_SAPLING = BLOCKS.register("lemon_sapling", () -> new SaplingBlock(new FruitTree(() -> FruitType.LEMON), blockProp(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> GRAPEFRUIT_SAPLING = BLOCKS.register("grapefruit_sapling", () -> new SaplingBlock(new FruitTree(() -> FruitType.GRAPEFRUIT), blockProp(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> APPLE_SAPLING = BLOCKS.register("apple_sapling", () -> new SaplingBlock(new FruitTree(() -> FruitType.APPLE), blockProp(Blocks.OAK_SAPLING)));
                                                                                
    public static final RegistryObject<Block> BLENDER = BLOCKS.register("blender", () -> new BlenderBlock());

                                                          
    //tile entities
    public static final RegistryObject<TileEntityType<BlenderTile>> BLENDER_TILE = TILES.register("blender", () -> TileEntityType.Builder.create(BlenderTile::new, BLENDER.get()).build(null));
    //Items
    //drinks
    public static final RegistryObject<Item> KOOL_AID = ITEMS.register("kool_aid", () -> new ItemKoolAid(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1)));
    public static final RegistryObject<Item> MAGIC_AID = ITEMS.register("magic_aid", () -> new ItemMagicAid(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1)));
    public static final RegistryObject<Item> LEMONADE = ITEMS.register("lemonade", () -> new ItemLemonade(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1)));
    //empty bottles
    public static final RegistryObject<Item> KOOL_AID_EMPTY = ITEMS.register("kool_aid_empty", () -> new Item(neProperties().group(Base.MOD_TAB)));
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
    public static final RegistryObject<ContainerType<BlenderBlockContainer>> BLENDER_CONT = CONTAINERS.register("blender", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new BlenderBlockContainer(windowId, world, pos, inv, inv.player);
    }));
                                                                                
    @Name("carpet")
    public static final TreeDecoratorType<CarpetTreeDecorator> CARPET_DECORATOR = new TreeDecoratorType<>(CarpetTreeDecorator.CODEC);

    @Name("blob")
    public static final FoliagePlacerType<FruitBlobFoliagePlacer> BLOB_PLACER = new FoliagePlacerType<>(FruitBlobFoliagePlacer.CODEC);
}
