package io.izzel.arclight.impl.mixin.optimization.general.chunkticking;

import io.izzel.arclight.common.bridge.world.IBlockReaderBridge;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.spawner.WorldEntitySpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({WorldEntitySpawner.class})
public class WorldEntitySpawnerMixin_Optimize {

    @Redirect(method = "Lnet/minecraft/world/spawner/WorldEntitySpawner;func_234966_a_(Lnet/minecraft/entity/EntityClassification;Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/chunk/IChunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/spawner/WorldEntitySpawner$IDensityCheck;Lnet/minecraft/world/spawner/WorldEntitySpawner$IOnSpawnDensityAdder;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/IChunk;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
    private static BlockState arclight$func_234966_a_$getBlockState(IChunk instance, BlockPos blockPos) {
        return ((IBlockReaderBridge) instance.getWorldForge()).getTypeIfLoaded(blockPos);
    }
}
