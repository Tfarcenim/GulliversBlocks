package tfar.gulliversblocks.client;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import tfar.gulliversblocks.GulliversBlocks;

public class ModKeybinds {

    public static final String CAT = GulliversBlocks.MOD_ID;
    public static final KeyMapping swap_shoulder = new KeyMapping("Swap Shoulder Entity",GLFW.GLFW_KEY_R, CAT);
}
