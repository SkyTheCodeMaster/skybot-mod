package com.skythecodemaster.skybot.websocket;

import com.google.gson.Gson;
import com.skythecodemaster.skybot.websocket.packets.outgoing.ResponsePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;

public class WSEvents {
  private final Gson gson = new Gson();
  @SubscribeEvent
  public void onServerChat(ServerChatEvent event) {
    String message = event.getRawText();
    String username = event.getUsername();
    HashMap<String, String> data = new HashMap<>();
    data.put("username",username);
    data.put("message" ,message );
    
    String sData = gson.toJson(data);
    ResponsePacket response = new ResponsePacket()
      .setType("chat")
      .setData(sData);
    
    String sPacket = gson.toJson(response);
    WSServer.sendString(sPacket);
  }
  
  @SubscribeEvent
  public void onLivingDeath(LivingDeathEvent event) {
    LivingEntity entity = event.getEntity();
    if (entity instanceof ServerPlayer) {
      // A player has died.
      String source = event.getSource().
        getLocalizedDeathMessage(entity).
        getString();
      
      ResponsePacket response = new ResponsePacket()
        .setType("death")
        .setData(source);
  
      String sPacket = gson.toJson(response);
      WSServer.sendString(sPacket);
    }
  }
  
  @SubscribeEvent
  public void onAdvancementEvent(AdvancementEvent.AdvancementEarnEvent event) {
    String advancementName = event.getAdvancement().toString();
    String username = event.getEntity().getName().toString();
  
    HashMap<String, String> data = new HashMap<>();
    data.put("username",username);
    data.put("source",advancementName);
  
    String sData = gson.toJson(data);
  
    ResponsePacket response = new ResponsePacket()
      .setType("advancement")
      .setData(sData);
  
    String sPacket = gson.toJson(response);
    WSServer.sendString(sPacket);
  }
  
  @SubscribeEvent
  public void onPlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
    Player player = event.getEntity();
    // A player has joined.
    String username = player.getName().toString();
  
    ResponsePacket response = new ResponsePacket()
      .setType("leave")
      .setData(username);
  
    String sPacket = gson.toJson(response);
    WSServer.sendString(sPacket);
  }
  
  @SubscribeEvent
  public void onPlayerWakeUpEvent(PlayerWakeUpEvent event) {
    String username = event.getEntity().getName().toString();
    
    ResponsePacket response = new ResponsePacket()
      .setType("sleep")
      .setData(username);
  
    String sPacket = gson.toJson(response);
    WSServer.sendString(sPacket);
  }
  
  @SubscribeEvent
  public void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
    Player player = event.getEntity();
    // A player has joined.
    String username = player.getName().toString();
      
    ResponsePacket response = new ResponsePacket()
      .setType("join")
      .setData(username);
      
    String sPacket = gson.toJson(response);
    WSServer.sendString(sPacket);
  }
}
