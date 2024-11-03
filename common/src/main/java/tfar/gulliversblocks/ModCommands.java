package tfar.gulliversblocks;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class ModCommands {

    public static void dispatcher(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(GulliversBlocks.MOD_ID)
                .then(Commands.literal("debug").executes(ModCommands::getInfo)));
    }

    private static int getInfo(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        List<Component> components = createInfo(player);
        components.forEach(component -> source.sendSuccess(() -> component,false));
        return 1;
    }

    static List<Component> createInfo(LivingEntity entity) {
        List<Component> list = new ArrayList<>();
        list.add(Component.empty().append("Gulliver Stats for ").append(entity.getDisplayName()));
        int scale = LivingEntityDuck.of(entity).gulliversBlocks$getGulliverScale();
        list.add(Component.literal("Gulliver Scale: "+ scale));
        double absoluteScale = GulliverScales.SCALES.get(scale);
        list.add(Component.literal("Absolute Scale: "+absoluteScale));
        if (entity instanceof Player player) {
            list.add(Component.literal("Block reach: "+ player.blockInteractionRange()));
        }
        return list;
    }
}
