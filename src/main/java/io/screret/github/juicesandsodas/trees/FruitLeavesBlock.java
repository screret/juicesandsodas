package io.screret.github.juicesandsodas.trees;

import io.screret.github.juicesandsodas.FruitType;
import io.screret.github.juicesandsodas.FruitsConfig;
import io.screret.github.juicesandsodas.tileentities.FruitTreeTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class FruitLeavesBlock extends LeavesBlock implements IGrowable {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public final Supplier<FruitType> type;


    public FruitLeavesBlock(Supplier<FruitType> type, Properties properties) {
        super(properties);
        this.type = type;
        this.defaultBlockState().setValue(DISTANCE, 7).setValue(PERSISTENT, false).setValue(AGE, 1);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, AGE);
    }

    @Override
    public boolean isBonemealSuccess(World p_180670_1_, Random p_180670_2_, BlockPos p_180670_3_, BlockState state) {
        return canGrow(state) || state.getValue(AGE) == 1;
    }

    @Override
    public boolean isValidBonemealTarget(IBlockReader p_176473_1_, BlockPos p_176473_2_, BlockState state, boolean p_176473_4_) {
        return state.getValue(AGE) != 3;
    }

    @Override
    public void performBonemeal(ServerWorld world, Random rand, BlockPos pos, BlockState state) {
        if (state.getValue(AGE) == 3) {
            if (!world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS))
                return;
            switch (FruitsConfig.getDropMode(world)) {
            case INDEPENDENT:
                world.setBlockAndUpdate(pos, onPassiveGathered(world, pos, state));
                popResource(world, pos, new ItemStack(type.get().fruit));
                break;
            case ONE_BY_ONE:
                FruitTreeTile tile = findTile(world, pos, state);
                if (tile != null && tile.canDrop()) {
                    ItemStack stack = new ItemStack(type.get().fruit);
                    if (!stack.isEmpty() && !world.restoringBlockSnapshots) { // do not drop items while restoring blockstates, prevents item dupe
                        double d0 = world.random.nextFloat() * 0.5F + 0.25D;
                        double d1 = world.random.nextFloat() * 0.5F + 0.25D;
                        double d2 = world.random.nextFloat() * 0.5F + 0.25D;
                        ItemEntity itementity = new ItemEntity(world, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, stack);
                        itementity.setDefaultPickUpDelay();
                        if (world.addFreshEntity(itementity))
                            tile.setOnlyItem(itementity);
                    }
                }
                break;
            default:
                break;
            }
        } else {
            world.setBlockAndUpdate(pos, state.cycle(AGE));
        }
    }

    @Nullable
    public FruitTreeTile findTile(ServerWorld world, BlockPos pos, BlockState state) {
        if (state.hasTileEntity()) {
            TileEntity tile = world.getBlockEntity(pos);
            if (tile instanceof FruitTreeTile) {
                return (FruitTreeTile) tile;
            }
        } else {
            for (BlockPos pos2 : BlockPos.betweenClosed(pos.getX() - 2, pos.getY(), pos.getZ() - 2, pos.getX() + 2, pos.getY() + 3, pos.getZ() + 2)) {
                TileEntity tile = world.getBlockEntity(pos2);
                if (tile instanceof FruitTreeTile) {
                    return (FruitTreeTile) tile;
                }
            }
        }
        return null;
    }

    public BlockState onPassiveGathered(ServerWorld world, BlockPos pos, BlockState state) {
        int death = 30;
        FruitTreeTile tile = findTile(world, pos, state);
        if (tile != null)
            death = tile.updateDeathRate();
        if (death >= 50 || !state.hasTileEntity() && world.random.nextInt(50) < death) {
            return state.setValue(AGE, 0);
        } else {
            return state.setValue(AGE, 1);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.getValue(PERSISTENT) && state.getValue(DISTANCE) == 1;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FruitTreeTile(type.get());
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        if (shouldDecay(state)) {
            dropResources(state, world, pos);
            world.removeBlock(pos, false);
            } else if (canGrow(state) && world.isAreaLoaded(pos, 1) && world.getLightEmission(pos.above()) >= 9) {

            boolean def = rand.nextInt(100) > (99 - FruitsConfig.growingSpeed);

            if (ForgeHooks.onCropsGrowPre(world, pos, state, def)) {
                performBonemeal(world, rand, pos, state);
                ForgeHooks.onCropsGrowPost(world, pos, state);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        if (state.getValue(PERSISTENT) && state.getValue(DISTANCE) != 1) {
            state = state.setValue(PERSISTENT, false);
        }
        world.setBlock(pos, state, 3);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return shouldDecay(state) || canGrow(state) || state.getValue(AGE) == 0;
    }

    public boolean shouldDecay(BlockState state) {
        return state.getValue(DISTANCE) == 7 && !state.getValue(PERSISTENT);
    }

    public boolean canGrow(BlockState state) {
        return state.getValue(AGE) > 0 && !state.getValue(PERSISTENT) || state.getValue(DISTANCE) == 1;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (canGrow(state) || state.getValue(AGE) == 0) {
            return super.updateShape(state, facing, facingState, worldIn, currentPos, facingPos);
        }
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(PERSISTENT, true);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Entity entity = context.getEntity();
        return VoxelShapes.block();
    }

    @Override
    public void fallOn(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        super.fallOn(worldIn, pos, entityIn, fallDistance);
        if (worldIn.isClientSide && fallDistance >= 1 && (entityIn instanceof LivingEntity || entityIn instanceof FallingBlockEntity)) {
            for (BlockPos pos2 : BlockPos.betweenClosed(pos.getX() - 1, Math.max(0, pos.getY() - 2), pos.getZ() - 1, pos.getX() + 1, pos.getY(), pos.getZ() + 1)) {
                BlockState state = worldIn.getBlockState(pos2);
                if (state.getBlock() instanceof FruitLeavesBlock) {
                    if (state.getValue(AGE) == 3) {
                        ((FruitLeavesBlock) state.getBlock()).performBonemeal((ServerWorld) worldIn, worldIn.random, pos2, state);
                    }
                }
            }
        }
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult ray) {
        if (state.getValue(AGE) == 3 && worldIn.setBlockAndUpdate(pos, state.setValue(AGE, 1))) {
            if (worldIn.isClientSide) {
                ItemStack fruit = new ItemStack(type.get().fruit);
                if (playerIn instanceof FakePlayer) {
                    double d0 = worldIn.random.nextFloat() * 0.5F + 0.25D;
                    double d1 = worldIn.random.nextFloat() * 0.5F + 0.25D;
                    double d2 = worldIn.random.nextFloat() * 0.5F + 0.25D;
                    ItemEntity itementity = new ItemEntity(worldIn, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, fruit);
                    itementity.setDefaultPickUpDelay();
                    worldIn.addFreshEntity(itementity);
                } else {
                    ItemHandlerHelper.giveItemToPlayer(playerIn, fruit);
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, MobEntity entity) {
        if (entity instanceof IFlyingAnimal) {
            return PathNodeType.OPEN;
        }
        return null;
    }
}
