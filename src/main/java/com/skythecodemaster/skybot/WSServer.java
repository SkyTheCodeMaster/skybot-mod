package com.skythecodemaster.skybot;

// Minecraft logging stuff
import com.mojang.logging.LogUtils;
import com.skythecodemaster.skybot.packets.BasePacket;
import com.skythecodemaster.skybot.packets.outgoing.ResponsePacket;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

// Websocket server
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WSServer extends WebSocketServer {
  
  private static final Logger LOGGER = LogUtils.getLogger();
  private ServerUtils sUtils = new ServerUtils();
  
  public WSServer(InetSocketAddress address) {
    super(address);
    
    LOGGER.info("WS Server instantiated...");
  }
  
  private WebSocket conn;
  
  public WebSocket getConn() {
    return this.conn;
  }
  
  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
    if (this.conn != null) {
      conn.send("client already connected");
      conn.close();
    }
    this.conn = conn;
    conn.send("hello"); // Later we will add information packets such as players, version, etc
    LOGGER.info("New client connected: " + conn.getRemoteSocketAddress());
  }
  
  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    this.conn = null;
    LOGGER.info("Client disconnected: " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
  }
  
  @Override
  public void onMessage(WebSocket conn, String message) {
    LOGGER.info("Received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
    // Parse the json out to the base class
    BasePacket packet = sUtils.parsePacket(message);
    // Now execute based on the contents of the packet
    ResponsePacket resp = sUtils.executePacket(packet);
    
  }
  
  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
    LOGGER.info("Received ByteBuffer from "	+ conn.getRemoteSocketAddress());
    // echo
    conn.send(message);
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