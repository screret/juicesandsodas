package io.screret.github.juicesandsodas.trees;

import com.mojang.serialization.Codec;
import io.screret.github.juicesandsodas.init.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class CarpetTreeDecorator extends TreeDecorator {
    public static final Codec<CarpetTreeDecorator> CODEC = BlockStateProvider.CODEC.fieldOf("provider").xmap(CarpetTreeDecorator::new, decorator -> {
        return decorator.carpetProvider;
    }).codec();
    private final BlockStateProvider carpetProvider;

    public CarpetTreeDecorator(BlockStateProvider carpetProvider) {
        this.carpetProvider = carpetProvider;
    }

    @Override
    protected TreeDecoratorType<?> getDecoratorType() {
        return Registration.CARPET_DECORATOR;
    }

    @Override
    public void func_225576_a_(ISeedReader world, Random rand, List<BlockPos> trunkList, List<BlockPos> foliageList, Set<BlockPos> allBlocks, MutableBoundingBox boundingBox) {
        if (foliageList.isEmpty()) {
            return;
        }
        int y = foliageList.get(0).getY() + 1;
        for (BlockPos pos : foliageList) {
            if (pos.getY() > y) {
                break;
            }
            if (placeCarpet(world, pos, carpetProvider.getState(rand, pos), 19)) {
                allBlocks.add(pos);
                boundingBox.expand(new MutableBoundingBox(pos, pos));
            }
        }
    }

    public static boolean placeCarpet(IWorld world, BlockPos pos, BlockState carpet, int flags) {
        int i = 0;
        BlockPos ground = pos;
        BlockState groundState = Blocks.AIR.defaultBlockState();
        while (++i < 5) {
            ground = pos.below(i);
            groundState = world.getBlockState(ground);
            if (groundState.getBlock() != Blocks.GRASS && groundState.getBlock() != Blocks.FERN && !groundState.getBlock().isAir(groundState, world, ground)) {
                if (i == 1) {
                    return false;
                } else {
                    break;
                }
            }
        }
        Block block = groundState.getBlock();
        if (block == Blocks.SNOW || block == Blocks.ICE || block == Blocks.PACKED_ICE || block == Blocks.BARRIER || block == Blocks.HONEY_BLOCK || block == Blocks.SOUL_SAND) {
            return false;
        }
        if (!Block.isShapeFullBlock().getCollisionShape(world, ground), Direction.UP)) {
            return false;
        }
        return world.(ground.up(), carpet, flags);
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return null;
    }

    @Override
    public void place(ISeedReader p_225576_1_, Random p_225576_2_, List<BlockPos> p_225576_3_, List<BlockPos> p_225576_4_, Set<BlockPos> p_225576_5_, MutableBoundingBox p_225576_6_) {

    }
}
