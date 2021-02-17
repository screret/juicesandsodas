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
                                                                                
    private List<Supplier<ConfiguredFeature<?, ?>>> trees;
    private ConfiguredFeature<?, ?> cherry;
    private static ConfiguredFeature<?, ?>[] allFeatures;

    private void makeFeature(String id, int count, float chance, int index) {
        ConfiguredFeature<?, ?> cf = Feature.SIMPLE_RANDOM_SELECTOR.withConfiguration(new SingleRandomFeature(trees)).withPlacement(Features.Placements.field_244001_l).withPlacement(Placement./*COUNT_EXTRA_HEIGHTMAP*/field_242902_f.configure(new AtSurfaceWithExtraConfig(count, chance, 1)));
        Registry.register(WorldGenRegistries.field_243653_e, "fruittrees:trees_" + id, cf);
        allFeatures[index] = cf;
        if (chance > 0 && cherry != null) {
            cf = cherry.withPlacement(Features.Placements.field_244001_l).withPlacement(Placement./*COUNT_EXTRA_HEIGHTMAP*/field_242902_f.configure(new AtSurfaceWithExtraConfig(count, chance / 2, 1)));
            Registry.register(WorldGenRegistries.field_243653_e, "fruittrees:cherry_" + id, cf);
            allFeatures[index + 3] = cf;
        }
    }

    public static void insertFeatures(BiomeLoadingEvent event) {
        if (!FruitsConfig.worldGen) {
            return;
        }
        Climate climate = event.getClimate();
        if (climate.field_242460_b != RainType.RAIN) {
            return;
        }
        if (climate.field_242462_d == TemperatureModifier.FROZEN) {
            return;
        }
        Category category = event.getCategory();
        int i;
        switch (category) {
        case JUNGLE:
            i = 2;
            break;
        case FOREST:
            i = 1;
            break;
        case PLAINS:
            i = 0;
            break;
        default:
            return;
        }
        event.getGeneration().func_242513_a(Decoration.VEGETAL_DECORATION, allFeatures[i]);
        if (category != Category.JUNGLE && FruitTypeExtension.CHERRY != null) {
            event.getGeneration().func_242513_a(Decoration.VEGETAL_DECORATION, allFeatures[i + 3]);
        }
    }

    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> buildTreeFeature(FruitType type, boolean worldGen, BlockStateProvider carpetProvider) {
        BlockStateProvider leavesProvider;
        List<TreeDecorator> decorators;
        if (worldGen) {
            if (carpetProvider == null) {
                decorators = ImmutableList.of(new BeehiveTreeDecorator(0.05F));
            } else {
                decorators = ImmutableList.of(new BeehiveTreeDecorator(0.05F), new CarpetTreeDecorator(carpetProvider));
            }
            leavesProvider = new WeightedBlockStateProvider().addWeightedBlockstate(type.leaves.getDefaultState(), 2).addWeightedBlockstate(type.leaves.getDefaultState().with(FruitLeavesBlock.AGE, 2), 1);
        } else {
            decorators = ImmutableList.of();
            leavesProvider = new SimpleBlockStateProvider(type.leaves.getDefaultState());
        }
        return Feature./*TREE*/field_236291_c_.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(type.log.getDefaultState()), leavesProvider, new FruitBlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayerFeature(1, 0, 1)).setIgnoreVines()/*.setSapling(type.sapling.get())*/./*decorators*/func_236703_a_(decorators).build()));
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void handleBlockColor(ColorHandlerEvent.Block event) {
        BlockState oakLeaves = Blocks.OAK_LEAVES.getDefaultState();
        BlockColors blockColors = event.getBlockColors();
        blockColors.register((state, world, pos, i) -> {
            if (i == 0) {
                return blockColors.getColor(oakLeaves, world, pos, i);
            }
            if (i == 1) {
                Block block = state.getBlock();
                if (block == CITRON_LEAVES)
                    return 0xDDCC58;
                if (block == GRAPEFRUIT_LEAVES)
                    return 0xF4502B;
                if (block == LEMON_LEAVES)
                    return 0xEBCA4B;
                if (block == LIME_LEAVES)
                    return 0xCADA76;
                if (block == MANDARIN_LEAVES)
                    return 0xF08A19;
                if (block == ORANGE_LEAVES)
                    return 0xF08A19;
                if (block == POMELO_LEAVES)
                    return 0xF7F67E;
                if (block == APPLE_LEAVES)
                    return 0xFC1C2A;
            }
            return -1;
        }, MANDARIN_LEAVES, LIME_LEAVES, CITRON_LEAVES, POMELO_LEAVES, ORANGE_LEAVES, LEMON_LEAVES, GRAPEFRUIT_LEAVES, APPLE_LEAVES);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void handleItemColor(ColorHandlerEvent.Item event) {
        ItemStack oakLeaves = new ItemStack(Items.OAK_LEAVES);
        ItemColors itemColors = event.getItemColors();
        itemColors.register((stack, i) -> itemColors.getColor(oakLeaves, i), MANDARIN_LEAVES, LIME_LEAVES, CITRON_LEAVES, POMELO_LEAVES, ORANGE_LEAVES, LEMON_LEAVES, GRAPEFRUIT_LEAVES, APPLE_LEAVES);
    }
}
