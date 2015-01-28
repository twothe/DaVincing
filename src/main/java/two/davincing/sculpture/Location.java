package two.davincing.sculpture;

public class Location implements Comparable<Location> {

  int x, y, z;

  public Location(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public int compareTo(Location o) {
    if (x != o.x) {
      return x - o.x;
    }
    if (y != o.y) {
      return y - o.y;
    }
    if (z != o.z) {
      return z - o.z;
    }
    return 0;
  }

}
