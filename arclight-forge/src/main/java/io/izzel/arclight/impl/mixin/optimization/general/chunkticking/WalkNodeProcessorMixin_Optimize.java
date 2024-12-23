package io.izzel.arclight.impl.mixin.optimization.general.chunkticking;

import io.izzel.arclight.common.bridge.world.IBlockReaderBridge;
import net.minecraft.block.BlockState;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WalkNodeProcessor.class)
public abstract class WalkNodeProcessorMixin_Optimize extends NodeProcessor {

    // @formatter:off
    @Shadow private static boolean isFiery(BlockState state) {
        return false;
    }
    // @formatter:on

    @Redirect(method = "Lnet/minecraft/pathfinding/WalkNodeProcessor;getSurroundingDanger(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos$Mutable;Lnet/minecraft/pathfinding/PathNodeType;)Lnet/minecraft/pathfinding/PathNodeType;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockReader;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
    private static BlockState arclight$getBlockState$getSurroundingDanger(IBlockReader instance, BlockPos blockPos) {
        return ((IBlockReaderBridge) instance).getTypeIfLoaded(blockPos);
    }

    @Inject(method = "Lnet/minecraft/pathfinding/WalkNodeProcessor;getSurroundingDanger(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos$Mutable;Lnet/minecraft/pathfinding/PathNodeType;)Lnet/minecraft/pathfinding/PathNodeType;", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/IBlockReader;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    @SuppressWarnings("all")
    private static void onGetBlockState(IBlockReader p_237232_0_, BlockPos.Mutable p_237232_1_, PathNodeType p_237232_2_, CallbackInfoReturnable<PathNodeType> cir, int i, int j, int k, int l, int i1, int j1, BlockState blockState) {
        if (blockState == null) {
            cir.setReturnValue(PathNodeType.BLOCKED);
        }
    }

    @Redirect(method = "Lnet/minecraft/pathfinding/WalkNodeProcessor;func_237238_b_(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/pathfinding/PathNodeType;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IBlockReader;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
    private static BlockState arclight$getBlockState$func_237238_b_(IBlockReader instance, BlockPos blockPos) {
        return ((IBlockReaderBridge) instance).getTypeIfLoaded(blockPos);
    }

    @Inject(method = "Lnet/minecraft/pathfinding/WalkNodeProcessor;func_237238_b_(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/pathfinding/PathNodeType;", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/IBlockReader;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    @SuppressWarnings("all")
    private static void onGetBlockState(IBlockReader p_237238_0_, BlockPos p_237238_1_, CallbackInfoReturnable<PathNodeType> cir, BlockState blockState) {
        if (blockState == null) {
            cir.setReturnValue(PathNodeType.BLOCKED);
        }
    }
}
