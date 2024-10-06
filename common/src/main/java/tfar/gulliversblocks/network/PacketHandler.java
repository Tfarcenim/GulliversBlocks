package tfar.gulliversblocks.network;

import net.minecraft.resources.ResourceLocation;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.platform.Services;

import java.util.Locale;

public class PacketHandler {


    public static void registerPackets() {

        Services.PLATFORM.registerServerPlayPacket(C2SDropHeldEntityPacket.TYPE, C2SDropHeldEntityPacket.STREAM_CODEC);
        Services.PLATFORM.registerServerPlayPacket(C2SSwapHandsPacket.TYPE, C2SSwapHandsPacket.STREAM_CODEC);


        ///////server to client

        Services.PLATFORM.registerClientPlayPacket(S2CInitialEntityPacket.TYPE, S2CInitialEntityPacket.STREAM_CODEC);
    }

    public static ResourceLocation packet(Class<?> clazz) {
        return GulliversBlocks.id(clazz.getName().toLowerCase(Locale.ROOT));
    }
}
