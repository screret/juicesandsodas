package io.screret.github.juicesandsodas;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import io.screret.github.juicesandsodas.containers.BlenderBlockScreen;
import io.screret.github.juicesandsodas.creativeTabs.ModCreativeTabs;
import io.screret.github.juicesandsodas.entities.KoolaidMan;
import io.screret.github.juicesandsodas.init.Registration;
import io.screret.github.juicesandsodas.properties.block.blender.BlenderOnModel;
import io.screret.github.juicesandsodas.trees.FruitTypeExtension;
import io.screret.github.juicesandsodas.util.BlenderRecipes;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
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
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Base.MODID)
public class Base {

    public static final String MODID = "juicesandsodas";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final ModCreativeTabs MOD_TAB = new ModCreativeTabs();

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
        Registration.ITEMS.register(modEventBus);
        Registration.ENTITIES.register(modEventBus);
        Registration.TILES.register(modEventBus);
        Registration.CONTAINERS.register(modEventBus);
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
        List<FruitType> types = Arrays.asList(FruitType.LIME, FruitType.MANDARIN);
            FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
            pot.addPlant(Registration.MANDARIN_SAPLING.get().getRegistryName(), () -> Registration.POTTED_MANDARIN.get());
            pot.addPlant(Registration.LIME_SAPLING.get().getRegistryName(), () -> Registration.POTTED_LIME.get());
            pot.addPlant(Registration.ORANGE_SAPLING.get().getRegistryName(), () -> Registration.POTTED_ORANGE.get());
            pot.addPlant(Registration.LEMON_SAPLING.get().getRegistryName(), () -> Registration.POTTED_LEMON.get());
            pot.addPlant(Registration.GRAPEFRUIT_SAPLING.get().getRegistryName(), () -> Registration.POTTED_GRAPEFRUIT.get());
            pot.addPlant(Registration.APPLE_SAPLING.get().getRegistryName(), () -> Registration.POTTED_APPLE.get());
            pot.addPlant(Registration.CHERRY_SAPLING.get().getRegistryName(), () -> Registration.POTTED_CHERRY.get());


            for (FruitType type : FruitType.values()) {
                ComposterBlock.CHANCES.put(type.fruit, 0.5f);
                ComposterBlock.CHANCES.put(type.leaves.asItem(), 0.3f);
                ComposterBlock.CHANCES.put(type.sapling.get().asItem(), 0.3f);
            }

        ImmutableList.Builder<Supplier<ConfiguredFeature<?, ?>>> builder = ImmutableList.builder();
        for (FruitType type : types) {
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
        Registration.makeFeature("1", 1, 0, 2);
        Registration.trees = null;
        Registration.cherry = null;

        Registration.AddRecipes();
        LOGGER.debug("recipes registered: {}", BlenderRecipes.getBlenderRecipes());

        DeferredWorkQueue.runLater(() -> GlobalEntityTypeAttributes.put(Registration.KOOLAIDMAN.get(), KoolaidMan.registerAttributes().create()));
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        DeferredWorkQueue.runLater(() -> ScreenManager.registerFactory(Registration.BLENDER_CONT.get(), BlenderBlockScreen::new));
        LOGGER.debug("Screens Registered");
        RenderingRegistry.registerEntityRenderingHandler(Registration.KOOLAIDMAN.get(), KoolaidMan.Renderer::new);
        ClientRegistry.bindTileEntityRenderer(Registration.BLENDER_TILE.get(), BlenderOnModel::new);
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
}
