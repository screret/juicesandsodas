package io.screret.github.juicesandsodas.plants.features;

import io.screret.github.juicesandsodas.blocks.IBlockPosQuery;
import io.screret.github.juicesandsodas.util.BlockUtil;
import net.minecraft.block.*;
import net.minecraft.state.Property;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;

import java.util.Random;
import java.util.Set;

public abstract class TreeFeatureBase extends TreeFeature
{
    protected static abstract class BuilderBase<T extends BuilderBase, F extends TreeFeatureBase>
    {
        protected IBlockPosQuery placeOn;
        protected IBlockPosQuery replace;
        protected BlockState log;
        protected BlockState leaves;
        protected BlockState vine;
        protected BlockState hanging;
        protected BlockState trunkFruit;
        protected BlockState altLeaves;
        protected int minHeight;
        protected int maxHeight;

        public BuilderBase()
        {
            this.placeOn = (world, pos) -> world.getBlockState(pos).canSustainPlant(world, pos, Direction.UP, (SaplingBlock)Blocks.OAK_SAPLING);
            this.replace = (world, pos) -> world.getBlockState(pos).canBeReplacedByLeaves(world, pos) || world.getBlockState(pos).getBlock().isIn(BlockTags.SAPLINGS) || world.getBlockState(pos).getBlock() instanceof BushBlock;
            this.log = Blocks.OAK_LOG.getDefaultState();
            this.leaves = Blocks.OAK_LEAVES.getDefaultState();
            this.vine = Blocks.AIR.getDefaultState();
            this.hanging = Blocks.AIR.getDefaultState();
            this.trunkFruit = Blocks.AIR.getDefaultState();
            this.altLeaves = Blocks.AIR.getDefaultState();
        }

        public T placeOn(IBlockPosQuery a) {this.placeOn = a; return (T)this;}

        public T replace(IBlockPosQuery a) {this.replace = a; return (T)this;}

        public T log(BlockState a) {this.log = a; return (T)this;}

        public T leaves(BlockState a) {this.leaves = a; return (T)this;}

        public T vine(BlockState a)
        {
            this.vine = a;
            return (T)this;
        }
        public T hanging(BlockState a)
        {
            this.hanging = a;
            return (T)this;
        }
        public T trunkFruit(BlockState a)
        {
            this.trunkFruit = a;
            return (T)this;
        }

        public T altLeaves(BlockState a) {this.altLeaves = a; return (T)this;}

        public T minHeight(int a) {this.minHeight = a; return (T)this;}
        public T maxHeight(int a) {this.maxHeight = a; return (T)this;}

        abstract F create();
    }

    protected final IBlockPosQuery placeOn;
    protected final IBlockPosQuery replace;

    protected final BlockState log;
    protected final BlockState leaves;
    protected final BlockState altLeaves;
    protected final BlockState vine;
    protected final BlockState hanging;
    protected final BlockState trunkFruit;

    protected final int minHeight;
    protected final int maxHeight;

    protected Property logAxisProperty;

    protected TreeFeatureBase(IBlockPosQuery placeOn, IBlockPosQuery replace, BlockState log, BlockState leaves, BlockState altLeaves, BlockState vine, BlockState hanging, BlockState trunkFruit, int minHeight, int maxHeight)
    {
        super(BaseTreeFeatureConfig.CODEC.stable());

        this.placeOn = placeOn;
        this.replace = replace;
        this.log = log;
        this.leaves = leaves;
        this.logAxisProperty = BlockUtil.getAxisProperty(log);
        this.altLeaves = altLeaves;
        this.vine = vine;
        this.hanging = hanging;
        this.trunkFruit = trunkFruit;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    public boolean placeLeaves(IWorld world, BlockPos pos, Set<BlockPos> changedBlocks, MutableBoundingBox boundingBox)
    {
        if (this.replace.matches(world, pos))
        {
            this.placeBlock(world, pos, this.leaves, changedBlocks, boundingBox);
            return true;
        }
        return false;
    }

    public boolean placeLog(IWorld world, BlockPos pos, Set<BlockPos> changedBlocks, MutableBoundingBox boundingBox)
    {
        return this.placeLog(world, pos, null, changedBlocks, boundingBox);
    }

    public boolean placeLog(IWorld world, BlockPos pos, Direction.Axis axis, Set<BlockPos> changedBlocks, MutableBoundingBox boundingBox)
    {
        BlockState directedLog = (axis != null && this.logAxisProperty != null) ? this.log.getBlockState() : this.log;
        if (this.replace.matches(world, pos))
        {
            // Logs must be added to the "changedBlocks" so that the leaves have their distance property updated,
            // preventing incorrect decay
            this.placeBlock(world, pos, directedLog, changedBlocks, boundingBox);
            return true;
        }
        return false;
    }

    public boolean setAltLeaves(IWorld world, BlockPos pos, Set<BlockPos> changedBlocks, MutableBoundingBox boundingBox)
    {
        if (this.replace.matches(world, pos))
        {
            this.placeBlock(world, pos, this.altLeaves, changedBlocks, boundingBox);
            return true;
        }
        return false;
    }

    public boolean tryDoPlace(IWorldGenerationReader reader, Random random, BlockPos pos, Set<BlockPos> changedLogs, Set<BlockPos> changedLeaves, MutableBoundingBox boundingBox, BaseTreeFeatureConfig config)
    {
        return place(changedLogs, changedLeaves, (IWorld)reader, random, pos, boundingBox);
    }

    protected boolean place(Set<BlockPos> changedLogs, Set<BlockPos> changedLeaves, IWorld world, Random rand, BlockPos position, MutableBoundingBox boundingBox)
    {
        return false;
    }

    protected boolean placeBlock(IWorld world, BlockPos pos, BlockState state, Set<BlockPos> changedBlocks, MutableBoundingBox boundingBox)
    {
        if (!isAirAt(world, pos))
        {
            return false;
        }
        else
        {
            setBlock(world, pos, state, boundingBox);
            changedBlocks.add(pos.toImmutable());
            return true;
        }
    }

    protected void setBlock(IWorldWriter world, BlockPos pos, BlockState state, MutableBoundingBox boundingBox)
    {
        setBlockState(world, pos, state);
        boundingBox.expandTo(new MutableBoundingBox(pos, pos));
    }
}
