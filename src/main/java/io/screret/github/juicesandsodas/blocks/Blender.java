import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.*;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler
public class BlockBlender extends Block {
	

	public BlockBlender() {
		super(Block.Properties.create(Material.IRON).hardnessAndResistance(4f, 15f).harvestLevel(0)
				.harvestTool(ToolType.PICKAXE).sound(SoundType.METAL), "blender", BlockBlender.TEBlender.class);
		this.setDefaultState(this.stateContainer.getBaseState().with(StateProperties.FLUID, BathCraftingFluids.NONE).with(HorizontalBlock.HORIZONTAL_FACING, Direction.NORTH));
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {

		builder.add(StateProperties.FLUID).add(HorizontalBlock.HORIZONTAL_FACING);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
	}
	
	@Override
	public boolean validSide(BlockState state, Direction dir) {
		return true;
	}
	
	
	public static class TEBlender extends LockableLootTileEntity implements ITickableTileEntity{
		
		public IFluidHandler handler;
		public int timer;
		public ItemStack output = null;
		public boolean check;
		public float progress = 0;
		public float cycles = 0;
		public ItemStack isa = null;
		public ItemStack isb = null;
		public BathCraftingFluids f = null;
		public boolean pendingOutput = false;
		public int cranks;
		
		public TESimpleFluidMixer(final TileEntityType<?> tileEntityTypeIn) {
			super(tileEntityTypeIn, 2, new TranslationTextComponent(Registry.getBlock("simple_fluid_mixer").getTranslationKey()), Registry.getContainerId("simple_fluid_mixer"), ContainerSimpleFluidMixer.class);
		}
		
		public TESimpleFluidMixer() {
			this(Registry.getTileEntity("simple_fluid_mixer"));
		}

		@Override
		public boolean isAllowedInSlot(int slot, ItemStack stack) {
			return true;
		}
		
		@Override
		public boolean perform() {
			if(output == null || pendingOutput == true) {
				return false;
			}
			cranks++;
			return true;
			
		}
		
		@Override
		public void tick() {
			if(!world.isRemote) {
				if(timer++ == 20) {
					timer = 0;
					if(handler == null) {
						handler = General.getCapabilityFromDirection(this, "handler", Direction.DOWN, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
					}
					if(output != null) {
						boolean sendupdate = false;
						boolean end = false;
						if(pendingOutput) {
							Direction opdr = getBlockState().get(HorizontalBlock.HORIZONTAL_FACING).rotateYCCW();
							TileEntity te = world.getTileEntity(pos.offset(opdr));
							if(te != null) {
								LazyOptional<IItemHandler> h = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, opdr.getOpposite());
								IItemHandler handler = h.orElse(null);
								if(handler != null) {
									for(int i = 0; i < handler.getSlots(); i++) {
										if(handler.insertItem(i, output, true) == ItemStack.EMPTY) {
											handler.insertItem(i, output, false);
											output = null;
											progress = 0;
											cycles = 0;
											f = null;
											pendingOutput = false;
											sendupdate = true;
											end = true;
											world.setBlockState(pos, getBlockState().with(StateProperties.FLUID, BathCraftingFluids.NONE));
											break;
										}
									}
								}
							}
							
						}else {
							
							if(cranks >= 1) {
								cranks = 0;
								if(progress == cycles) {
									pendingOutput = true;
								}
								progress++;
								sendupdate = true;
							}
							
						}
						
						if(getBlockState().get(StateProperties.FLUID) != f && end == false) {
							world.setBlockState(pos, getBlockState().with(StateProperties.FLUID, f));
							sendupdate = true;
						}
						
						if(sendupdate) {
							sendUpdates();
						}
						
					}else {
						if(handler != null) {
							if(contents.get(0) != isa || contents.get(1) != isb) {
								
								check = true;
							}
							if(check == true) {
								isa = contents.get(0);
								isb = contents.get(1);
								check = false;
								
								BathCrafting crafting = world.getRecipeManager().getRecipe(BathCrafting.BATH_RECIPE, this, world).orElse(null);
								if(crafting != null && crafting.getFluid().isElectricMixerOnly() == false) {
									if(crafting.getFluid().getAssocFluid() == handler.getFluidInTank(0).getFluid() && handler.drain(crafting.getPercentage().getMB(), FluidAction.SIMULATE).getAmount() == crafting.getPercentage().getMB()) {
										handler.drain(crafting.getPercentage().getMB(), FluidAction.EXECUTE);
										isa.shrink(1);
										isb.shrink(1);
										isa = null;
										isb = null;
										output = crafting.getRecipeOutput().copy();
										cycles = crafting.getStirs();
										f = crafting.getFluid();
										pendingOutput = false;
										sendUpdates();
										
									}else {
										check = true;
									}
								}
							}
						}
					}
				}
				
			}
			
		}
		
		@Override
		public CompoundNBT write(CompoundNBT compound) {
			compound.putFloat("juicesandsodas:cycles", cycles);
			compound.putFloat("juicesandsodas:progress", progress);
			compound.putBoolean("juicesandsodas:pendingoutput", pendingOutput);
			if(f != null) {
				compound.putString("juicesandsodas:fluid", f.toString());
			}
			if(output != null) {
				CompoundNBT sub = new CompoundNBT();
				output.write(sub);
				compound.put("juicesandsodas:output", sub);
			}
			return super.write(compound);
		}
		
		@Override
		public void read(CompoundNBT compound) {
			super.read(compound);
			if(compound.contains("juicesandsodas:cycles")) {
				cycles = compound.getFloat("juicesandsodas:cycles");
			}
			if(compound.contains("juicesandsodas:progress")) {
				progress = compound.getFloat("juicesandsodas:progress");
			}
			if(compound.contains("juicesandsodas:pendingoutput")) {
				pendingOutput = compound.getBoolean("juicesandsodas:pendingoutput");
			}
			if(compound.contains("juicesandsodas:output")) {
				output = ItemStack.read(compound.getCompound("juicesandsodas:output"));
			}
			if(compound.contains("juicesandsodas:fluid")) {
				f = BathCraftingFluids.valueOf(compound.getString("juicesandsodas:fluid"));
			}
		}

	}

	public static class ContainerBlender extends Container<TEBlender>{
		
		private static final Pair<Integer, Integer> INPUT_A_POS = new Pair<>(63, 48);
		private static final Pair<Integer, Integer> INPUT_B_POS = new Pair<>(88, 48);
		private static final Pair<Integer, Integer> PLAYER_INV_POS = new Pair<>(8, 84);
		private static final Pair<Integer, Integer> PLAYER_HOTBAR_POS = new Pair<>(8, 142);
		
		public ContainerBlender(final int windowId, final PlayerInventory playerInventory, final TESimpleFluidMixer tileEntity) {
			super(Registry.getContainerType("simple_fluid_mixer"), windowId, tileEntity, playerInventory, PLAYER_INV_POS, PLAYER_HOTBAR_POS, 0);
			
			this.addSlot(new Slot(this.tileEntity, 0, INPUT_A_POS.getFirst(), INPUT_A_POS.getSecond()));
			this.addSlot(new Slot(this.tileEntity, 1, INPUT_B_POS.getFirst(), INPUT_B_POS.getSecond()));
		}
		
		public ContainerBlender(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
			this(windowId, playerInventory, General.getTileEntity(playerInventory, data, TESimpleFluidMixer.class));
		}
		
	}
}
