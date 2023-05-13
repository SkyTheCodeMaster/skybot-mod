package com.skythecodemaster.skybot.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.skythecodemaster.skybot.commands.tpa.TPACommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandsRegister {
  @SubscribeEvent
  public void registerCommands(RegisterCommandsEvent event) {
    CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
    
    // Register each command
    TPACommand.register(dispatcher);
  }
}
