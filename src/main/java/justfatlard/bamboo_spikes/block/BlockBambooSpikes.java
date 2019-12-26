package justfatlard.bamboo_spikes.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlockBambooSpikes extends Block {
    private static final DirectionProperty FACING = FacingBlock.FACING;

    protected static final VoxelShape UP_SHAPE = Block.createCuboidShape(13.0D, 12.0D, 13.0D, 3.0D, 0.0D, 3.0D);
    protected static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(13.0D, 16.0D, 13.0D, 3.0D, 4.0D, 3.0D);

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(3.0D, 13.0D, 16.0D, 13.0D, 3.0D, 4.0D);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(13.0D, 13.0D, 0.0D, 3.0D, 3.0D, 12.0D);
 
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(12.0D, 3.0D, 13.0D, 0.0D, 13.0D, 3.0D);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(16.0D, 3.0D, 13.0D, 4.0D, 13.0D, 3.0D);

    public BlockBambooSpikes(Settings settings) {
        super(settings);

        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.UP)));
    }
    
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        Direction direction = context.getSide();
        BlockState blockState = context.getWorld().getBlockState(context.getBlockPos().offset(direction.getOpposite()));
        return blockState.getBlock() == this && blockState.get(FACING) == direction ? (BlockState)this.getDefaultState().with(FACING, direction.getOpposite()) : (BlockState)this.getDefaultState().with(FACING, direction);
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
        entity.damage(DamageSource.CACTUS, 2.0F);

        super.onEntityCollision(blockState, world, blockPosition, entity);
    }

    @Override
    public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance){
        entity.handleFallDamage(distance, 5.0F);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder){
        builder.add(FACING);
    }
}

