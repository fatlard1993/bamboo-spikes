package justfatlard.bamboo_spikes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BambooSpikesBlock extends Block implements Waterloggable {
	private static final DirectionProperty FACING = FacingBlock.FACING;
	public static final BooleanProperty WATERLOGGED;

	protected static final VoxelShape UP_SHAPE = Block.createCuboidShape(13.0D, 12.0D, 13.0D, 3.0D, 0.0D, 3.0D);
	protected static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(13.0D, 16.0D, 13.0D, 3.0D, 4.0D, 3.0D);
	protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(3.0D, 13.0D, 16.0D, 13.0D, 3.0D, 4.0D);
	protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(13.0D, 13.0D, 0.0D, 3.0D, 3.0D, 12.0D);
	protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(12.0D, 3.0D, 13.0D, 0.0D, 13.0D, 3.0D);
	protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(16.0D, 3.0D, 13.0D, 4.0D, 13.0D, 3.0D);

	public BambooSpikesBlock(Settings settings) {
		super(settings);

		this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.UP).with(WATERLOGGED, false)));
	}

	public FluidState getFluidState(BlockState state) {
		return (Boolean)state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if ((Boolean)state.get(WATERLOGGED)) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		BlockPos blockPos = context.getBlockPos();
		FluidState fluidState = context.getWorld().getFluidState(blockPos);
		Direction direction = context.getSide();
		BlockState blockState = context.getWorld().getBlockState(context.getBlockPos().offset(direction.getOpposite()));

		return (BlockState)((BlockState)this.getDefaultState().with(FACING, blockState.getBlock() == this && blockState.get(FACING) == direction ? direction.getOpposite() : direction)).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPosition, EntityContext entityContext) {
		switch((Direction)blockState.get(FACING)){
			case DOWN:
				return DOWN_SHAPE;
			case NORTH:
				return NORTH_SHAPE;
			case SOUTH:
				return SOUTH_SHAPE;
			case WEST:
				return WEST_SHAPE;
			case EAST:
				return EAST_SHAPE;
			default:
				return UP_SHAPE;
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState blockState, BlockView blockView, BlockPos blockPosition, EntityContext entityContext){
		return this.collidable ? blockState.getOutlineShape(blockView, blockPosition) : VoxelShapes.empty();
	}

	@Override
	public void onEntityCollision(BlockState blockState, World world, BlockPos blockPosition, Entity entity){
		if(entity.getType() == EntityType.PLAYER|| entity.getType().getCategory() != EntityCategory.MISC) entity.damage(DamageSource.CACTUS, 2.0F);

		super.onEntityCollision(blockState, world, blockPosition, entity);
	}

	@Override
	public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance){
		entity.handleFallDamage(distance, 5.0F);
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	static {
		WATERLOGGED = Properties.WATERLOGGED;
	}
}