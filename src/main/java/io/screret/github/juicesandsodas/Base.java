package io.screret.github.juicesandsodas;

import com.google.common.collect.ImmutableList;
import io.screret.github.juicesandsodas.creativeTabs.ModCreativeTabs;
import io.screret.github.juicesandsodas.init.ModStuff;
import io.screret.github.juicesandsodas.plants.ModFeatures;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.TwoLayerFeature;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        ModStuff.BLOCKS.register(modEventBus);
        ModStuff.ITEMS.register(modEventBus);
        ModStuff.ENTITIES.register(modEventBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        ModFeatures.LEMON_TREE = Feature.TREE.withConfiguration((
                new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()),
                        new SimpleBlockStateProvider(ModStuff.LEMON_LEAVES.get().getDefaultState()),
                        new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3),
                        new StraightTrunkPlacer(3, 1, 0),
                        new TwoLayerFeature(1, 0, 1))
                        .setIgnoreVines()
                        .setDecorators(ImmutableList.of(new BeehiveTreeDecorator(0.05F))))
                .build());
        ModFeatures.TREE_LEMON_CONFIG = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "tree_lemon", Feature.TREE.withConfiguration(ModFeatures.LEMON_TREE.config));
        //DeferredWorkQueue.runLater(() -> { GlobalEntityTypeAttributes.put((EntityType<? extends LivingEntity>) ModStuff.KOOLAIDMAN.get(), KoolaidMan.setCustomAttributes().create()); });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("juicesandsodas", "helloworld", () -> { LOGGER.info("Hello world from Juices 6 Sodas"); return "Hello world";});
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
