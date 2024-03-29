package io.screret.github.juicesandsodas.blocks;

import io.screret.github.juicesandsodas.tileentities.BlenderTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class BlenderBlock extends Block {
    public BlenderBlock(Properties properties) {
        super(properties);
        this.defaultBlockState().setValue(BlockStateProperties.POWERED, false).setValue(BLENDING, false);
//        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("minecraft", "textures/block/oak_planks.png"));
//        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("minecraft", "textures/block/glass.png"));
//        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("minecraft", "textures/block/iron_block.png"));
    }

    public static BooleanProperty BLENDING = BooleanProperty.create("blending");

    public VoxelShape shape = VoxelShapes.or(Block.box(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D));

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return shape;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState();//.with(BlockStateProperties.HORIZONTAL_FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (!world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            LOGGER.debug(tileEntity.getBlockPos());
            if (tileEntity instanceof BlenderTile) {
                //player.openMenu(containerProvider);
                NetworkHooks.openGui((ServerPlayerEntity) player, (BlenderTile)tileEntity, tileEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
            return ActionResultType.CONSUME;
        }else{
            return ActionResultType.SUCCESS;
        }
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!state.is(newState.getBlock())) {
            TileEntity tileentity = worldIn.getBlockEntity(pos);
            InventoryHelper.dropContents(worldIn, pos, (BlenderTile)tileentity);
            worldIn.removeBlockEntity(pos);
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.POWERED, BLENDING);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) {
        return new BlenderTile();
    }

    @Nullable
    @Override
    public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
        TileEntity tileentity = world.getBlockEntity(pos);
        return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider)tileentity : null;
    }
}
