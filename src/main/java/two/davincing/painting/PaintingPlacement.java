package two.davincing.painting;

import net.minecraft.block.Block;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

public enum PaintingPlacement {

  UPXNEG(ForgeDirection.UP, ForgeDirection.WEST),
  UPXPOS(ForgeDirection.UP, ForgeDirection.EAST),
  UPZNEG(ForgeDirection.UP, ForgeDirection.NORTH),
  UPZPOS(ForgeDirection.UP, ForgeDirection.SOUTH),
  XNEG(ForgeDirection.WEST, ForgeDirection.DOWN),
  XPOS(ForgeDirection.EAST, ForgeDirection.DOWN),
  ZNEG(ForgeDirection.NORTH, ForgeDirection.DOWN),
  ZPOS(ForgeDirection.SOUTH, ForgeDirection.DOWN),
  DOWNXNEG(ForgeDirection.DOWN, ForgeDirection.WEST),
  DOWNXPOS(ForgeDirection.DOWN, ForgeDirection.EAST),
  DOWNZNEG(ForgeDirection.DOWN, ForgeDirection.NORTH),
  DOWNZPOS(ForgeDirection.DOWN, ForgeDirection.SOUTH);

  public static PaintingPlacement of(int id) {
    return values()[id % values().length];
  }

  public static PaintingPlacement of(Vec3 vec, int face) {

    ForgeDirection dir = ForgeDirection.getOrientation(face);
    switch (dir) {
      case SOUTH:
        return ZPOS;
      case NORTH:
        return ZNEG;
      case WEST:
        return XNEG;
      case EAST:
        return XPOS;
      case DOWN:
        if (Math.abs(vec.xCoord) > Math.abs(vec.zCoord)) {
          return vec.xCoord > 0 ? DOWNXNEG : DOWNXPOS;
        }
        return vec.zCoord > 0 ? DOWNZNEG : DOWNZPOS;
      case UP:
        if (Math.abs(vec.xCoord) > Math.abs(vec.zCoord)) {
          return vec.xCoord > 0 ? UPXNEG : UPXPOS;
        }
        return vec.zCoord > 0 ? UPZNEG : UPZPOS;
      default:
        return null;
    }
  }

  ForgeDirection normal, ypos, xpos;

  PaintingPlacement(ForgeDirection normal, ForgeDirection ypos) {
    this.normal = normal;
    this.ypos = ypos;
    this.xpos = normal.getRotation(ypos);
  }

  public float[] painting2blockWithShift(float x, float y, float shift) {
    float[] point = new float[3];
    point[0] = (1 - (xpos.offsetX + ypos.offsetX + normal.offsetX)) / 2;
    point[1] = (1 - (xpos.offsetY + ypos.offsetY + normal.offsetY)) / 2;
    point[2] = (1 - (xpos.offsetZ + ypos.offsetZ + normal.offsetZ)) / 2;
    point[0] += xpos.offsetX * x + ypos.offsetX * y + normal.offsetX * shift;
    point[1] += xpos.offsetY * x + ypos.offsetY * y + normal.offsetY * shift;
    point[2] += xpos.offsetZ * x + ypos.offsetZ * y + normal.offsetZ * shift;
    return point;
  }

  public float[] painting2block(float x, float y) {
    return painting2blockWithShift(x, y, 1 / 16f);
  }

  public float[] block2painting(float x, float y, float z) {
    float[] point = new float[2];
    point[0] = (1 - xpos.offsetX - xpos.offsetY - xpos.offsetZ) / 2;
    point[1] = (1 - ypos.offsetX - ypos.offsetY - ypos.offsetZ) / 2;
    point[0] += xpos.offsetX * x + xpos.offsetY * y + xpos.offsetZ * z;
    point[1] += ypos.offsetX * x + ypos.offsetY * y + ypos.offsetZ * z;
    return point;
  }

  public void setBlockBounds(Block b) {
    b.setBlockBounds(0 + (1 - normal.offsetX) / 2,
            0 + (1 - normal.offsetY) / 2,
            0 + (1 - normal.offsetZ) / 2,
            1 - (1 + normal.offsetX) / 2,
            1 - (1 + normal.offsetY) / 2,
            1 - (1 + normal.offsetZ) / 2);
  }
}
