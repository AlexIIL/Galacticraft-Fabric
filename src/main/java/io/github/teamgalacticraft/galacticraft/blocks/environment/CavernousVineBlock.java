package io.github.teamgalacticraft.galacticraft.blocks.environment;

import blue.endless.jankson.annotation.Nullable;
import io.github.teamgalacticraft.galacticraft.blocks.GalacticraftBlocks;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.sortme.ItemScatterer;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author <a href="https://github.com/teamgalacticraft">TeamGalacticraft</a>
 */
public class CavernousVineBlock extends Block implements Waterloggable {
    protected static final EnumProperty<VineTypes> VINES = EnumProperty.create("vinetype", VineTypes.class);
    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public CavernousVineBlock(Settings settings) {
        super(settings);
        settings.noCollision();
        this.setDefaultState(this.stateFactory.getDefaultState().with(WATERLOGGED, false).with(VINES, VineTypes.VINE_0));
    }

    @Override
    public void onBreak(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity) {
        super.onBreak(world, blockPos, blockState, playerEntity);

        if (playerEntity.getActiveItem().getItem() instanceof ShearsItem) {
            ItemScatterer.spawn(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(this.getItem(), 1));
        }
    }

    @Override
    public void onEntityCollision(BlockState blockState_1, World world_1, BlockPos blockPos_1, Entity entity) {
        if (!(entity instanceof LivingEntity) || (entity instanceof PlayerEntity && ((PlayerEntity) entity).abilities.flying)) {
            return;
        }

        onCollided((LivingEntity) entity);
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState_1) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public void onCollided(LivingEntity entity) {
        dragEntityUp(entity);
    }

    void dragEntityUp(LivingEntity entity) {
        entity.setVelocity(entity.getVelocity().x, 0.1D, entity.getVelocity().z);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        FluidState fluidState = context.getWorld().getFluidState(context.getBlockPos());
        return super.getPlacementState(context).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER).with(VINES, VineTypes.VINE_0);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockState, Direction direction, BlockState neighborBlockState, IWorld world, BlockPos blockPos, BlockPos neighborBlockPos) {
        if (blockState.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(blockPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(blockState, direction, neighborBlockState, world, blockPos, neighborBlockPos);
    }

    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
        builder.with(WATERLOGGED);
        builder.with(VINES);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.get(WATERLOGGED) ? Fluids.WATER.getState(false) : super.getFluidState(blockState);
    }

    @Override
    public boolean canPlaceAt(BlockState state, ViewableWorld viewableWorld, BlockPos pos) {
        BlockPos pos2 = pos;
        BlockPos pos3 = pos;
        pos2 = pos2.add(0, -1, 0);
        pos3 = pos3.add(0, 1, 0);
        //If it isn't on the ground and it is below a block
        return (!viewableWorld.getBlockState(pos3).getBlock().equals(Blocks.AIR))
                && (viewableWorld.getBlockState(pos2).getBlock().equals(Blocks.AIR)
                || viewableWorld.getBlockState(pos2).getBlock().equals(GalacticraftBlocks.CAVERNOUS_VINE_BLOCK)
                || viewableWorld.getBlockState(pos2).getBlock().equals(GalacticraftBlocks.POISONOUS_CAVERNOUS_VINE_BLOCK));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos pos_2, boolean boolean_1) {
        super.neighborUpdate(state, world, pos, block, pos_2, boolean_1);
        if (!canPlaceAt(state, world, pos)) {
            world.breakBlock(pos, false);
        }
    }

    @Override
    public void onScheduledTick(BlockState state, World world, BlockPos pos, Random random) {
        for (int y2 = pos.getY() - 1; y2 >= pos.getY() - 2; y2--) {

            BlockPos pos1 = new BlockPos(pos.getX(), y2, pos.getZ());
            BlockState blockState = world.getBlockState(pos1);

            if (!blockState.isAir()) {
                return;
            }
        }
        world.setBlockState(pos.down(), this.getStateFromMeta(getVineLength(world, pos)), 2);
        world.updateNeighbors(pos, state.getBlock());
    }

    private BlockState getStateFromMeta(int meta) {
        return GalacticraftBlocks.POISONOUS_CAVERNOUS_VINE_BLOCK.getDefaultState().with(VINES, VineTypes.byMetadata(meta));
    }

    private int getVineLength(World world, BlockPos pos) {
        int vineCount = 0;
        int y2 = pos.getY();

        while (world.getBlockState(new BlockPos(pos.getX(), y2, pos.getZ())).getBlock() == this) {
            vineCount++;
            y2++;
        }
        return vineCount;
    }

    public int getTickRate(ViewableWorld viewableWorld) {
        return 50;
    }

    @Override
    public void onPlaced(World world, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        BlockPos abovePos = new BlockPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
        BlockState stateAbove = world.getBlockState(abovePos);

        if (stateAbove.getBlock() == GalacticraftBlocks.CAVERNOUS_VINE_BLOCK || stateAbove.getBlock() == GalacticraftBlocks.POISONOUS_CAVERNOUS_VINE_BLOCK) {
            switch (stateAbove.get(VINES).getMeta()) {
                case 0:
                    world.setBlockState(blockPos, this.stateFactory.getDefaultState().with(WATERLOGGED, world.getBlockState(blockPos).getBlock() == Blocks.WATER).with(VINES, VineTypes.VINE_1));
                    break;
                case 1:
                    world.setBlockState(blockPos, this.stateFactory.getDefaultState().with(WATERLOGGED, world.getBlockState(blockPos).getBlock() == Blocks.WATER).with(VINES, VineTypes.VINE_2));
                    break;
                default:
                    world.setBlockState(blockPos, this.stateFactory.getDefaultState().with(WATERLOGGED, world.getBlockState(blockPos).getBlock() == Blocks.WATER).with(VINES, VineTypes.VINE_0));
                    break;
            }
        }
    }

    public enum VineTypes implements StringRepresentable {
        VINE_0("vine_0", 0),
        VINE_1("vine_1", 1),
        VINE_2("vine_2", 2);

        private final static VineTypes[] values = values();
        private String name;
        private int meta;

        VineTypes(String name, int meta) {
            this.name = name;
            this.meta = meta;
        }

        public static VineTypes byMetadata(int meta) {
            return values[meta % values.length];
        }

        public int getMeta() {
            return this.meta;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}
