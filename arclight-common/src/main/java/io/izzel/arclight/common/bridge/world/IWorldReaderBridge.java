package io.izzel.arclight.common.bridge.world;

import net.minecraft.world.chunk.IChunk;

public interface IWorldReaderBridge {

    IChunk getChunkIfLoadedImmediately(int x, int z);
}
