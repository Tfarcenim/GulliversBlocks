package tfar.gulliversblocks.platform;

import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.commons.lang3.tuple.Pair;
import tfar.gulliversblocks.DankPacketHandlerNeoForge;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.network.C2SModPacket;
import tfar.gulliversblocks.network.S2CModPacket;
import tfar.gulliversblocks.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public <F> void registerAll(Map<String, ? extends F> map, Registry<F> registry, Class<? extends F> filter) {
        List<Pair<ResourceLocation, Supplier<?>>> list = null;//GulliversBlocksNeoForge.registerLater.computeIfAbsent(registry, k -> new ArrayList<>());
        for (Map.Entry<String, ? extends F> entry : map.entrySet()) {
            list.add(Pair.of(GulliversBlocks.id(entry.getKey()), entry::getValue));
        }
    }

    public static PayloadRegistrar registrar;
    @Override
    public <MSG extends S2CModPacket<?>> void registerClientPlayPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf,MSG> streamCodec) {
        registrar.playToClient(type, streamCodec, (p, t) -> p.handleClient());
    }

    @Override
    public <MSG extends C2SModPacket<?>> void registerServerPlayPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec) {
        registrar.playToServer(type, streamCodec, (p, t) -> p.handleServer((ServerPlayer) t.player()));
    }


    @Override
    public void sendToClient(S2CModPacket<?> msg, ServerPlayer player) {
        DankPacketHandlerNeoForge.sendToClient(msg, player);
    }

    @Override
    public void sendToServer(C2SModPacket msg) {
        DankPacketHandlerNeoForge.sendToServer(msg);
    }

}