package com.skythecodemaster.skybot.packets;

public class Packet {
  private String type; // InfoPacket, ResponsePacket, CommandPacket, ChatPacket
  private String value; // The actual json string for each of these.
  
  public String getType() {
    return this.type;
  }
  
  public String getValue() {
    return this.value;
  }
}
