/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * Decorative block representing a small gecko statue.
 *
 * <p>The block behaves as a horizontally oriented placed object with a custom collision and outline
 * shape for each facing direction. Its state is intentionally simple: only the horizontal facing is
 * persisted, and placement derives directly from the player's current look direction.
 */
public class GeckoStatueBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape NORTH_SHAPE = Block.box(4, 0, 4, 12, 6, 14);
    private static final VoxelShape SOUTH_SHAPE = Block.box(4, 0, 2, 12, 6, 12);
    private static final VoxelShape EAST_SHAPE  = Block.box(2, 0, 4, 12, 6, 12);
    private static final VoxelShape WEST_SHAPE  = Block.box(4, 0, 4, 14, 6, 12);

    /**
     * Creates a new statue block and initializes its default facing state.
     *
     * @param properties block property set used to configure hardness, sound, and related traits
     */
    public GeckoStatueBlock(Properties properties) {
        super(properties);

        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    /**
     * Adds the block state properties used by this block.
     *
     * @param builder mutable state-definition builder supplied by the block bootstrap pipeline
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    /**
     * Chooses the initial block state when the block is placed into the world.
     *
     * @param context placement context containing player direction and target position information
     * @return block state whose facing is opposite the player's horizontal facing
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    /**
     * Returns the shape used for collision and selection based on the current facing.
     *
     * @param state current block state
     * @param level level view requesting the shape
     * @param pos block position
     * @param context collision context describing the interacting entity, if any
     * @return facing-specific voxel shape representing the statue footprint
     */
    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> NORTH_SHAPE;
        };
    }
}
