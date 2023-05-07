package com.skythecodemaster.skybot;

// Minecraft logging stuff
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

// Websocket server
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WSServer extends WebSocketServer {
  
  private static final Logger LOGGER = LogUtils.getLogger();
  
  public WSServer(InetSocketAddress address) {
    super(address);
    
    LOGGER.info("WS Server instantiated...");
  }
  
  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
    conn.send("hello"); // Later we will add information packets such as players, version, etc
    LOGGER.info("New client connected: " + conn.getRemoteSocketAddress());
  }
  
  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    LOGGER.info("Client disconnected: " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
  }
  
  @Override
  public void onMessage(WebSocket conn, String message) {
    LOGGER.info("Received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
  }
  
  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
    LOGGER.info("Received ByteBuffer from "	+ conn.getRemoteSocketAddress());
  }
  
  @Override
  public void onError(WebSocket conn, Exception ex) {
    LOGGER.info("An error occurred on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
  }
  
  @Override
  public void onStart() {
    LOGGER.info("WebSocket server started successfully");
  }
}