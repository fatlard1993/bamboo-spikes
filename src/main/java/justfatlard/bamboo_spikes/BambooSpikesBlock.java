package justfatlard.bamboo_spikes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BambooSpikesBlock extends Block implements SimpleWaterloggedBlock {
	public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected static final VoxelShape UP_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 12.0D, 13.0D);
	protected static final VoxelShape DOWN_SHAPE = Block.box(3.0D, 4.0D, 3.0D, 13.0D, 16.0D, 13.0D);
	protected static final VoxelShape NORTH_SHAPE = Block.box(3.0D, 3.0D, 4.0D, 13.0D, 13.0D, 16.0D);
	protected static final VoxelShape SOUTH_SHAPE = Block.box(3.0D, 3.0D, 0.0D, 13.0D, 13.0D, 12.0D);
	protected static final VoxelShape EAST_SHAPE = Block.box(0.0D, 3.0D, 3.0D, 12.0D, 13.0D, 13.0D);
	protected static final VoxelShape WEST_SHAPE = Block.box(4.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D);

	public BambooSpikesBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.getStateDefinition().any()
			.setValue(FACING, Direction.UP)
			.setValue(WATERLOGGED, false));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos blockPos = context.getClickedPos();
		Direction direction = context.getClickedFace();
		BlockState blockState = context.getLevel().getBlockState(blockPos.relative(direction.getOpposite()));
		boolean waterlogged = context.getLevel().getFluidState(blockPos).getType() == Fluids.WATER;

		Direction facing = blockState.getBlock() == this && blockState.getValue(FACING) == direction
			? direction.getOpposite()
			: direction;

		return this.defaultBlockState()
			.setValue(FACING, facing)
			.setValue(WATERLOGGED, waterlogged);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case DOWN -> DOWN_SHAPE;
			case NORTH -> NORTH_SHAPE;
			case SOUTH -> SOUTH_SHAPE;
			case WEST -> WEST_SHAPE;
			case EAST -> EAST_SHAPE;
			default -> UP_SHAPE;
		};
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		// Return empty collision so entities can walk into the spikes and trigger entityInside
		return Shapes.empty();
	}

	@Override
	protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity, net.minecraft.world.entity.InsideBlockEffectApplier effectApplier, boolean movedByPiston) {
		if (!world.isClientSide() && world instanceof ServerLevel serverWorld) {
			// PLAYER is explicitly checked because its spawn group is MISC,
			// which would otherwise exclude it from the living-entity filter
			if (entity.getType() == EntityTypes.PLAYER || entity.getType().getCategory() != MobCategory.MISC) {
				entity.hurt(serverWorld.damageSources().cactus(), 2.0F);
			}
		}
	}

	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
		if (!world.isClientSide() && world instanceof ServerLevel serverWorld) {
			entity.causeFallDamage((float) fallDistance, 5.0F, serverWorld.damageSources().fall());
		}
		super.fallOn(world, state, pos, entity, fallDistance);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}
}
