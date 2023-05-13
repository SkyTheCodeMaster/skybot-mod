package com.skythecodemaster.skybot.websocket;

// Minecraft logging stuff
import com.mojang.logging.LogUtils;
import com.skythecodemaster.skybot.websocket.packets.BasePacket;
import com.skythecodemaster.skybot.websocket.packets.incoming.ChatPacket;
import com.skythecodemaster.skybot.websocket.packets.incoming.CommandPacket;
import com.skythecodemaster.skybot.websocket.packets.incoming.InfoPacket;
import com.skythecodemaster.skybot.websocket.packets.outgoing.ResponsePacket;
import org.slf4j.Logger;

// Websocket server
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class WSServer extends WebSocketServer {
  
  private static final Logger LOGGER = LogUtils.getLogger();
  private final ServerUtils sUtils = new ServerUtils();
  
  public WSServer(InetSocketAddress address) {
    super(address);
    
    LOGGER.info("WS Server instantiated...");
  }
  
  private static WebSocket conn;
  
  public static void sendString(String data) {
    if (conn == null) {
      return;
    }
    conn.send(data);
  }
  
  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
    if (WSServer.conn != null) {
      conn.send("client already connected");
      conn.close();
    }
    WSServer.conn = conn;
    conn.send("hello"); // Later we will add information packets such as players, version, etc
    LOGGER.info("New client connected: " + conn.getRemoteSocketAddress());
  }
  
  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    WSServer.conn = null;
    LOGGER.info("Client disconnected: " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
  }
  
  @Override
  public void onMessage(WebSocket conn, String message) {
    LOGGER.info("Received message from " + conn.getRemoteSocketAddress() + ": " + message);
    // Parse the json out to the base class
    BasePacket packet;
    try {
      packet = sUtils.parsePacket(message);
    } catch (IllegalArgumentException e) {
      conn.send(e.getMessage());
      return; // If we error and die, don't finish executing the packet.
    }
    // Now execute based on the contents of the
    ResponsePacket resp = null;
    if (packet instanceof ChatPacket) {
      resp = sUtils.executeChatPacket((ChatPacket) packet);
    } else if (packet instanceof InfoPacket) {
      resp = sUtils.executeInfoPacket((InfoPacket) packet);
    } else if (packet instanceof CommandPacket) {
      resp = sUtils.executeCommandPacket((CommandPacket) packet);
    }
  
    // JSONify the response
    String json_resp = sUtils.jsonifyResponse(resp);
    // Send it down the websocket
    conn.send(json_resp);
  }
  
  @Override
  public void onMessage(WebSocket conn, ByteBuffer message) {
    LOGGER.info("Received ByteBuffer from "	+ conn.getRemoteSocketAddress());
    // echo
    conn.send("Binary messages are not supported!");
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