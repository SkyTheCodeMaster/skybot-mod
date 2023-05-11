package com.skythecodemaster.skybot.packets.incoming;

import com.skythecodemaster.skybot.packets.BasePacket;

public class InfoPacket extends BasePacket {
  private String data; // Current Values: "tps","players"
  // TPS will return an array of tps,mspt.
  // Players will return array of player usernames.
  
  public String getData() {
    return this.data;
  }
}
