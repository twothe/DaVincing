package hx.utils;

import cpw.mods.fml.common.FMLLog;

public class Debug {

  public static <T> void log(T... thing) {
    StringBuilder sb = new StringBuilder("[MinePainter]");
    for (T i : thing) {
      sb.append(i).append(", ");
    }
    FMLLog.severe(sb.toString());
  }
}
