package tfar.gulliversblocks;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ModCommands {

    public static void dispatcher(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(GulliversBlocks.MOD_ID).executes(ModCommands::getInfo)
        );
    }

    private static int getInfo(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        source.sendSuccess(() -> Component.literal("Block reach: "+ player.blockInteractionRange()),false);
        return 1;
    }


}
