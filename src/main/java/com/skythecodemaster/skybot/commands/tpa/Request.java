package com.skythecodemaster.skybot.commands.tpa;

import java.util.UUID;

public class Request {
  private UUID source;
  private UUID target;
  private long expiry;
  
  private boolean direction;
  // False is from source -> target, aka normal tpa
  // True is from target -> source, aka tpahere
  
  public Request(UUID source, UUID target, long expiry, boolean direction) {
    this.source = source;
    this.target = target;
    this.expiry = expiry;
    this.direction = direction;
  }
  
  public long getExpiry() {
    return this.expiry;
  }
  
  public UUID getSource() {
    return this.source;
  }
  
  public UUID getTarget() {
    return this.target;
  }
  public boolean getDirection() {
    return this.direction;
  }
}
