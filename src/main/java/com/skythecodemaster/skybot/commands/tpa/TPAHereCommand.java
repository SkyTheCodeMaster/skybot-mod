package com.skythecodemaster.skybot.commands.tpa;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.time.Instant;

public class TPAHereCommand implements Command<CommandSourceStack> {
  private static final TPAHereCommand CMD = new TPAHereCommand();
  
  public static LiteralArgumentBuilder<CommandSourceStack> register() {
    return Commands.literal("tpahere")
      .requires(cs -> cs.hasPermission(0))
      .then(Commands.argument("other", EntityArgument.players())
      .executes(CMD));
  }
  
  @Override
  public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
    ServerPlayer sourcePlayer = context.getSource().getPlayer();
    EntitySelector targets = context.getArgument("other", EntitySelector.class);
    ServerPlayer targetPlayer = targets.findSinglePlayer(context.getSource());
    /*if (sourcePlayer == null || targetPlayer == null) {
      throw new CommandSyntaxException(
        CommandExceptionType.class,
        "Invalid player!"
      );
    }*/
    
    // Create the request
    long expiryTime = Instant.now().getEpochSecond() + 120;
    assert sourcePlayer != null;
    Request request = new Request(
      sourcePlayer.getUUID(),
      targetPlayer.getUUID(),
      expiryTime,
      false
    );
    // Insert it into data storage
    TPACommand.requests.put(
      targetPlayer.getUUID(),
      request
    );
    
    MutableComponent comp = Component.empty()
      .withStyle(ChatFormatting.WHITE)
      .append(Component.literal("<"))
      .append(
        Component.literal(
          sourcePlayer
            .getName()
            .getString()
        ).withStyle(
          ChatFormatting.GOLD
        )
      )
      .append(Component.literal(">"))
      .append(Component.literal(" would like you to teleport to them..\n"))
      .append(Component.literal("Type "))
      .append(Component.literal("/tpaccept").withStyle(ChatFormatting.GRAY))
      .append(Component.literal(" to accept."));
    
    targetPlayer.sendSystemMessage(comp);
    
    return 1;
  }
}
