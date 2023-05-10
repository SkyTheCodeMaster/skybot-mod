package com.skythecodemaster.skybot.packets;

public class InfoPacket extends BasePacket {
  private String data; // Current Values: "tps","players"
  // TPS will return '20.0|50.0'.
  // Players will return array of player usernames.
}
