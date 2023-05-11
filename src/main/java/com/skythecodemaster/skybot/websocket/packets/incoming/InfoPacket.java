package com.skythecodemaster.skybot.websocket.packets.incoming;

import com.skythecodemaster.skybot.websocket.packets.BasePacket;

public class InfoPacket extends BasePacket {
  private String data; // Current Values: "tps","players"
  // TPS will return an array of tps,mspt.
  // Players will return array of player usernames.
  
  public String getData() {
    return this.data;
  }
}
