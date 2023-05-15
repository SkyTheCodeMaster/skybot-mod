package com.skythecodemaster.skybot.websocket.packets;

public class BasePacket {
  private int id;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public int getId() {
    return this.id;
  }
}
