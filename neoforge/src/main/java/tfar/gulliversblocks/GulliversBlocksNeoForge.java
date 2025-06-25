package tfar.gulliversblocks;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import tfar.gulliversblocks.config.GulliversBlocksConfig;
import tfar.gulliversblocks.datagen.ModDatagen;

@Mod(GulliversBlocks.MOD_ID)
public class GulliversBlocksNeoForge {

    public GulliversBlocksNeoForge(IEventBus eventBus, ModContainer modContainer) {
        eventBus.addListener(this::register);
        eventBus.addListener(ModDatagen::gather);
        eventBus.addListener(PacketHandlerNeoForge::register);

        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> ModCommands.dispatcher(event.getDispatcher()));

        NeoForge.EVENT_BUS.addListener((PlayerEvent.Clone event) -> GulliversBlocks.copyFrom((ServerPlayer) event.getOriginal(), (ServerPlayer) event.getEntity(),!event.isWasDeath()));
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.
        modContainer.registerConfig(ModConfig.Type.SERVER, GulliversBlocksConfig.SERVER_SPEC);
        // Use NeoForge to bootstrap the Common mod.
        GulliversBlocks.init();
        NeoForge.EVENT_BUS.addListener(this::entityInteract);
    }

    void entityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        InteractionResult interactionResult = GulliversBlocks.entityInteract(event.getEntity(), event.getLevel(), event.getHand(), event.getTarget());
        if (interactionResult != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(interactionResult);
        }
    }

    private void register(RegisterEvent event) {
        if (event.getRegistry() == BuiltInRegistries.BLOCK) {
            GulliversBlocks.register();
        }
    }

}