package io.izzel.arclight.common.mixin.core.world;

import io.izzel.arclight.common.bridge.world.IBlockReaderBridge;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ICollisionReader;
import net.minecraft.world.Region;
import net.minecraft.world.chunk.IChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Region.class)
public abstract class RegionMixin implements IBlockReader, ICollisionReader, IBlockReaderBridge {

    // @formatter:off
    @Shadow @Final protected int chunkX;
    @Shadow @Final protected int chunkZ;
    @Shadow @Final protected IChunk[][] chunks;
    // @formatter:on

    private IChunk getChunkIfLoaded(int x, int z) {
        int k = x - this.chunkX;
        int l = z - this.chunkZ;
        if (k >= 0 && k < this.chunks.length && l >= 0 && l < this.chunks[k].length) {
            return this.chunks[k][l];
        } else {
            return null;
        }
    }

    @Override
    public BlockState getTypeIfLoaded(BlockPos blockPos) {
        IChunk chunk = this.getChunkIfLoaded(blockPos.getX() >> 4, blockPos.getZ() >> 4);
        return chunk == null ? null : chunk.getBlockState(blockPos);
    }

    @Override
    public FluidState getFluidIfLoaded(BlockPos blockPos) {
        IChunk chunk = this.getChunkIfLoaded(blockPos.getX() >> 4, blockPos.getZ() >> 4);
        return chunk == null ? null : chunk.getFluidState(blockPos);
    }
}
