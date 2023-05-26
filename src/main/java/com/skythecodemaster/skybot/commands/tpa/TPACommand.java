package com.skythecodemaster.skybot.commands.tpa;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.arguments.selector.EntitySelector;
import org.slf4j.Logger;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

public class TPACommand implements Command<CommandSourceStack> {
  private static final TPACommand CMD = new TPACommand();
  
  private static final Logger LOGGER = LogUtils.getLogger();
  // target, source
  public static final HashMap<UUID, Request> requests = new HashMap<>();
  
  public static LiteralArgumentBuilder<CommandSourceStack> register() {
    return Commands.literal("tpa")
      .requires(cs -> cs.hasPermission(0))
      .then(Commands.argument("other", EntityArgument.players())
      .executes(CMD));
  }
  
  @Override
  public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
    try {
      ServerPlayer sourcePlayer = context.getSource().getPlayer();
      EntitySelector targets = context.getArgument("other", EntitySelector.class);
      ServerPlayer targetPlayer = targets.findSinglePlayer(context.getSource());
  
  
      // Create the request
      long expiryTime = Instant.now().getEpochSecond() + 120;
      assert sourcePlayer != null;
      Request request = new Request(
        sourcePlayer.getUUID(),
        targetPlayer.getUUID(),
        expiryTime,
        true
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
        .append(Component.literal(" would like to teleport to you.\n"))
        .append(Component.literal("Type "))
        .append(Component.literal("/tpaccept").withStyle(ChatFormatting.GRAY))
        .append(Component.literal(" to accept."));
  
      targetPlayer.sendSystemMessage(comp);
  
      return 1;
    } catch (Exception e) {
      LOGGER.info("TPA Error: " + e.getMessage());
      return 0;
    }
  }
}
