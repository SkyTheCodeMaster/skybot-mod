package com.skythecodemaster.skybot.commands.tpa;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.time.Instant;
import java.util.HashMap;

public class TPAAcceptCommand implements Command<CommandSourceStack> {
  private static final TPAAcceptCommand CMD = new TPAAcceptCommand();
  
  public static LiteralArgumentBuilder<CommandSourceStack> register() {
    return Commands.literal("tpaccept")
      .requires(cs -> cs.hasPermission(0))
      .executes(CMD);
  }
  
  @Override
  public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
    ServerPlayer targetPlayer = context.getSource().getPlayer();
  
    // See if we can grab a Request object from the TPACommand
    assert targetPlayer != null;
    Request request = TPACommand.requests.get(targetPlayer.getUUID());
  
    if (request == null) {
      return 0;
    } else if (request.getExpiry() >= Instant.now().getEpochSecond()) {
      MutableComponent comp = Component.literal("This teleport request has expired.")
        .withStyle(ChatFormatting.RED);
      
      context.getSource().getPlayer().sendSystemMessage(comp);
      return 0;
    }
  
    ServerPlayer sourcePlayer = null;
    if (request.getDirection()) {
      // tpahere
      sourcePlayer = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(request.getSource());
    } else {
      sourcePlayer = targetPlayer;
      targetPlayer = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(request.getSource());
    }
    assert sourcePlayer != null;
    Vec3 sourcePos = sourcePlayer.getPosition(0);
    Vec2 sourceRot = sourcePlayer.getRotationVector();
  
    assert targetPlayer != null;
    targetPlayer.teleportTo(sourcePlayer.getLevel(), sourcePos.x, sourcePos.y, sourcePos.z, sourceRot.x, sourceRot.y);
  
    MutableComponent comp = Component.empty()
      .withStyle(ChatFormatting.WHITE)
      .append(Component.literal("["))
      .append(Component.literal("TPA").withStyle(ChatFormatting.GREEN))
      .append(Component.literal("] Teleport request accepted."));
  
    context.getSource().getPlayer().sendSystemMessage(comp);
    return 1;
  }
}
