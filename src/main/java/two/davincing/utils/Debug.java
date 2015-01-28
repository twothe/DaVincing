package two.davincing.utils;

public class Debug {

  public static <T> void log(T... thing) {
    StringBuilder sb = new StringBuilder();
    for (T i : thing) {
      sb.append(i + ", ");
    }
    System.err.println(sb.toString());
  }

}
