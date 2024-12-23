package io.izzel.arclight.common.bridge.entity.player;

import com.mojang.datafixers.util.Either;
import io.izzel.arclight.common.bridge.entity.LivingEntityBridge;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import org.bukkit.craftbukkit.v.entity.CraftHumanEntity;

public interface PlayerEntityBridge extends LivingEntityBridge {

    boolean bridge$isFauxSleeping();

    int bridge$activatedTick();

    void accessor$activatedTick(int activatedTick);

    @Override
    CraftHumanEntity bridge$getBukkitEntity();

    Either<PlayerEntity.SleepResult, Unit> bridge$trySleep(BlockPos at, boolean force);
}
