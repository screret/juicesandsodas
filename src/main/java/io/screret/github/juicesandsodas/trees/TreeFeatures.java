public class TreeFeatures {

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
