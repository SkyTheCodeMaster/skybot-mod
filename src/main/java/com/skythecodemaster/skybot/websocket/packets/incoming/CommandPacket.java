package com.skythecodemaster.skybot.websocket.packets.incoming;

import com.skythecodemaster.skybot.websocket.packets.BasePacket;

public class CommandPacket extends BasePacket {
  private String command; // Minecraft command to execute
  
  public String getCommand() {
    return this.command;
  }
}
