package com.github.upcraftlp.gmfix;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.impl.GameModeCommand;
import net.minecraft.world.GameType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimdev.rift.listener.CommandAdder;
import org.dimdev.rift.listener.MinecraftStartListener;

import java.util.Collections;

@SuppressWarnings("unused")
public class GamemodeFix implements MinecraftStartListener, CommandAdder {

    private static final Logger log = LogManager.getLogger("GamemodeFix");

    public static final String MODID = "gm_fix";

    public static Logger getLog() {
        return log;
    }

    @Override
    public void onMinecraftStart() {
        log.info("loaded Gamemode Fix!");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void registerCommands(CommandDispatcher<CommandSource> dispatcher) {
        ArgumentBuilder<CommandSource, ?> builder = dispatcher.getRoot().getChild("gamemode").createBuilder(); //get the /gamemode command node
        for(GameType type : GameType.values()) {
            if(type != GameType.NOT_SET) { //we don't want to be able to set the NOT_SET gamemode!
                builder
                        .then(Commands.literal(String.valueOf(type.ordinal() - 1)).executes(context -> GameModeCommand.func_198484_a(context, Collections.singleton(context.getSource().assertIsPlayer()), type))
                        .then(Commands.argument("target", EntityArgument.func_197094_d()).executes(source -> GameModeCommand.func_198484_a(source, EntityArgument.func_197090_e(source, "target"), type))));
            }
        }
        dispatcher.register((LiteralArgumentBuilder<CommandSource>) builder);
    }
}
