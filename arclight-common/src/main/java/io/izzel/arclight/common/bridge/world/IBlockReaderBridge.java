package io.izzel.arclight.common.bridge.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;

public interface IBlockReaderBridge {

    BlockRayTraceResult bridge$rayTraceBlock(RayTraceContext context, BlockPos pos);

    BlockState getTypeIfLoaded(BlockPos blockPos);

    FluidState getFluidIfLoaded(BlockPos blockPos);

    default Block getBlockIfLoaded(BlockPos blockPos) {
        BlockState typeIfLoaded = this.getTypeIfLoaded(blockPos);
        return typeIfLoaded == null ? null : typeIfLoaded.getBlock();
    }

    default Material getMaterialIfLoaded(BlockPos blockPos) {
        BlockState typeIfLoaded = this.getTypeIfLoaded(blockPos);
        return typeIfLoaded == null ? null : typeIfLoaded.getMaterial();
    }
}
