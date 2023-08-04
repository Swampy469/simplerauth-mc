package com.dqu.simplerauth.commands;

import com.dqu.simplerauth.AuthMod;
import com.dqu.simplerauth.managers.DbManager;
import com.dqu.simplerauth.PlayerObject;
import com.dqu.simplerauth.managers.LangManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.literal;
public class LogoutCommand {

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("logout").executes(LogoutCommand::logout)
        );
    }

    private static int logout(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        PlayerObject playerObject = AuthMod.playerManager.get(player);

        try {
            playerObject.deauthenticate();

            String playerName = player.getEntityName();

            //Remove last session
            DbManager.sessionDestroy(playerName);

            //Disconnect player
            player.networkHandler.disconnect(LangManager.getLiteralText("command.logout.disconnected"));
            return 1;
        } catch(Exception e) {
            return 0;
        }

    }

}
