package two.davincing.sculpture;

import net.minecraftforge.common.util.ForgeDirection;

public class HingeRotationTable {

  private static Hinge[][] table = new Hinge[12][6];

  private static Hinge find(ForgeDirection dir1, ForgeDirection dir2) {
    for (Hinge h : Hinge.values()) {
      if (h.dir1 == dir1 && h.dir2 == dir2) {
        return h;
      }
      if (h.dir1 == dir2 && h.dir2 == dir1) {
        return h;
      }
    }
    return null;
  }

  static {

    for (int i = 0; i < 12; i++) {
      for (int j = 0; j < 6; j++) {

        Hinge h = Hinge.values()[i];
        ForgeDirection axis = ForgeDirection.getOrientation(j);

        ForgeDirection dir1 = axis.getRotation(h.dir1);
        ForgeDirection dir2 = axis.getRotation(h.dir2);
        table[i][j] = find(dir1, dir2);
      }
    }
  }

  public static Hinge rotate(Hinge h, int face) {
    return table[h.ordinal()][face];
  }
}
