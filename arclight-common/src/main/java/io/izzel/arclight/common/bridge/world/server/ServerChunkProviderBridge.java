package io.izzel.arclight.common.bridge.world.server;

import io.izzel.arclight.common.bridge.world.IWorldReaderBridge;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorldLightManager;

import java.io.IOException;

public interface ServerChunkProviderBridge extends IWorldReaderBridge {

    void bridge$close(boolean save) throws IOException;

    void bridge$purgeUnload();

    boolean bridge$tickDistanceManager();

    boolean bridge$isChunkLoaded(int x, int z);

    ServerWorldLightManager bridge$getLightManager();

    void bridge$setChunkGenerator(ChunkGenerator chunkGenerator);

    void bridge$setViewDistance(int viewDistance);
}
