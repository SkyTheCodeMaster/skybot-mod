package com.skythecodemaster.skybot.websocket.packets;

public class Packet {
  private String type; // InfoPacket, ResponsePacket, CommandPacket, ChatPacket
  private int id;
  private String value; // The actual json string for each of these.
  
  public String getType() {
    return this.type;
  }
  
  public int getId() {
    return this.id;
  }
  
  public String getValue() {
    return this.value;
  }
}
