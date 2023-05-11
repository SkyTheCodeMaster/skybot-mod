package com.skythecodemaster.skybot.packets.incoming;

import com.skythecodemaster.skybot.packets.BasePacket;

public class ChatPacket extends BasePacket {
  private String sender; // Whose name is inside the <>
  private String date; // Used for the hover event.
  private String message; // The message to be sent.
  private String colour; // What colour the name should be. This is mincraft colour codes.
  // The name of the sender.
  public String getSender() {
    return this.sender;
  }
  // The date for hover
  public String getDate() {
    return this.date;
  }
  // The message
  public String getMessage() {
    return this.message;
  }
  // The colour of the username
  public String getColour() {
    return this.colour;
  }
}
