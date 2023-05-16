package com.skythecodemaster.skybot.websocket.packets;

public class BasePacket {
  private int id;
  
  public BasePacket setId(int id) {
    this.id = id;
    return this;
  }
  
  public int getId() {
    return this.id;
  }
}
