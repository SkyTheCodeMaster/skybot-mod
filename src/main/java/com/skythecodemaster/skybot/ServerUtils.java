package com.skythecodemaster.skybot;

import com.google.gson.Gson;
import com.skythecodemaster.skybot.packets.*;
import com.skythecodemaster.skybot.packets.incoming.ChatPacket;
import com.skythecodemaster.skybot.packets.incoming.CommandPacket;
import com.skythecodemaster.skybot.packets.incoming.InfoPacket;
import com.skythecodemaster.skybot.packets.outgoing.ResponsePacket;
import com.skythecodemaster.skybot.utils.CommandReceiver;
import com.skythecodemaster.skybot.utils.TickTimes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerUtils {
  // Provide methods for taking a data packet and returning a new one with the response.
  private final Gson gson = new Gson(); // Use Gson for parse/stringify
  private final CommandReceiver receiver = new CommandReceiver();
  
  // Get the command source.
  private CommandSourceStack getSource() {
    String name = "skybot";
    return new CommandSourceStack(
      receiver,
      Vec3.ZERO,
      Vec2.ZERO,
      ServerLifecycleHooks.getCurrentServer().overworld(),
      2,
      name,
      Component.literal(name),
      ServerLifecycleHooks.getCurrentServer(),
      null
    );
  }
  
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
      // Grab the player list
      PlayerList players = ServerLifecycleHooks
        .getCurrentServer()
        .getPlayerList();
  
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
  
      // Because we do not need to return anything of importance, just return a
      // packet with 'OK' as its response.
      return new ResponsePacket()
        .setType("chat")
        .setData("OK");
    } catch (Exception e) {
      // Something went wrong, report it.
      return new ResponsePacket()
        .setType("error_chat")
        .setData("ERROR " + e.getMessage());
    }
  }
  
  public ResponsePacket executePacket(InfoPacket packet) {
    try {
      ServerData sData = new ServerData();
  
      switch (packet.getData()) {
        case "tps" -> {
          TickTimes times = sData.getTickTimes();
          String data = Arrays.toString(times.toArray());
          return new ResponsePacket()
            .setType("info")
            .setData(data);
        }
        case "players" -> {
          List<ServerPlayer> playerList = sData.getPlayers();
          // Now convert this to an array of player names
          ArrayList<String> arr = new ArrayList<>();
          for (ServerPlayer player : playerList) {
            arr.add(player.getName().getString());
          }
          
          // Convert it to a flat array
          String[] names = arr.toArray(new String[0]);
          
          // Make a response packet
          return new ResponsePacket()
            .setType("info")
            .setData(Arrays.toString(names));
        }
        default -> {
          return new ResponsePacket()
            .setType("info")
            .setData("data type not found");
        }
      }
    } catch (Exception e) {
      return new ResponsePacket()
        .setType("error_info")
        .setData("ERROR " + e.getMessage());
    }
  }
  
  public ResponsePacket executePacket(CommandPacket packet) {
    try {
      MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
      server.getCommands().performPrefixedCommand(
        this.getSource(),
        packet.fullyQualifiedCommand()
      );
  
      return new ResponsePacket()
        .setType("command")
        .setData(receiver.getLastOutput());
      
    } catch (Exception e) {
      return new ResponsePacket()
        .setType("error_command")
        .setData("ERROR " + e.getMessage());
    }
  }
}
