package io.izzel.arclight.impl.mixin.optimization.general.chunkticking;

import io.izzel.arclight.common.bridge.world.IBlockReaderBridge;

import java.util.stream.Stream;

import net.minecraft.block.BlockState;
import net.minecraft.util.AxisRotation;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IWorldReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(VoxelShapes.class)
public class VoxelShapesMixin_Optimize {

    @Inject(method = "Lnet/minecraft/util/math/shapes/VoxelShapes;getAllowedOffset(Lnet/minecraft/util/math/AxisAlignedBB;Lnet/minecraft/world/IWorldReader;DLnet/minecraft/util/math/shapes/ISelectionContext;Lnet/minecraft/util/AxisRotation;Ljava/util/stream/Stream;)D", at = {@At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isCollisionShapeLargerThanFullBlock()Z", shift = Shift.BY, by = -2)}, locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void arclight$getAllowedOffset$getBlockState(AxisAlignedBB p_216386_0_, IWorldReader p_216386_1_, double p_216386_2_, ISelectionContext p_216386_4_, AxisRotation p_216386_5_, Stream<VoxelShape> p_216386_6_, CallbackInfoReturnable<Double> cir, AxisRotation lvt_7_1_, Axis lvt_8_1_, Axis lvt_9_1_, Axis lvt_10_1_, Mutable lvt_11_1_, int lvt_12_1_, int lvt_13_1_, int lvt_14_1_, int lvt_15_1_, double lvt_16_1_, double lvt_18_1_, boolean lvt_20_1_, int lvt_21_1_, int lvt_22_1_, int lvt_23_1_, int lvt_24_1_, int lvt_25_1_, int lvt_26_1_, int lvt_27_1_, BlockState lvt_28_1_) {
        if (lvt_28_1_ == null) {
            cir.setReturnValue(0.0);
        }
    }

    @Redirect(method = "Lnet/minecraft/util/math/shapes/VoxelShapes;getAllowedOffset(Lnet/minecraft/util/math/AxisAlignedBB;Lnet/minecraft/world/IWorldReader;DLnet/minecraft/util/math/shapes/ISelectionContext;Lnet/minecraft/util/AxisRotation;Ljava/util/stream/Stream;)D", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IWorldReader;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
    private static BlockState arclight$getAllowedOffset$getBlockState(IWorldReader instance, BlockPos blockPos) {
        return ((IBlockReaderBridge) instance).getTypeIfLoaded(blockPos);
    }
}
