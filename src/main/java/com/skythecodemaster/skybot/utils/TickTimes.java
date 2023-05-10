package com.skythecodemaster.skybot.utils;

public class TickTimes {
  private double mspt;
  private double tps;
  
  public TickTimes(double mspt, double tps) {
    this.mspt = mspt;
    this.tps = tps;
  }
  
  public double getTPS() {
    return this.tps;
  }
  
  public double getMSPT() {
    return this.mspt;
  }
}
