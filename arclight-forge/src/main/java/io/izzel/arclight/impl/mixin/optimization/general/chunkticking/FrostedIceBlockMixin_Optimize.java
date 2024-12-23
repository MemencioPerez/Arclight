package io.izzel.arclight.impl.mixin.optimization.general.chunkticking;

import io.izzel.arclight.common.bridge.world.IBlockReaderBridge;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FrostedIceBlock.class)
public abstract class FrostedIceBlockMixin_Optimize {

    // @formatter:off
    @Shadow @Final public static IntegerProperty AGE;
    @Shadow protected abstract boolean slightlyMelt(BlockState var1, World var2, BlockPos var3);
    // @formatter:on

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if ((rand.nextInt(3) == 0 || this.shouldMelt(worldIn, pos, 4))
                && worldIn.getLight(pos) > 11 - (Integer) state.get(AGE) - state.getOpacity(worldIn, pos)
                && this.slightlyMelt(state, worldIn, pos)) {
            Mutable blockpos$mutable = new Mutable();

            for (Direction direction : Direction.values()) {
                blockpos$mutable.setAndMove(pos, direction);
                BlockState blockstate = ((IBlockReaderBridge) worldIn).getTypeIfLoaded(blockpos$mutable);
                if (blockstate != null && blockstate.matchesBlock((FrostedIceBlock) (Object) this) && !this.slightlyMelt(blockstate, worldIn, blockpos$mutable)) {
                    worldIn.getPendingBlockTicks().scheduleTick(blockpos$mutable, (FrostedIceBlock) (Object) this, MathHelper.nextInt(rand, 20, 40));
                }
            }
        } else {
            worldIn.getPendingBlockTicks().scheduleTick(pos, (FrostedIceBlock) (Object) this, MathHelper.nextInt(rand, 20, 40));
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private boolean shouldMelt(IBlockReader worldIn, BlockPos pos, int neighborsRequired) {
        int i = 0;
        Mutable blockpos$mutable = new Mutable();

        for (Direction direction : Direction.values()) {
            blockpos$mutable.setAndMove(pos, direction);
            BlockState blockState = ((IBlockReaderBridge) worldIn).getTypeIfLoaded(blockpos$mutable);
            if (blockState != null && blockState.matchesBlock((FrostedIceBlock) (Object) this)) {
                if (++i >= neighborsRequired) {
                    return false;
                }
            }
        }

        return true;
    }
}
