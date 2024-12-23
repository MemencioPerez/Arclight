package io.izzel.arclight.impl.mixin.optimization.general.chunkticking;

import io.izzel.arclight.common.bridge.world.IWorldReaderBridge;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.SectionPos;
import net.minecraft.world.IStructureReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraftforge.common.extensions.IForgeStructure;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Structure.class)
public abstract class StructureMixin_Optimize<C extends IFeatureConfig> extends ForgeRegistryEntry<Structure<?>> implements IForgeStructure {

    @Redirect(method = "Lnet/minecraft/world/gen/feature/structure/Structure;func_236388_a_(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/util/math/BlockPos;IZJLnet/minecraft/world/gen/settings/StructureSeparationSettings;)Lnet/minecraft/util/math/BlockPos;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/IWorldReader;getChunk(IILnet/minecraft/world/chunk/ChunkStatus;)Lnet/minecraft/world/chunk/IChunk;"))
    public IChunk arclight$func_236388_a_$getChunk(IWorldReader instance, int chunkX, int chunkZ, ChunkStatus p_217348_3_) {
        return ((IWorldReaderBridge) instance).getChunkIfLoadedImmediately(chunkX, chunkZ);
    }

    @Redirect(method = "Lnet/minecraft/world/gen/feature/structure/Structure;func_236388_a_(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/util/math/BlockPos;IZJLnet/minecraft/world/gen/settings/StructureSeparationSettings;)Lnet/minecraft/util/math/BlockPos;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/structure/StructureManager;getStructureStart(Lnet/minecraft/util/math/SectionPos;Lnet/minecraft/world/gen/feature/structure/Structure;Lnet/minecraft/world/IStructureReader;)Lnet/minecraft/world/gen/feature/structure/StructureStart;"))
    public StructureStart<?> arclight$func_236388_a_$getStructureStart(StructureManager instance, SectionPos sectionPos, Structure<?> structure, IStructureReader reader) {
        if (reader == null || sectionPos == null) {
            return null;
        } else {
            return instance.getStructureStart(sectionPos, (Structure<?>) (Object) this, reader);
        }
    }

    @Redirect(method = "Lnet/minecraft/world/gen/feature/structure/Structure;func_236388_a_(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/util/math/BlockPos;IZJLnet/minecraft/world/gen/settings/StructureSeparationSettings;)Lnet/minecraft/util/math/BlockPos;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/IChunk;getPos()Lnet/minecraft/util/math/ChunkPos;"))
    public ChunkPos arclight$func_236388_a_$getPos(IChunk instance) {
        if (instance == null) {
            return null;
        } else {
            return instance.getPos();
        }
    }

    @Redirect(method = "Lnet/minecraft/world/gen/feature/structure/Structure;func_236388_a_(Lnet/minecraft/world/IWorldReader;Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/util/math/BlockPos;IZJLnet/minecraft/world/gen/settings/StructureSeparationSettings;)Lnet/minecraft/util/math/BlockPos;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/SectionPos;from(Lnet/minecraft/util/math/ChunkPos;I)Lnet/minecraft/util/math/SectionPos;"))
    public SectionPos arclight$func_236388_a_$from(ChunkPos xz, int y) {
        if (xz == null) {
            return null;
        } else {
            return SectionPos.from(xz, y);
        }
    }
}
