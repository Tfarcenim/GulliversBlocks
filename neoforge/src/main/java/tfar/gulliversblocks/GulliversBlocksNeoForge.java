package tfar.gulliversblocks;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.RegisterEvent;
import tfar.gulliversblocks.config.GulliversBlocksConfig;
import tfar.gulliversblocks.datagen.ModDatagen;

@Mod(GulliversBlocks.MOD_ID)
public class GulliversBlocksNeoForge {

    public GulliversBlocksNeoForge(IEventBus eventBus, ModContainer modContainer) {
        eventBus.addListener(this::register);
        eventBus.addListener(ModDatagen::gather);
        eventBus.addListener(PacketHandlerNeoForge::register);
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.
        modContainer.registerConfig(ModConfig.Type.SERVER, GulliversBlocksConfig.SERVER_SPEC);
        // Use NeoForge to bootstrap the Common mod.
        GulliversBlocks.init();
    }

    private void register(RegisterEvent event) {
        GulliversBlocks.register();
    }

}