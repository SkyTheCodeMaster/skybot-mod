package com.skythecodemaster.skybot;

import com.skythecodemaster.skybot.websocket.WSEvents;
import com.skythecodemaster.skybot.websocket.WSServer;
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
    MinecraftForge.EVENT_BUS.register(new WSEvents()); // Register the WS events
  }

  private void runWebserver(WSServer server, String host, int port) {
    try {
      new Thread(server).start();
      LOGGER.info("Running server on ws://" + host + ":" + port);
    } catch (Exception e) {
      LOGGER.error("Caught exception in webserver; restarting in 10 seconds...");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        runWebserver(server, host, port);
    }
  }
  
  // This is called when the server world finishes loading, aka perfect time to set up chat integration
  @SubscribeEvent
  public void onServerStarting(ServerStartingEvent event) {
    // Print out a little message
    LOGGER.info("SkyBot Server activating...");
    
    // Set host and port for server
    String host = "127.0.0.1";
    int port = 40000;


    WSServer server = new WSServer(new InetSocketAddress(host,port));
    server.setReuseAddr(true);
    runWebserver(server, host, port);
  }
}