package com.skythecodemaster.skybot;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.net.InetSocketAddress;

@Mod(SkyBotMod.MOD_ID)
public class SkyBotMod {
  public static final String MOD_ID = "skybot";
  // Directly reference a slf4j logger
  private static final Logger LOGGER = LogUtils.getLogger();
  
  public SkyBotMod() {
    MinecraftForge.EVENT_BUS.register(this);
  }
  
  // This is called when the server starts
  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {
    // Print out a little message
    LOGGER.info("SkyBot Server activating...");
    
    // Set host and port for server
    String host = "localhost";
    int port = 20500;
    
    WSServer server = new WSServer(new InetSocketAddress(host,port));
    LOGGER.info("Running server on ws://" + host + ":" + port);
    server.run();
  }
}