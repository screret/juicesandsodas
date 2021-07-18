package io.screret.github.juicesandsodas.blocks;

import io.screret.github.juicesandsodas.containers.BlenderBlockContainer;
import io.screret.github.juicesandsodas.tileentities.BlenderTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
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

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState();//.with(BlockStateProperties.HORIZONTAL_FACING, context.getNearestLookingDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult trace) {
        if (world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof BlenderTile) {
                INamedContainerProvider containerProvider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("gui.juicesandsodas.blender");
                    }

                    @Override
                    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                        BlenderTile tile = (BlenderTile) world.getBlockEntity(pos);
                        //LOGGER.debug(tile.isBlending() + ", " + tile.COOK_TIME + ", " + tile.blenderData.get(0) + ", " + tile.toString());
                        return new BlenderBlockContainer(i, playerInventory, new CombinedInvWrapper(tile.inputSlot, tile.bottleSlot, tile.outputSlot), tile);
                    }
                };
                NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!worldIn.isClientSide)
        {
            return;
        }

        if(!(newState.getBlock() instanceof BlenderBlock))
        {
            worldIn.removeBlockEntity(pos);
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

}
