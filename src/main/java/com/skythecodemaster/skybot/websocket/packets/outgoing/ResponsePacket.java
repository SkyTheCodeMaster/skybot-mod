package com.skythecodemaster.skybot.websocket.packets.outgoing;

import com.skythecodemaster.skybot.websocket.packets.BasePacket;
import com.skythecodemaster.skybot.websocket.packets.Packet;

// This is a response packet
public class ResponsePacket extends BasePacket {
  private String data;
  private String type; // Whether this is a response to an info/command packet, or just a chat packet.
  private int id;
  
  public ResponsePacket() {
    this.setId(-1);
  }
  
  public ResponsePacket(Packet packet) {
    this.setId(packet.getId());
  }
  
  public ResponsePacket setData(String data) {
    this.data = data;
    return this;
  }
  
  public ResponsePacket setType(String type) {
    this.type = type;
    return this;
  }
  
  public ResponsePacket setId(int id) {
    this.id = id;
    return this;
  }
  
  public String getData() {
    return this.data;
  }
  
  public String getType() {
    return this.type;
  }
  
  public int getId() {
    return this.id;
  }
}
