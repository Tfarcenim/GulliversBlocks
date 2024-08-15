package tfar.gulliversblocks;

import net.fabricmc.api.ModInitializer;

public class GulliversBlocksFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        GulliversBlocks.register();
        // Use Fabric to bootstrap the Common mod.
        GulliversBlocks.init();
    }
}
