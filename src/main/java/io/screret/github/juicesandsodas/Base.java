package io.screret.github.juicesandsodas;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import io.screret.github.juicesandsodas.containers.BlenderBlockScreen;
import io.screret.github.juicesandsodas.creativeTabs.ModCreativeTabs;
import io.screret.github.juicesandsodas.entities.KoolaidMan;
import io.screret.github.juicesandsodas.init.Registration;
import io.screret.github.juicesandsodas.properties.block.blender.BlenderTileRenderer;
import io.screret.github.juicesandsodas.trees.FruitTypeExtension;
import io.screret.github.juicesandsodas.util.ModdedSpawnEggItem;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.stream.Collectors;

//import io.screret.github.juicesandsodas.entities.KoolaidMan;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Base.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Base.MODID)
public class Base {

    public static final String MODID = "juicesandsodas";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final ModCreativeTabs MOD_TAB = new ModCreativeTabs();

    public static ResourceLocation SHADER;

    public Base() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registration.BLOCKS.register(modEventBus);
        Registration.ENTITIES.register(modEventBus);
        Registration.ITEMS.register(modEventBus);
        Registration.TILES.register(modEventBus);
        Registration.CONTAINERS.register(modEventBus);
        Registration.RECIPE_SERIALIZERS.register(modEventBus);
        ModdedSpawnEggItem.initUnaddedEggs();
    }

    private void setup(final FMLCommonSetupEvent event) {
        Registration.ALL_LEAVES = Collections.synchronizedSet(Sets.newHashSet(Arrays.asList(
                Registration.MANDARIN_LEAVES.get(),
                Registration.LIME_LEAVES.get(),
                Registration.ORANGE_LEAVES.get(),
                Registration.LEMON_LEAVES.get(),
                Registration.GRAPEFRUIT_LEAVES.get(),
                Registration.APPLE_LEAVES.get()
        )));
            FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
            pot.addPlant(Registration.MANDARIN_SAPLING.get().getRegistryName(), Registration.POTTED_MANDARIN);
            pot.addPlant(Registration.LIME_SAPLING.get().getRegistryName(), Registration.POTTED_LIME);
            pot.addPlant(Registration.ORANGE_SAPLING.get().getRegistryName(), Registration.POTTED_ORANGE);
            pot.addPlant(Registration.LEMON_SAPLING.get().getRegistryName(), Registration.POTTED_LEMON);
            pot.addPlant(Registration.GRAPEFRUIT_SAPLING.get().getRegistryName(), Registration.POTTED_GRAPEFRUIT);
            pot.addPlant(Registration.APPLE_SAPLING.get().getRegistryName(), Registration.POTTED_APPLE);
            pot.addPlant(Registration.CHERRY_SAPLING.get().getRegistryName(), Registration.POTTED_CHERRY);


            for (FruitType type : FruitType.values()) {
                ComposterBlock.COMPOSTABLES.put(type.fruit, 0.5f);
                ComposterBlock.COMPOSTABLES.put(type.leaves.asItem(), 0.3f);
                ComposterBlock.COMPOSTABLES.put(type.sapling.get().asItem(), 0.3f);
            }

        ImmutableList.Builder<Supplier<ConfiguredFeature<?, ?>>> builder = ImmutableList.builder();
        for (FruitType type : FruitType.values()) {
            Supplier<ConfiguredFeature<?, ?>> cf = () -> Registration.buildTreeFeature(type, true, null);
            builder.add(cf);
        }

        Registration.trees = builder.build();
        if (FruitTypeExtension.CHERRY != null) {
            Registration.allFeatures = new ConfiguredFeature[5];
        } else {
            Registration.allFeatures = new ConfiguredFeature[3];
        }
        Registration.makeFeature("002", 0, .002f, 0);
        Registration.makeFeature("005", 0, .005f, 1);
        Registration.makeFeature("1", 1, 1, 2);
        Registration.trees = null;
        Registration.cherry = null;

        ClientRegistry.bindTileEntityRenderer(Registration.BLENDER_TILE.get(), BlenderTileRenderer::new);
        event.enqueueWork(() -> GlobalEntityTypeAttributes.put(Registration.KOOLAIDMAN.get(), KoolaidMan.registerAttributes().build()));
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> ScreenManager.register(Registration.BLENDER_CONT.get(), BlenderBlockScreen::new));
        LOGGER.debug("Screens Registered");
        RenderTypeLookup.setRenderLayer(Registration.BLENDER.get(), RenderType.translucent());
        RenderingRegistry.registerEntityRenderingHandler(Registration.KOOLAIDMAN.get(), KoolaidMan.Renderer::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("juicesandsodas", "helloworld", () -> { LOGGER.info("Hello world from Juices & Sodas"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        GameSettings settings = Minecraft.getInstance().options;

        if(settings.keyTogglePerspective.isDown()){
            PointOfView pointofview = settings.getCameraType();
            settings.setCameraType(settings.getCameraType().cycle());
            if (pointofview.isFirstPerson() != settings.getCameraType().isFirstPerson()) {
                mc.gameRenderer.checkEntityPostEffect(settings.getCameraType().isFirstPerson() ? Minecraft.getInstance().cameraEntity : null);
            }
            if(SHADER != null){
                mc.gameRenderer.loadEffect(SHADER);
            }
        }
    }

    @SubscribeEvent
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
            for(Supplier<ConfiguredFeature<?, ?>> tree : Registration.trees) {
                event.getGeneration().addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, tree.get());
            }
        }
        if(category == Biome.Category.JUNGLE){
            event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(Registration.KOOLAIDMAN.get(), 5, 0, 1));
        }
    }
}
