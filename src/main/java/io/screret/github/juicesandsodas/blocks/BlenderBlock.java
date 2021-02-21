package io.screret.github.juicesandsodas.blocks;

import io.screret.github.juicesandsodas.containers.BlenderBlockContainer;
import io.screret.github.juicesandsodas.tileentities.BlenderTile;
import io.screret.github.juicesandsodas.tileentities.IBlenderRod;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.Nullable;

public class BlenderBlock extends Block {
    public BlenderBlock(Properties properties) {
        super(AbstractBlock.Properties.create(Material.IRON).notSolid());
    }

    protected static final VoxelShape SHAPE = Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 10.0D, 13.0D);

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
     return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState();//.with(BlockStateProperties.HORIZONTAL_FACING, context.getNearestLookingDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof BlenderTile) {
                INamedContainerProvider containerProvider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("gui.juicesandsodas.blender");
                    }

                    @Override
                    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                        BlenderTile tile = (BlenderTile) tileEntity;
                        return new BlenderBlockContainer(i, playerInventory, new CombinedInvWrapper(tile.inputSlot, tile.outputSlotWrapper), tile);
                    }
                };
                NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getPos());
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.POWERED);
    }


    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) {
        return new BlenderTile();
    }

    @OnlyIn(Dist.CLIENT)
    public static TileEntityMerger.ICallback<BlenderTile, Float2FloatFunction> getLidRotationCallback(final IBlenderRod rod) {
        return new TileEntityMerger.ICallback<BlenderTile, Float2FloatFunction>() {

            @Override
            public Float2FloatFunction func_225539_a_(BlenderTile p_225539_1_, BlenderTile p_225539_2_) {
                return p_225539_1_::getRodAngle;
            }

            @Override
            public Float2FloatFunction func_225538_a_(BlenderTile p_225538_1_) {
                return p_225538_1_::getRodAngle;
            }

            public Float2FloatFunction func_225537_b_() {
                return rod::getRodAngle;
            }
        };
    }
}
