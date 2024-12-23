package io.izzel.arclight.common.mixin.core.world;

import io.izzel.arclight.common.bridge.world.IBlockReaderBridge;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

@Mixin(IBlockReader.class)
public interface IBlockReaderMixin extends IBlockReaderBridge {

    // @formatter:off
    @Shadow BlockState getBlockState(BlockPos pos);
    @Shadow FluidState getFluidState(BlockPos pos);
    @Shadow @Nullable BlockRayTraceResult rayTraceBlocks(Vector3d startVec, Vector3d endVec, BlockPos pos, VoxelShape shape, BlockState state);
    @Shadow static <T> T doRayTrace(RayTraceContext context, BiFunction<RayTraceContext, BlockPos, T> rayTracer, Function<RayTraceContext, T> missFactory) { return null; }
    // @formatter:on

    default BlockRayTraceResult rayTraceBlock(RayTraceContext context, BlockPos pos) {
        BlockState blockstate = this.getTypeIfLoaded(pos);
        if (blockstate == null) {
            Vector3d vector3d = context.getStartVec().subtract(context.getEndVec());
            return BlockRayTraceResult.createMiss(context.getEndVec(), Direction.getFacingFromVector(vector3d.x, vector3d.y, vector3d.z), new BlockPos(context.getEndVec()));
        } else {
            FluidState fluidstate = this.getFluidState(pos);
            Vector3d vector3d = context.getStartVec();
            Vector3d vector3d1 = context.getEndVec();
            VoxelShape voxelshape = context.getBlockShape(blockstate, (IBlockReader) this, pos);
            BlockRayTraceResult blockraytraceresult = this.rayTraceBlocks(vector3d, vector3d1, pos, voxelshape, blockstate);
            VoxelShape voxelshape1 = context.getFluidShape(fluidstate, (IBlockReader) this, pos);
            BlockRayTraceResult blockraytraceresult1 = voxelshape1.rayTrace(vector3d, vector3d1, pos);
            double d0 = blockraytraceresult == null ? Double.MAX_VALUE : context.getStartVec().squareDistanceTo(blockraytraceresult.getHitVec());
            double d1 = blockraytraceresult1 == null ? Double.MAX_VALUE : context.getStartVec().squareDistanceTo(blockraytraceresult1.getHitVec());
            return d0 <= d1 ? blockraytraceresult : blockraytraceresult1;
        }
    }

    @Override
    default BlockRayTraceResult bridge$rayTraceBlock(RayTraceContext context, BlockPos pos) {
        return rayTraceBlock(context, pos);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    default BlockRayTraceResult rayTraceBlocks(RayTraceContext context) {
        return doRayTrace(context, this::bridge$rayTraceBlock,p_217302_0_ -> {
                    Vector3d vector3d = p_217302_0_.getStartVec().subtract(p_217302_0_.getEndVec());
                    return BlockRayTraceResult.createMiss(p_217302_0_.getEndVec(), Direction.getFacingFromVector(vector3d.x, vector3d.y, vector3d.z), new BlockPos(p_217302_0_.getEndVec()));
                }
        );
    }
}
