package com.skythecodemaster.skybot.websocket.packets.incoming;

import com.skythecodemaster.skybot.websocket.packets.BasePacket;

public class CommandPacket extends BasePacket {
  private String command; // Minecraft command to execute
  private String[] args; // List of arguments to pass
  
  public String getCommand() {
    return this.command;
  }
  
  public String[] getArgs() {
    return this.args;
  }
  
  public String stringArgs() {
    StringBuilder output = new StringBuilder();
    for (String arg : this.args) {
      output.append(arg).append(" ");
    }
    return output.toString();
  }
  
  public String fullyQualifiedCommand() {
    return this.command + this.stringArgs();
  }
}
