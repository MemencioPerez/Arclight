package io.izzel.arclight.impl.mixin.optimization.general.chunkticking;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import io.izzel.arclight.common.bridge.world.IBlockReaderBridge;
import it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
import it.unimi.dsi.fastutil.shorts.Short2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Plane;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FlowingFluid.class)
public abstract class FlowingFluidMixin_Optimize extends Fluid {

    // @formatter:off
    @Shadow private static short func_212752_a(BlockPos p_212752_0_, BlockPos p_212752_1_) {
        return 0;
    }
    @Shadow protected abstract FluidState calculateCorrectFlowingState(IWorldReader var1, BlockPos var2, BlockState var3);
    @Shadow protected abstract boolean func_211760_a(IBlockReader var1, Fluid var2, BlockPos var3, BlockState var4, Direction var5, BlockPos var6, BlockState var7, FluidState var8);
    @Shadow protected abstract boolean func_211759_a(IBlockReader var1, Fluid var2, BlockPos var3, BlockState var4, BlockPos var5, BlockState var6);
    @Shadow public abstract Fluid getFlowingFluid();
    @Shadow protected abstract boolean canSourcesMultiply();
    @Shadow protected abstract int getSlopeFindDistance(IWorldReader var1);
    // @formatter:on

    @Redirect(method = "Lnet/minecraft/fluid/FlowingFluid;func_207937_a(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/fluid/FluidState;Lnet/minecraft/block/BlockState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IWorld;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
    public BlockState arclight$getBlockState$func_207937_a(IWorld instance, BlockPos blockPos) {
        return ((IBlockReaderBridge) instance).getTypeIfLoaded(blockPos);
    }

    @Redirect(method = "Lnet/minecraft/fluid/FlowingFluid;calculateCorrectFlowingState(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/fluid/FluidState;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IWorldReader;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"))
    public BlockState arclight$getBlockState$calculateCorrectFlowingState(IWorldReader instance, BlockPos blockPos) {
        return ((IBlockReaderBridge) instance).getTypeIfLoaded(blockPos);
    }

    @Redirect(method = "Lnet/minecraft/fluid/FlowingFluid;calculateCorrectFlowingState(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/fluid/FluidState;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getFluidState()Lnet/minecraft/fluid/FluidState;"))
    public FluidState arclight$getFluidState$calculateCorrectFlowingState(BlockState instance) {
        return instance == null ? null : instance.getFluidState();
    }

    @Redirect(method = "Lnet/minecraft/fluid/FlowingFluid;calculateCorrectFlowingState(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/fluid/FluidState;", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;getFluid()Lnet/minecraft/fluid/Fluid;"))
    public Fluid arclight$getFluid$calculateCorrectFlowingState(FluidState instance) {
        return instance == null ? null : instance.getFluid();
    }

    @Redirect(method = "Lnet/minecraft/fluid/FlowingFluid;calculateCorrectFlowingState(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/fluid/FluidState;", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/Fluid;isEquivalentTo(Lnet/minecraft/fluid/Fluid;)Z"))
    public boolean arclight$isEquivalentTo$calculateCorrectFlowingState(Fluid instance, Fluid p_207187_1_) {
        return instance == null ? false : instance.isEquivalentTo(p_207187_1_);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected Map<Direction, FluidState> func_205572_b(IWorldReader p_205572_1_, BlockPos p_205572_2_, BlockState p_205572_3_) {
        int i = 1000;
        Map<Direction, FluidState> map = Maps.newEnumMap(Direction.class);
        Short2ObjectMap<Pair<BlockState, FluidState>> short2objectmap = new Short2ObjectOpenHashMap<>();
        Short2BooleanMap short2booleanmap = new Short2BooleanOpenHashMap();

        for (Direction direction : Plane.HORIZONTAL) {
            BlockPos blockpos = p_205572_2_.offset(direction);
            short short1 = func_212752_a(p_205572_2_, blockpos);
            Pair<BlockState, FluidState> pair = short2objectmap.get(short1);
            if (pair == null) {
                BlockState blockState = ((IBlockReaderBridge) p_205572_1_).getTypeIfLoaded(blockpos);
                if (blockState == null) {
                    continue;
                }

                pair = Pair.of(blockState, blockState.getFluidState());
                short2objectmap.put(short1, pair);
            }

            BlockState blockstate = pair.getFirst();
            FluidState fluidstate = pair.getSecond();
            FluidState fluidstate1 = this.calculateCorrectFlowingState(p_205572_1_, blockpos, blockstate);
            if (this.func_211760_a(p_205572_1_, fluidstate1.getFluid(), p_205572_2_, p_205572_3_, direction, blockpos, blockstate, fluidstate)) {
                BlockPos blockpos1 = blockpos.down();
                boolean flag = short2booleanmap.computeIfAbsent(short1, p_212753_5_ -> {
                    BlockState blockstate1 = p_205572_1_.getBlockState(blockpos1);
                    return this.func_211759_a(p_205572_1_, this.getFlowingFluid(), blockpos, blockstate, blockpos1, blockstate1);
                });
                int j;
                if (flag) {
                    j = 0;
                } else {
                    j = this.func_205571_a(p_205572_1_, blockpos, 1, direction.getOpposite(), blockstate, p_205572_2_, short2objectmap, short2booleanmap);
                }

                if (j < i) {
                    map.clear();
                }

                if (j <= i) {
                    map.put(direction, fluidstate1);
                    i = j;
                }
            }
        }

        return map;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected int func_205571_a(IWorldReader p_205571_1_, BlockPos p_205571_2_, int p_205571_3_, Direction p_205571_4_, BlockState p_205571_5_, BlockPos p_205571_6_, Short2ObjectMap<Pair<BlockState, FluidState>> p_205571_7_, Short2BooleanMap p_205571_8_) {
        int i = 1000;

        for (Direction direction : Plane.HORIZONTAL) {
            if (direction != p_205571_4_) {
                BlockPos blockpos = p_205571_2_.offset(direction);
                short short1 = func_212752_a(p_205571_6_, blockpos);
                Pair<BlockState, FluidState> pair = p_205571_7_.get(short1);
                if (pair == null) {
                    BlockState blockState = ((IBlockReaderBridge) p_205571_1_).getTypeIfLoaded(blockpos);
                    if (blockState == null) {
                        continue;
                    }

                    pair = Pair.of(blockState, blockState.getFluidState());
                    p_205571_7_.put(short1, pair);
                }

                BlockState blockstate = pair.getFirst();
                FluidState fluidstate = pair.getSecond();
                if (this.func_211760_a(p_205571_1_, this.getFlowingFluid(), p_205571_2_, p_205571_5_, direction, blockpos, blockstate, fluidstate)) {
                    boolean flag = p_205571_8_.computeIfAbsent(short1, p_212749_4_ -> {
                        BlockPos blockpos1 = blockpos.down();
                        BlockState blockstate1 = p_205571_1_.getBlockState(blockpos1);
                        return this.func_211759_a(p_205571_1_, this.getFlowingFluid(), blockpos, blockstate, blockpos1, blockstate1);
                    });
                    if (flag) {
                        return p_205571_3_;
                    }

                    if (p_205571_3_ < this.getSlopeFindDistance(p_205571_1_)) {
                        int j = this.func_205571_a(p_205571_1_, blockpos, p_205571_3_ + 1, direction.getOpposite(), blockstate, p_205571_6_, p_205571_7_, p_205571_8_);
                        if (j < i) {
                            i = j;
                        }
                    }
                }
            }
        }

        return i;
    }
}
