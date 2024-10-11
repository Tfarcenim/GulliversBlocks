package tfar.gulliversblocks.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.Minecraft;
import tfar.gulliversblocks.network.C2SSwapShoulderPacket;

public class ModClientFabric implements ClientModInitializer {




    @Override
    public void onInitializeClient() {
        //ModClient.setup();
        ClientTickEvents.START_CLIENT_TICK.register(ModClientFabric::keyPressed);
        KeyBindingHelper.registerKeyBinding(ModKeybinds.swap_shoulder);
    }

    public static void keyPressed(Minecraft client) {
        if (ModKeybinds.swap_shoulder.consumeClick()) {
            C2SSwapShoulderPacket.send();
        }
    }
}
