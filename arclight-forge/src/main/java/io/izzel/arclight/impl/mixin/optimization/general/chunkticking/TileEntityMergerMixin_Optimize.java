package io.izzel.arclight.impl.mixin.optimization.general.chunkticking;

import io.izzel.arclight.common.bridge.world.IBlockReaderBridge;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TileEntityMerger.class)
public class TileEntityMergerMixin_Optimize {

    @Redirect(method = "Lnet/minecraft/tileentity/TileEntityMerger;func_226924_a_(Lnet/minecraft/tileentity/TileEntityType;Ljava/util/function/Function;Ljava/util/function/Function;Lnet/minecraft/state/DirectionProperty;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Ljava/util/function/BiPredicate;)Lnet/minecraft/tileentity/TileEntityMerger$ICallbackWrapper;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IWorld;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
    private static BlockState getBlockState(IWorld instance, BlockPos blockPos) {
        return ((IBlockReaderBridge) instance).getTypeIfLoaded(blockPos);
    }

    @Redirect(method = "Lnet/minecraft/tileentity/TileEntityMerger;func_226924_a_(Lnet/minecraft/tileentity/TileEntityType;Ljava/util/function/Function;Ljava/util/function/Function;Lnet/minecraft/state/DirectionProperty;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Ljava/util/function/BiPredicate;)Lnet/minecraft/tileentity/TileEntityMerger$ICallbackWrapper;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;matchesBlock(Lnet/minecraft/block/Block;)Z"))
    private static boolean getBlockState(BlockState instance, Block block) {
        return instance != null && instance.matchesBlock(block);
    }
}
