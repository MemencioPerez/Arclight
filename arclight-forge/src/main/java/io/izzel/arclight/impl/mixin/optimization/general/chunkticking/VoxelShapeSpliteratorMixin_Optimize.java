package io.izzel.arclight.impl.mixin.optimization.general.chunkticking;

import io.izzel.arclight.common.bridge.entity.EntityBridge;
import io.izzel.arclight.common.bridge.world.IBlockReaderBridge;

import java.util.Spliterators.AbstractSpliterator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.CubeCoordinateIterator;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapeSpliterator;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ICollisionReader;
import net.minecraft.world.Region;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VoxelShapeSpliterator.class)
public abstract class VoxelShapeSpliteratorMixin_Optimize extends AbstractSpliterator<VoxelShape> {

    // @formatter:off
    @Shadow @Final private CubeCoordinateIterator cubeCoordinateIterator;
    @Shadow @Final private Mutable mutablePos;
    @Shadow @Final private BiPredicate<BlockState, BlockPos> statePositionPredicate;
    @Shadow @Final private ICollisionReader reader;
    @Shadow @Final private ISelectionContext context;
    @Shadow @Final private VoxelShape shape;
    @Shadow @Final private AxisAlignedBB aabb;
    @Shadow @Final @Nullable private Entity entity;
    @Shadow @Nullable protected abstract IBlockReader func_234876_a_(int var1, int var2);
    // @formatter:on

    public VoxelShapeSpliteratorMixin_Optimize(long est, int additionalCharacteristics) {
        super(est, additionalCharacteristics);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    boolean func_234878_a_(Consumer<? super VoxelShape> p_234878_1_) {
        while (this.cubeCoordinateIterator.hasNext()) {
            int lvt_2_1_ = this.cubeCoordinateIterator.getX();
            int lvt_3_1_ = this.cubeCoordinateIterator.getY();
            int lvt_4_1_ = this.cubeCoordinateIterator.getZ();
            int lvt_5_1_ = this.cubeCoordinateIterator.numBoundariesTouched();
            if (lvt_5_1_ != 3) {
                Entity entity = this.entity;
                Mutable mutable = this.mutablePos;
                boolean far = entity != null && new BlockPos(entity.getPosX(),  lvt_3_1_, entity.getPosZ()).distanceSq( lvt_2_1_,  lvt_3_1_,  lvt_4_1_, false) > 14.0;
                mutable.setPos(lvt_2_1_, lvt_3_1_, lvt_4_1_);
                boolean isRegionLimited = this.reader instanceof Region;
                BlockState lvt_7_1_ = isRegionLimited ? Blocks.VOID_AIR.getDefaultState() : !far && entity instanceof PlayerEntity || entity != null && ((EntityBridge) entity).bridge$collisionCanLoadChunks() ? this.reader.getBlockState(this.mutablePos) : ((IBlockReaderBridge)this.reader).getTypeIfLoaded(this.mutablePos);
                if (lvt_7_1_ == null) {
                    if (!(entity instanceof PlayerEntity)) {
                        VoxelShape voxelShape = VoxelShapes.create(far ? entity.getBoundingBox() : new AxisAlignedBB(new BlockPos(lvt_2_1_, lvt_3_1_, lvt_4_1_)));
                        p_234878_1_.accept(voxelShape);
                        return true;
                    }
                } else if (this.statePositionPredicate.test(lvt_7_1_, this.mutablePos) && (lvt_5_1_ != 1 || lvt_7_1_.isCollisionShapeLargerThanFullBlock()) && (lvt_5_1_ != 2 || lvt_7_1_.matchesBlock(Blocks.MOVING_PISTON))) {
                    VoxelShape lvt_8_1_ = lvt_7_1_.getCollisionShape(this.reader, this.mutablePos, this.context);
                    if (lvt_8_1_ == VoxelShapes.fullCube()) {
                        if (this.aabb.intersects( lvt_2_1_,  lvt_3_1_,  lvt_4_1_,  lvt_2_1_ + 1.0,  lvt_3_1_ + 1.0,  lvt_4_1_ + 1.0)) {
                            p_234878_1_.accept(lvt_8_1_.withOffset( lvt_2_1_,  lvt_3_1_,  lvt_4_1_));
                            return true;
                        }
                    } else {
                        VoxelShape lvt_9_1_ = lvt_8_1_.withOffset( lvt_2_1_,  lvt_3_1_,  lvt_4_1_);
                        if (VoxelShapes.compare(lvt_9_1_, this.shape, IBooleanFunction.AND)) {
                            p_234878_1_.accept(lvt_9_1_);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
