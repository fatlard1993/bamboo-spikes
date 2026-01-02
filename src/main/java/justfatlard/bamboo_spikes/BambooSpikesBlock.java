package justfatlard.bamboo_spikes;

import eu.pb4.polymer.blocks.api.PolymerTexturedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.EnumMap;
import java.util.Map;

public class BambooSpikesBlock extends Block implements Waterloggable, PolymerTexturedBlock {
	public static final EnumProperty<Direction> FACING = FacingBlock.FACING;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	protected static final VoxelShape UP_SHAPE = Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 12.0D, 13.0D);
	protected static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(3.0D, 4.0D, 3.0D, 13.0D, 16.0D, 13.0D);
	protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(3.0D, 3.0D, 4.0D, 13.0D, 13.0D, 16.0D);
	protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(3.0D, 3.0D, 0.0D, 13.0D, 13.0D, 12.0D);
	protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0D, 3.0D, 3.0D, 12.0D, 13.0D, 13.0D);
	protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(4.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D);

	// Polymer block states for each facing direction
	private final Map<Direction, BlockState> polymerBlockStates = new EnumMap<>(Direction.class);

	public BambooSpikesBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState()
			.with(FACING, Direction.UP)
			.with(WATERLOGGED, false));
	}

	public void setPolymerBlockState(Direction facing, BlockState state) {
		this.polymerBlockStates.put(facing, state);
	}

	@Override
	public BlockState getPolymerBlockState(BlockState state, PacketContext context) {
		Direction facing = state.get(FACING);
		BlockState polymerState = this.polymerBlockStates.get(facing);
		return polymerState != null ? polymerState : state;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (state.get(WATERLOGGED)) {
			tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		BlockPos blockPos = context.getBlockPos();
		Direction direction = context.getSide();
		BlockState blockState = context.getWorld().getBlockState(blockPos.offset(direction.getOpposite()));
		boolean waterlogged = context.getWorld().getFluidState(blockPos).getFluid() == Fluids.WATER;

		Direction facing = blockState.getBlock() == this && blockState.get(FACING) == direction
			? direction.getOpposite()
			: direction;

		return this.getDefaultState()
			.with(FACING, facing)
			.with(WATERLOGGED, waterlogged);
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return switch (state.get(FACING)) {
			case DOWN -> DOWN_SHAPE;
			case NORTH -> NORTH_SHAPE;
			case SOUTH -> SOUTH_SHAPE;
			case WEST -> WEST_SHAPE;
			case EAST -> EAST_SHAPE;
			default -> UP_SHAPE;
		};
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		// Return empty collision so entities can walk into the spikes and trigger onEntityCollision
		return VoxelShapes.empty();
	}

	@Override
	protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler, boolean collidedWithFluid) {
		if (!world.isClient() && world instanceof ServerWorld serverWorld) {
			if (entity.getType() == EntityType.PLAYER || entity.getType().getSpawnGroup() != SpawnGroup.MISC) {
				entity.damage(serverWorld, world.getDamageSources().cactus(), 2.0F);
			}
		}
	}

	@Override
	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
		if (!world.isClient() && world instanceof ServerWorld serverWorld) {
			entity.handleFallDamage((float) fallDistance, 5.0F, serverWorld.getDamageSources().fall());
		}
		super.onLandedUpon(world, state, pos, entity, fallDistance);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}
}
