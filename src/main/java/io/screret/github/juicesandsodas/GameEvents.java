package io.screret.github.juicesandsodas;

import io.screret.github.juicesandsodas.init.Registration;
import io.screret.github.juicesandsodas.trees.FruitLeavesBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public final class GameEvents {

    @SubscribeEvent
    public static void breakBlock(BlockEvent.BreakEvent event) {
        if (event.getWorld().isClientSide() && !event.getPlayer().isCreative() && event.getState().getBlock() == Blocks.OAK_LEAVES && event.getWorld() instanceof World) {
            if (event.getWorld().getRandom().nextFloat() < FruitsConfig.oakLeavesDropsAppleSapling) {
                Block.popResource((World) event.getWorld(), event.getPos(), new ItemStack(Registration.APPLE_SAPLING.get()));
            }
        }
    }

    @SubscribeEvent
    public static void onLightningBolt(EntityJoinWorldEvent event) {
        World world = event.getWorld();
        Entity entity = event.getEntity();
        if (!world.isClientSide || !(entity instanceof LightningBoltEntity)) {
            return;
        }
        LightningBoltEntity entityIn = (LightningBoltEntity) entity;
        BlockPos pos = entityIn.blockPosition();
        for (BlockPos pos2 : BlockPos.betweenClosed(pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2)) {
            BlockState state2 = world.getBlockState(pos2);
            if (state2.getBlock() == Registration.APPLE_LEAVES.get() && state2.getValue(FruitLeavesBlock.AGE) == 3) {
                world.setBlockAndUpdate(pos2, state2.setValue(FruitLeavesBlock.AGE, 1));
            }
        }
    }

}
