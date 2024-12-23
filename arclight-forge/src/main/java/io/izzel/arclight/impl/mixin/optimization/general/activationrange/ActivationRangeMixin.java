package io.izzel.arclight.impl.mixin.optimization.general.activationrange;

import io.izzel.arclight.common.bridge.entity.player.PlayerEntityBridge;
import io.izzel.arclight.common.bridge.world.IWorldReaderBridge;
import io.izzel.arclight.common.bridge.world.WorldBridge;
import io.izzel.arclight.impl.bridge.EntityBridge_ActivationRange;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.bukkit.craftbukkit.v.SpigotTimings;
import org.spigotmc.ActivationRange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ActivationRange.class, remap = false)
public class ActivationRangeMixin {

    // @formatter:off
    @Shadow private static AxisAlignedBB maxBB;
    // @formatter:on

    /**
     * @author IzzelAliz
     * @reason entityLists
     */
    @Overwrite
    private static void activateChunkEntities(Chunk chunk) {
        for (ClassInheritanceMultiMap<Entity> entityList : chunk.entityLists) {
            for (Entity entity : entityList) {
                ((EntityBridge_ActivationRange) entity).bridge$updateActivation();
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void activateEntities(World world) {
        SpigotTimings.entityActivationCheckTimer.startTiming();
        int miscActivationRange = ((WorldBridge) world).bridge$spigotConfig().miscActivationRange;
        int raiderActivationRange = ((WorldBridge) world).bridge$spigotConfig().raiderActivationRange;
        int animalActivationRange = ((WorldBridge) world).bridge$spigotConfig().animalActivationRange;
        int monsterActivationRange = ((WorldBridge) world).bridge$spigotConfig().monsterActivationRange;
        int maxRange = Math.max(monsterActivationRange, animalActivationRange);
        maxRange = Math.max(maxRange, raiderActivationRange);
        maxRange = Math.max(maxRange, miscActivationRange);
        maxRange = Math.min((((WorldBridge) world).bridge$spigotConfig().viewDistance << 4) - 8, maxRange);
        for (PlayerEntity player : world.getPlayers()) {
            ((PlayerEntityBridge)player).accessor$activatedTick(ServerLifecycleHooks.getCurrentServer().getTickCounter());
            maxBB = player.getBoundingBox().grow(maxRange, 256.0, maxRange);
            ActivationRange.ActivationType.MISC.boundingBox = player.getBoundingBox().grow(miscActivationRange, 256.0, miscActivationRange);
            ActivationRange.ActivationType.RAIDER.boundingBox = player.getBoundingBox().grow(raiderActivationRange, 256.0, raiderActivationRange);
            ActivationRange.ActivationType.ANIMAL.boundingBox = player.getBoundingBox().grow(animalActivationRange, 256.0, animalActivationRange);
            ActivationRange.ActivationType.MONSTER.boundingBox = player.getBoundingBox().grow(monsterActivationRange, 256.0, monsterActivationRange);
            int i = MathHelper.floor((ActivationRangeMixin.maxBB.minX / 16.0));
            int j = MathHelper.floor((ActivationRangeMixin.maxBB.maxX / 16.0));
            int k = MathHelper.floor((ActivationRangeMixin.maxBB.minZ / 16.0));
            int l = MathHelper.floor((ActivationRangeMixin.maxBB.maxZ / 16.0));
            for (int i1 = i; i1 <= j; ++i1) {
                for (int j1 = k; j1 <= l; ++j1) {
                    if (((IWorldReaderBridge)world).getChunkIfLoadedImmediately(i1, j1) == null) continue;
                    ActivationRangeMixin.activateChunkEntities(world.getChunk(i1, j1));
                }
            }
        }
        SpigotTimings.entityActivationCheckTimer.stopTiming();
    }
}
