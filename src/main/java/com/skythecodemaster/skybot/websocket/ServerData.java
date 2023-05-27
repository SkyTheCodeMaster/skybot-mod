package com.skythecodemaster.skybot.websocket;

import com.skythecodemaster.skybot.websocket.utils.TickTimes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;

public class ServerData {
  private double ms_to_tps(long mspt) {
    return Math.min(1000.0 / mspt, 20.0);
  }
  
  public List<ServerPlayer> getPlayers() {
    // Get the list of players
    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    return server.getPlayerList().getPlayers();
  }
  
  public TickTimes getTickTimes() {
    // Calculate average tps and average mspt
    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    long[] times = server.tickTimes;
    long mspt_sum = 0;
    long tps_sum = 0;
    
    for (long _mspt : times) {
      long mspt = _mspt / 1000000L;
      mspt_sum += mspt;
      tps_sum += ms_to_tps(mspt);
    }
    
    // Average them out
    double mspt = mspt_sum / (double) times.length;
    double tps = tps_sum / (double) times.length;
  
    return new TickTimes(mspt,tps);
    
  }
}
