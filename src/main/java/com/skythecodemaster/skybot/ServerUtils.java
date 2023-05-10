package com.skythecodemaster.skybot;

import com.google.gson.Gson;
import com.skythecodemaster.skybot.packets.*;

public class ServerUtils {
  // Provide methods for taking a data packet and returning a new one with the response.
  Gson gson = new Gson(); // Use Gson for parse/stringify
  
  public BasePacket parsePacket(String jsonData) {
    // Parse the top level json
    Packet packet = gson.fromJson(jsonData, Packet.class);
    // Figure out what class to parse the resulting json from.
    return switch (packet.getType()) {
      case "CommandPacket" -> gson.fromJson(packet.getValue(), CommandPacket.class);
      case "InfoPacket"    -> gson.fromJson(packet.getValue(), InfoPacket.class);
      case "ChatPacket"    -> gson.fromJson(packet.getValue(), ChatPacket.class);
      default -> throw new IllegalArgumentException("Sent packet contains invalid type!");
    };
  }
  
  // Now provide methods for actually using the packet. Set these up as overloads so it's
  // simple.
  
  public ResponsePacket executePacket(ChatPacket packet) {
  
  }
}
