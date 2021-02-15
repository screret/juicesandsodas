public abstract class BlockTileEntity extends Block{

	private final String teName;
	private final VoxelShape shape;
	private final boolean shouldRotate;
	private final Direction dir;
	
	private static final VoxelShape NO_SHAPE_CUBE = VoxelShapes.fullCube();
	public BlockTileEntity(Properties properties, String teName) {
		super(properties);
		this.teName = teName;
		this.shape = null;
		this.shouldRotate = false;
		this.dir = null;
	}
	
	public BlockTileEntity(Properties properties, String teName, VoxelShape shape, boolean shouldRotate, Direction defaultDirection) {
		super(properties);
		this.teName = teName;
		this.shape = shape;
		this.shouldRotate = shouldRotate;
		this.dir = defaultDirection;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return Registry.getTileEntity(teName).create();
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	public abstract ActionResultType blockRightClickServer(BlockState state, World world, BlockPos pos, PlayerEntity player);
	
	public abstract ActionResultType blockRightClickClient(BlockState state, World world, BlockPos pos, PlayerEntity player);
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand hand, BlockRayTraceResult hit) {
		
		if(hand.equals(Hand.MAIN_HAND)) {
			if(!worldIn.isRemote) {
				return blockRightClickServer(state, worldIn, pos, player);
			}else {
				return blockRightClickClient(state, worldIn, pos, player);
			}
		}
		
		return ActionResultType.CONSUME;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if(shape != null) {
			if(shouldRotate == true && state.func_235901_b_(HorizontalBlock.HORIZONTAL_FACING)) {
				return General.rotateShape(dir, state.get(HorizontalBlock.HORIZONTAL_FACING), shape);
			}else {
				return shape;
			}
		}else {
			return NO_SHAPE_CUBE;
		}
	}
}
