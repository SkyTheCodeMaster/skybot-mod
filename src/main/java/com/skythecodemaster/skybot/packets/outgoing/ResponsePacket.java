package com.skythecodemaster.skybot.packets.outgoing;

// This is a response packet
public class ResponsePacket extends BasePacket {
  private String data;
  private String type; // Whether this is a response to an info/command packet, or just a chat packet.
  
  public ResponsePacket setData(String data) {
    this.data = data;
    return this;
  }
  
  public ResponsePacket setType(String type) {
    this.type = type;
    return this;
  }
  
  public String getData() {
    return this.data;
  }
  
  public String getType() {
    return this.type;
  }
}
