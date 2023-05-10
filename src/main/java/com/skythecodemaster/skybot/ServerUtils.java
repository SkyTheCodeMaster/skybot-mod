package com.skythecodemaster.skybot;

import com.google.gson.Gson;
import com.skythecodemaster.skybot.packets.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;

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
    try {
      // Grab the playerlist
      PlayerList players = ServerLifecycleHooks.getCurrentServer().getPlayerList();
  
      HoverEvent hvrEvent = new HoverEvent(
        HoverEvent.Action.SHOW_TEXT,
        Component.literal(packet.getDate())
      );
      Style style = Style.EMPTY;
      style.withHoverEvent(hvrEvent);
  
      String formatted = String.format(
        "<§3D§r> <§%s%s§r> %s",
        packet.getColour(),
        packet.getSender(),
        packet.getMessage()
      );
  
      MutableComponent message = Component.literal(formatted)
        .setStyle(style);
  
      players.broadcastSystemMessage(
        message,
        true
      );
  
      // Because we do not need to return anything of importance, just return a packet
      // with 'OK' as its response.
      return new ResponsePacket()
        .setType("chat")
        .setData("OK");
    } catch (Exception e) {
      // Something went wrong, report it.
      return new ResponsePacket()
        .setType("chat")
        .setData("ERROR " + e.getMessage());
    }
  }
}
