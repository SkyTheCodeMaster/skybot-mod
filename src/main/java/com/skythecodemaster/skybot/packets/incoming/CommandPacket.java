package com.skythecodemaster.skybot.packets.incoming;

import com.skythecodemaster.skybot.packets.BasePacket;

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
    String output = "";
    for (String arg : this.args) {
      output += arg + " ";
    }
    return output;
  }
  
  public String fullyQualifiedCommand() {
    return this.command + this.stringArgs();
  }
}
