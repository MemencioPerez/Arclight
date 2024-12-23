package io.izzel.arclight.common.mod;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import io.izzel.arclight.api.ArclightVersion;
import io.izzel.arclight.common.mod.server.ArclightPermissionHandler;
import io.izzel.arclight.common.mod.server.event.ArclightEventDispatcherRegistry;
import io.izzel.arclight.common.mod.util.log.ArclightI18nLogger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;

@Mod("arclight")
public class ArclightMod {

    public static final Logger LOGGER = ArclightI18nLogger.getLogger("Arclight");
    private long start;

    public ArclightMod() {
        LOGGER.info("mod-load");
        ArclightVersion.setVersion(ArclightVersion.v1_16_4);
        ArclightEventDispatcherRegistry.registerAllEventDispatchers();
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        PermissionAPI.setPermissionHandler(ArclightPermissionHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            this.start = System.nanoTime();
            Bukkit.getPluginManager().callEvent(new ServerTickStartEvent(ServerLifecycleHooks.getCurrentServer().getTickCounter() + 1));
        }
        if (event.phase == TickEvent.Phase.END) {
            long endTime = System.nanoTime();
            Bukkit.getPluginManager().callEvent(new ServerTickEndEvent(ServerLifecycleHooks.getCurrentServer().getTickCounter(), (double)(endTime - this.start) / 1000000.0, 0L));
        }
    }
}
