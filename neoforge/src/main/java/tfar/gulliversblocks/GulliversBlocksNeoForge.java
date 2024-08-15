package tfar.gulliversblocks;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(GulliversBlocks.MOD_ID)
public class GulliversBlocksNeoForge {

    public GulliversBlocksNeoForge(IEventBus eventBus) {
        eventBus.addListener(this::register);
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        GulliversBlocks.init();
    }

    private void register(RegisterEvent event) {
        GulliversBlocks.register();
    }

}