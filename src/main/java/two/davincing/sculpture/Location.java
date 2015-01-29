package two.davincing.sculpture;

public class Location implements Comparable<Location> {

  int x, y, z;

  public Location(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public int compareTo(final Location o) {
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

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 23 * hash + this.x;
    hash = 23 * hash + this.y;
    hash = 23 * hash + this.z;
    return hash;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Location other = (Location) obj;
    if (this.x != other.x) {
      return false;
    }
    if (this.y != other.y) {
      return false;
    }
    if (this.z != other.z) {
      return false;
    }
    return true;
  }

}
