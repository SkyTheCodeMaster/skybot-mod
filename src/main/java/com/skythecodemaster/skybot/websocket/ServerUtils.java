package com.skythecodemaster.skybot.websocket;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import com.skythecodemaster.skybot.websocket.packets.*;
import com.skythecodemaster.skybot.websocket.packets.incoming.ChatPacket;
import com.skythecodemaster.skybot.websocket.packets.incoming.CommandPacket;
import com.skythecodemaster.skybot.websocket.packets.incoming.InfoPacket;
import com.skythecodemaster.skybot.websocket.packets.outgoing.ResponsePacket;
import com.skythecodemaster.skybot.websocket.utils.CommandReceiver;
import com.skythecodemaster.skybot.websocket.utils.TickTimes;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerUtils {
  // Provide methods for taking a data packet and returning a new one with the response.
  private final Gson gson = new Gson(); // Use Gson for parse/stringify
  private final CommandReceiver receiver = new CommandReceiver();
  private static final Logger LOGGER = LogUtils.getLogger();
  
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
    BasePacket result = switch (packet.getType()) {
      case "command" -> gson.fromJson(packet.getValue(), CommandPacket.class);
      case "info"    -> gson.fromJson(packet.getValue(), InfoPacket.class);
      case "chat"    -> gson.fromJson(packet.getValue(), ChatPacket.class);
      default -> throw new IllegalArgumentException("Sent packet contains invalid type \"" + packet.getType() + "\"!");
    };
    result.setId(packet.getId());
    return result;
  }
  
  // Now provide methods for actually using the packet. Set these up as overloads so it's
  // simple.
  
  public ResponsePacket executeChatPacket(ChatPacket packet) {
    try {
      // Grab the player list
      List<ServerPlayer> players = ServerLifecycleHooks
        .getCurrentServer()
        .getPlayerList()
        .getPlayers();
  
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
        .withStyle(style);
  
      for (ServerPlayer player : players) {
        player.sendSystemMessage(message);
      }
  
      // Because we do not need to return anything of importance, just return a
      // packet with 'OK' as its response.
      return new ResponsePacket()
        .setType("chat")
        .setId(packet.getId())
        .setData("OK");
    } catch (Exception e) {
      // Something went wrong, report it.
      return new ResponsePacket()
        .setType("error_chat")
        .setId(packet.getId())
        .setData("ERROR " + e.getMessage());
    }
  }
  
  public ResponsePacket executeInfoPacket(InfoPacket packet) {
    try {
      ServerData sData = new ServerData();
  
      switch (packet.getData()) {
        case "tps" -> {
          TickTimes times = sData.getTickTimes();
          String data = Arrays.toString(times.toArray());
          return new ResponsePacket()
            .setType("info")
            .setId(packet.getId())
            .setData(data);
        }
        case "players" -> {
          List<ServerPlayer> playerList = sData.getPlayers();
          // Now convert this to an array of player names
          ArrayList<String> arr = new ArrayList<>();
          for (ServerPlayer player : playerList) {
            arr.add(player.getName().getString());
          }
          
          // Make a response packet
          return new ResponsePacket()
            .setType("info")
            .setId(packet.getId())
            .setData(gson.toJson(arr));
        }
        default -> {
          return new ResponsePacket()
            .setType("info")
            .setId(packet.getId())
            .setData("data type not found");
        }
      }
    } catch (Exception e) {
      return new ResponsePacket()
        .setType("error_info")
        .setData("ERROR " + e.getMessage());
    }
  }
  
  public ResponsePacket executeCommandPacket(CommandPacket packet) {
    try {
      MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
      server.getCommands().performPrefixedCommand(
        this.getSource(),
        packet.getCommand()
      );
  
      return new ResponsePacket()
        .setType("command")
        .setId(packet.getId())
        .setData(receiver.getLastOutput());
      
    } catch (Exception e) {
      return new ResponsePacket()
        .setType("error_command")
        .setId(packet.getId())
        .setData("ERROR " + e.getMessage());
    }
  }
  
  public String jsonifyResponse(@Nullable ResponsePacket resp) {
    if (resp == null) {
      return "'error occurred in data processing'";
    }
    return gson.toJson(resp);
  }
}
