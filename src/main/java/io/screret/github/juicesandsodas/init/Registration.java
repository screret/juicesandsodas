package io.screret.github.juicesandsodas.init;

import com.google.common.collect.ImmutableList;
import io.screret.github.juicesandsodas.Base;
import io.screret.github.juicesandsodas.FruitTree;
import io.screret.github.juicesandsodas.FruitType;
import io.screret.github.juicesandsodas.blocks.BlenderBlock;
import io.screret.github.juicesandsodas.containers.BlenderBlockContainer;
import io.screret.github.juicesandsodas.crafting.BlenderRecipeSerializer;
import io.screret.github.juicesandsodas.entities.KoolaidMan;
import io.screret.github.juicesandsodas.items.ItemDrink;
import io.screret.github.juicesandsodas.items.armor.ModArmor;
import io.screret.github.juicesandsodas.materials.ModMaterials;
import io.screret.github.juicesandsodas.tileentities.BlenderTile;
import io.screret.github.juicesandsodas.tileentities.FruitTreeTile;
import io.screret.github.juicesandsodas.trees.CarpetTreeDecorator;
import io.screret.github.juicesandsodas.trees.FruitBlobFoliagePlacer;
import io.screret.github.juicesandsodas.trees.FruitLeavesBlock;
import io.screret.github.juicesandsodas.trees.FruitTypeExtension;
import io.screret.github.juicesandsodas.util.BlenderRecipe;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Base.MODID)
public class Registration {

    public Registration() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registration.BLOCKS.register(modEventBus);
        Registration.ITEMS.register(modEventBus);
        Registration.ENTITIES.register(modEventBus);
        Registration.TILES.register(modEventBus);
        Registration.CONTAINERS.register(modEventBus);
        Registration.RECIPE_SERIALIZERS.register(modEventBus);
    }

    //registries
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Base.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Base.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Base.MODID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Base.MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Base.MODID);
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Base.MODID);
    //blocks
    //leaves
    public static final RegistryObject<Block> LIME_LEAVES = BLOCKS.register("lime_leaves", () -> new FruitLeavesBlock(() -> FruitType.LIME, AbstractBlock.Properties.from(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> ORANGE_LEAVES = BLOCKS.register("orange_leaves", () -> new FruitLeavesBlock(() -> FruitType.ORANGE, AbstractBlock.Properties.from(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> LEMON_LEAVES = BLOCKS.register("lemon_leaves", () -> new FruitLeavesBlock(() -> FruitType.LEMON, AbstractBlock.Properties.from(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> GRAPEFRUIT_LEAVES = BLOCKS.register("grapefruit_leaves", () -> new FruitLeavesBlock(() -> FruitType.GRAPEFRUIT, AbstractBlock.Properties.from(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> APPLE_LEAVES = BLOCKS.register("apple_leaves", () -> new FruitLeavesBlock(() -> FruitType.APPLE, AbstractBlock.Properties.from(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> MANDARIN_LEAVES = BLOCKS.register("mandarin_leaves", () -> new FruitLeavesBlock(() -> FruitType.MANDARIN, AbstractBlock.Properties.from(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> CHERRY_LEAVES = BLOCKS.register("cherry_leaves", () -> new FruitLeavesBlock(() -> FruitTypeExtension.CHERRY, AbstractBlock.Properties.from(Blocks.OAK_LEAVES)));
    //saplings
    public static final RegistryObject<Block> MANDARIN_SAPLING = BLOCKS.register("mandarin_sapling", () -> new SaplingBlock(new FruitTree(() -> FruitType.MANDARIN), AbstractBlock.Properties.from(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> LIME_SAPLING = BLOCKS.register("lime_sapling", () -> new SaplingBlock(new FruitTree(() -> FruitType.LIME), AbstractBlock.Properties.from(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> ORANGE_SAPLING = BLOCKS.register("orange_sapling", () -> new SaplingBlock(new FruitTree(() -> FruitType.ORANGE), AbstractBlock.Properties.from(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> LEMON_SAPLING = BLOCKS.register("lemon_sapling", () -> new SaplingBlock(new FruitTree(() -> FruitType.LEMON), AbstractBlock.Properties.from(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> GRAPEFRUIT_SAPLING = BLOCKS.register("grapefruit_sapling", () -> new SaplingBlock(new FruitTree(() -> FruitType.GRAPEFRUIT), AbstractBlock.Properties.from(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> APPLE_SAPLING = BLOCKS.register("apple_sapling", () -> new SaplingBlock(new FruitTree(() -> FruitType.APPLE), AbstractBlock.Properties.from(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> CHERRY_SAPLING = BLOCKS.register("cherry_sapling", () -> new SaplingBlock(new FruitTree(() -> FruitTypeExtension.CHERRY), AbstractBlock.Properties.from(Blocks.OAK_SAPLING)));

    //potted plants
    public static final RegistryObject<Block> POTTED_MANDARIN = BLOCKS.register("potted_mandarin", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, MANDARIN_SAPLING, AbstractBlock.Properties.from(Blocks.POTTED_JUNGLE_SAPLING)));
    public static final RegistryObject<Block> POTTED_LIME = BLOCKS.register("potted_lime", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, LIME_SAPLING, AbstractBlock.Properties.from(Blocks.POTTED_JUNGLE_SAPLING)));
    public static final RegistryObject<Block> POTTED_ORANGE = BLOCKS.register("potted_orange", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ORANGE_SAPLING, AbstractBlock.Properties.from(Blocks.POTTED_JUNGLE_SAPLING)));
    public static final RegistryObject<Block> POTTED_LEMON = BLOCKS.register("potted_lemon", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, LEMON_SAPLING, AbstractBlock.Properties.from(Blocks.POTTED_JUNGLE_SAPLING)));
    public static final RegistryObject<Block> POTTED_GRAPEFRUIT = BLOCKS.register("potted_grapefruit", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, GRAPEFRUIT_SAPLING, AbstractBlock.Properties.from(Blocks.POTTED_JUNGLE_SAPLING)));
    public static final RegistryObject<Block> POTTED_APPLE = BLOCKS.register("potted_apple", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CHERRY_SAPLING, AbstractBlock.Properties.from(Blocks.POTTED_JUNGLE_SAPLING)));
    public static final RegistryObject<Block> POTTED_CHERRY = BLOCKS.register("potted_cherry", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, APPLE_SAPLING, AbstractBlock.Properties.from(Blocks.POTTED_JUNGLE_SAPLING)));

    public static final RegistryObject<Block> BLENDER = BLOCKS.register("blender", () -> new BlenderBlock(AbstractBlock.Properties.create(Material.WOOD).notSolid()));

    public static Set<Block> ALL_LEAVES;


    //tile entities
    public static final RegistryObject<TileEntityType<BlenderTile>> BLENDER_TILE = TILES.register("blender", () -> TileEntityType.Builder.create(BlenderTile::new, BLENDER.get()).build(null));
    public static final RegistryObject<TileEntityType<FruitTreeTile>> FRUIT_TREE = TILES.register("fruit_tree", () -> new TileEntityType<>(FruitTreeTile::new, ALL_LEAVES, null));


    //Items
    //drinks
    public static final RegistryObject<Item> KOOL_AID = ITEMS.register("kool_aid", () -> new ItemDrink(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1), 41391, null, null));
    public static final RegistryObject<Item> MAGIC_AID = ITEMS.register("magic_aid", () -> new ItemDrink(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1), 3932107, new EffectInstance[]{new EffectInstance(Effect.get(9)), new EffectInstance(Effect.get(20))}, new ResourceLocation("minecraft:shaders/post/wobble.json")));
    public static final RegistryObject<Item> LEMONADE = ITEMS.register("lemonade", () -> new ItemDrink(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1), 14607039, null, null));
    public static final RegistryObject<Item> GRAPE_JUICE = ITEMS.register("grape_juice", () -> new ItemDrink(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1), 14607039, null, null));
    public static final RegistryObject<Item> LIME_SODA = ITEMS.register("lime_soda", () -> new ItemDrink(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1), 14607039, null, null));
    public static final RegistryObject<Item> CHERRY_JUICE = ITEMS.register("cherry_juice", () -> new ItemDrink(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1), 14607039, null, null));
    public static final RegistryObject<Item> ORANGE_JUICE = ITEMS.register("orange_juice", () -> new ItemDrink(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1), 14607039, null, null));
    //empty bottles
    public static final RegistryObject<Item> EMPTY_JUICE_BOTTLE = ITEMS.register("empty_juice_bottle", () -> new Item(new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> EMPTY_BOTTLE = ITEMS.register("empty_bottle", () -> new Item(new Item.Properties().group(Base.MOD_TAB)));
    //normies
    public static final RegistryObject<Item> JELLO = ITEMS.register("jello", () -> new Item(new Item.Properties().group(Base.MOD_TAB)));

    //fruits
    public static final RegistryObject<Item> LEMON = ITEMS.register("lemon", () -> new Item(new Item.Properties().group(Base.MOD_TAB).food(Foods.LEMON)));
    public static final RegistryObject<Item> MANDARIN = ITEMS.register("mandarin", () -> new Item(new Item.Properties().group(Base.MOD_TAB).food(Foods.MANDARIN)));
    public static final RegistryObject<Item> LIME = ITEMS.register("lime", () -> new Item(new Item.Properties().group(Base.MOD_TAB).food(Foods.LIME)));
    public static final RegistryObject<Item> ORANGE = ITEMS.register("orange", () -> new Item(new Item.Properties().group(Base.MOD_TAB).food(Foods.ORANGE)));
    public static final RegistryObject<Item> GRAPEFRUIT = ITEMS.register("grapefruit", () -> new Item(new Item.Properties().group(Base.MOD_TAB).food(Foods.GRAPEFRUIT)));
    public static final RegistryObject<Item> CHERRY = ITEMS.register("cherry", () ->  new Item(new Item.Properties().group(Base.MOD_TAB).food(Foods.CHERRY)));

    //armor
    public static final RegistryObject<Item> JELLO_HELMET = ITEMS.register("jello_helmet", () -> new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.HEAD, new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> JELLO_CHESTPLATE = ITEMS.register("jello_chestplate", () -> new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.CHEST, new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> JELLO_LEGGINGS = ITEMS.register("jello_leggings", () -> new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.LEGS, new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> JELLO_BOOTS = ITEMS.register("jello_boots", () -> new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.FEET, new Item.Properties().group(Base.MOD_TAB)));
    //BlockItems
    //leaves
    public static final RegistryObject<Item> LEMON_LEAVES_ITEM = ITEMS.register("lemon_leaves", () -> new BlockItem(Registration.LEMON_LEAVES.get(), new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> CHERRY_LEAVES_ITEM = ITEMS.register("cherry_leaves", () -> new BlockItem(Registration.CHERRY_LEAVES.get(), new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> ORANGE_LEAVES_ITEM = ITEMS.register("orange_leaves", () -> new BlockItem(Registration.ORANGE_LEAVES.get(), new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> GRAPEFRUIT_LEAVES_ITEM = ITEMS.register("grapefruit_leaves", () -> new BlockItem(Registration.GRAPEFRUIT_LEAVES.get(), new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> APPLE_LEAVES_ITEM = ITEMS.register("apple_leaves", () -> new BlockItem(Registration.APPLE_LEAVES.get(), new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> MANDARIN_LEAVES_ITEM = ITEMS.register("mandarin_leaves", () -> new BlockItem(Registration.MANDARIN_LEAVES.get(), new Item.Properties().group(Base.MOD_TAB)));
    //saplings
    public static final RegistryObject<Item> LEMON_SAPLING_ITEM = ITEMS.register("lemon_sapling", () -> new BlockItem(Registration.LEMON_SAPLING.get(), new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> MANDARIN_SAPLING_ITEM = ITEMS.register("mandarin_sapling", () -> new BlockItem(Registration.MANDARIN_SAPLING.get(), new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> LIME_SAPLING_ITEM = ITEMS.register("lime_sapling", () -> new BlockItem(Registration.LIME_SAPLING.get(), new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> ORANGE_SAPLING_ITEM = ITEMS.register("orange_sapling", () -> new BlockItem(Registration.ORANGE_SAPLING.get(), new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> GRAPEFRUIT_SAPLING_ITEM = ITEMS.register("grapefruit_sapling", () -> new BlockItem(Registration.GRAPEFRUIT_SAPLING.get(), new Item.Properties().group(Base.MOD_TAB)));
    public static final RegistryObject<Item> CHERRY_SAPLING_ITEM = ITEMS.register("cherry_sapling", () -> new BlockItem(Registration.CHERRY_SAPLING.get(), new Item.Properties().group(Base.MOD_TAB)));
    //others
    public static final RegistryObject<Item> BLENDER_ITEM = ITEMS.register("blender", () -> new BlockItem(Registration.BLENDER.get(), new Item.Properties().group(Base.MOD_TAB)));


    //entities
    //bosses
    public static final RegistryObject<EntityType<KoolaidMan>> KOOLAIDMAN = ENTITIES.register("koolaid_man", () -> EntityType.Builder.create(KoolaidMan::new, EntityClassification.MONSTER).size(1f, 1.5f).setTrackingRange(64).build("koolaid_man"));


    //containers
    public static final RegistryObject<ContainerType<BlenderBlockContainer>> BLENDER_CONT = CONTAINERS.register("blender", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        BlenderTile tile = (BlenderTile) world.getTileEntity(pos);
        return new BlenderBlockContainer(windowId, inv, new CombinedInvWrapper(tile.inputSlot, tile.outputSlot), tile);
    }));


    //blender recipes
    public static final RegistryObject<BlenderRecipeSerializer<BlenderRecipe>> BLENDER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("blending", () -> new BlenderRecipeSerializer(BlenderRecipe::new));


    //foods
    public static final class Foods {
        public static final Food MANDARIN = new Food.Builder().hunger(3).saturation(0.3f).build();
        public static final Food LIME = new Food.Builder().hunger(3).saturation(0.3f).build();
        public static final Food ORANGE = new Food.Builder().hunger(3).saturation(0.5f).build();
        public static final Food LEMON = new Food.Builder().hunger(2).saturation(1f).fastToEat().build();
        public static final Food GRAPEFRUIT = new Food.Builder().hunger(6).saturation(0.4f).build();
        public static final Food CHERRY = new Food.Builder().hunger(6).saturation(0.1f).build();
    }


    public static final TreeDecoratorType<CarpetTreeDecorator> CARPET_DECORATOR = new TreeDecoratorType<>(CarpetTreeDecorator.CODEC);

    public static final FoliagePlacerType<FruitBlobFoliagePlacer> BLOB_PLACER = new FoliagePlacerType<>(FruitBlobFoliagePlacer.CODEC);

    public static List<Supplier<ConfiguredFeature<?, ?>>> trees;
    public static ConfiguredFeature<?, ?> cherry;
    public static ConfiguredFeature<?, ?>[] allFeatures;

    public static void makeFeature(String id, int count, float chance, int index) {
        ConfiguredFeature<?, ?> cf = Feature.SIMPLE_RANDOM_SELECTOR.withConfiguration(new SingleRandomFeature(trees)).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(count, chance, 1)));
        net.minecraft.util.registry.Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "juicesandsodas:trees_" + id, cf);
        allFeatures[index] = cf;
        if (chance > 0 && cherry != null) {
            cf = cherry.withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(count, chance / 2, 1)));
            net.minecraft.util.registry.Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "juicesandsodas:cherry_" + id, cf);
            allFeatures[index + 3] = cf;
        }
    }

    public static void insertFeatures(BiomeLoadingEvent event) {
        Biome.Climate climate = event.getClimate();
        if (climate.precipitation != Biome.RainType.RAIN) {
            return;
        }
        if (climate.temperatureModifier == Biome.TemperatureModifier.FROZEN) {
            return;
        }
        Biome.Category category = event.getCategory();
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
        event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);
        if (category != Biome.Category.JUNGLE && FruitTypeExtension.CHERRY != null) {
            event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);
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
        return Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(type.log.getDefaultState()), leavesProvider, new FruitBlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(4, 2, 0), new TwoLayerFeature(1, 0, 1)).setIgnoreVines().setDecorators(decorators).build()));
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
                if (block == GRAPEFRUIT_LEAVES.get())
                    return 0xF4502B;
                if (block == LEMON_LEAVES.get())
                    return 0xEBCA4B;
                if (block == LIME_LEAVES.get())
                    return 0xCADA76;
                if (block == MANDARIN_LEAVES.get())
                    return 0xF08A19;
                if (block == ORANGE_LEAVES.get())
                    return 0xF08A19;
                if (block == APPLE_LEAVES.get())
                    return 0xFC1C2A;
            }
            return -1;
        }, MANDARIN_LEAVES.get(), LIME_LEAVES.get(), ORANGE_LEAVES.get(), LEMON_LEAVES.get(), GRAPEFRUIT_LEAVES.get(), APPLE_LEAVES.get());
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void handleItemColor(ColorHandlerEvent.Item event) {
        ItemStack oakLeaves = new ItemStack(Items.OAK_LEAVES);
        ItemColors itemColors = event.getItemColors();
        itemColors.register((stack, i) -> itemColors.getColor(oakLeaves, i), MANDARIN_LEAVES.get(), LIME_LEAVES.get(), ORANGE_LEAVES.get(), LEMON_LEAVES.get(), GRAPEFRUIT_LEAVES.get(), APPLE_LEAVES.get());
    }
}
