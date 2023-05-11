package com.skythecodemaster.skybot.websocket.utils;

import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;

public class CommandReceiver implements CommandSource {
  private final ArrayList<String> output = new ArrayList<>();
  
  public String getLastOutput() {
    return output.get(output.size()-1);
  }
  
  @Override
  public void sendSystemMessage(Component pComponent) {
    output.add(pComponent.getString());
  }
  
  @Override
  public boolean acceptsSuccess() {
    return true;
  }
  
  @Override
  public boolean acceptsFailure() {
    return true;
  }
  
  @Override
  public boolean shouldInformAdmins() {
    return false;
  }
}
