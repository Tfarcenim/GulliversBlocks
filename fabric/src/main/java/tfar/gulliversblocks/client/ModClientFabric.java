package tfar.gulliversblocks.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

public class ModClientFabric implements ClientModInitializer {




    @Override
    public void onInitializeClient() {
        //ModClient.setup();
        ClientTickEvents.START_CLIENT_TICK.register(ModClient::keyPressed);
        KeyBindingHelper.registerKeyBinding(ModKeybinds.swap_shoulder);
    }

}
