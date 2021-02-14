package io.screret.github.juicesandsodas.init;

import com.google.common.collect.ImmutableList;
import io.screret.github.juicesandsodas.Base;
import io.screret.github.juicesandsodas.entities.KoolaidMan;
import io.screret.github.juicesandsodas.items.ItemKoolAid;
import io.screret.github.juicesandsodas.items.ItemLemonade;
import io.screret.github.juicesandsodas.items.ItemMagicAid;
import io.screret.github.juicesandsodas.items.armor.ModArmor;
import io.screret.github.juicesandsodas.materials.ModMaterials;
import io.screret.github.juicesandsodas.plants.LemonTree;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Base.MODID)
@ObjectHolder(Base.MODID)
public class ModStuff {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Base.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Base.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Base.MODID);

    //blocks
    public static final RegistryObject<Block> LEMON_LEAVES = BLOCKS.register("lemon_leaves", () -> new LeavesBlock(AbstractBlock.Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()).setRegistryName("lemon_leaves"));
    public static final RegistryObject<Block> LEMON_SAPLING = BLOCKS.register("lemon_sapling", () -> new SaplingBlock(new LemonTree(), AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().zeroHardnessAndResistance().sound(SoundType.PLANT)).setRegistryName("lemon_sapling"));

    
    //Items
    //drinks
    public static final RegistryObject<Item> KOOL_AID = ITEMS.register("kool_aid", () -> new ItemKoolAid(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1)).setRegistryName(Base.MODID, "kool_aid"));
    public static final RegistryObject<Item> MAGIC_AID = ITEMS.register("magic_aid", () -> new ItemMagicAid(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1)).setRegistryName(Base.MODID, "magic_aid"));
    public static final RegistryObject<Item> LEMONADE = ITEMS.register("lemonade", () -> new ItemLemonade(new Item.Properties().group(Base.MOD_TAB).maxStackSize(1)).setRegistryName(Base.MODID, "lemonade"));
    //empty bottles
    public static final RegistryObject<Item> KOOL_AID_EMPTY = ITEMS.register("kool_aid_empty", () -> new Item(new Item.Properties().group(Base.MOD_TAB)).setRegistryName(Base.MODID, "kool_aid_empty"));
    public static final RegistryObject<Item> LEMONADE_EMPTY = ITEMS.register("lemonade_empty", () -> new Item(new Item.Properties().group(Base.MOD_TAB)).setRegistryName(Base.MODID, "lemonade_empty"));
    //normies
    public static final RegistryObject<Item> JELLO = ITEMS.register("jello", () -> new Item(new Item.Properties().group(Base.MOD_TAB)).setRegistryName(Base.MODID, "jello"));
    public static final RegistryObject<Item> LEMON = ITEMS.register("lemon", () -> new Item(new Item.Properties().group(Base.MOD_TAB)).setRegistryName(Base.MODID, "lemon"));
    //armor
    public static final RegistryObject<Item> JELLO_HELMET = ITEMS.register("jello_helmet", () -> new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.HEAD, new Item.Properties().group(Base.MOD_TAB)).setRegistryName(Base.MODID, "jello_helmet"));
    public static final RegistryObject<Item> JELLO_CHESTPLATE = ITEMS.register("jello_chestplate", () -> new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.CHEST, new Item.Properties().group(Base.MOD_TAB)).setRegistryName(Base.MODID, "jello_chestplate"));
    public static final RegistryObject<Item> JELLO_LEGGINGS = ITEMS.register("jello_leggings", () -> new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.LEGS, new Item.Properties().group(Base.MOD_TAB)).setRegistryName(Base.MODID, "jello_leggings"));
    public static final RegistryObject<Item> JELLO_BOOTS = ITEMS.register("jello_boots", () -> new ModArmor(ModMaterials.JELLO_ARMOR, EquipmentSlotType.FEET, new Item.Properties().group(Base.MOD_TAB)).setRegistryName(Base.MODID, "jello_boots"));
    //BlockItems
    public static final RegistryObject<Item> LEMON_LEAVES_ITEM = ITEMS.register("lemon_leaves", () -> new BlockItem(ModStuff.LEMON_LEAVES.get(), new Item.Properties().group(Base.MOD_TAB)).setRegistryName(LEMON_LEAVES.get().getRegistryName()));
    public static final RegistryObject<Item> LEMON_SAPLING_ITEM = ITEMS.register("lemon_sapling", () -> new BlockItem(ModStuff.LEMON_SAPLING.get(), new Item.Properties().group(Base.MOD_TAB)).setRegistryName(LEMON_SAPLING.get().getRegistryName()));
    
    
    //entities
    //bosses
    public static final RegistryObject<EntityType<?>> KOOLAIDMAN = ENTITIES.register("koolaid_man", () -> new KoolaidMan(EntityType.IRON_GOLEM, Minecraft.getInstance().world).getType());

    //Features
    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> LEMON_TREE = Feature.TREE.withConfiguration((
            new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()),
                    new SimpleBlockStateProvider(LEMON_LEAVES.get().getDefaultState()),
                    new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3),
                    new StraightTrunkPlacer(3, 1, 0),
                    new TwoLayerFeature(1, 0, 1))
                    .setIgnoreVines()
                    .setDecorators(ImmutableList.of(new BeehiveTreeDecorator(0.05F))))
            .build());

    public ModStuff(){
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModStuff.BLOCKS.register(modEventBus);
        ModStuff.ITEMS.register(modEventBus);
        ModStuff.ENTITIES.register(modEventBus);
    }
}
