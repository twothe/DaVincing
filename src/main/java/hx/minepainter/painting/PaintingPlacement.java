package hx.minepainter.painting;

import net.minecraft.block.Block;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

public enum PaintingPlacement {

  UPXNEG(ForgeDirection.UP, ForgeDirection.WEST), UPXPOS(ForgeDirection.UP, ForgeDirection.EAST), UPZNEG(ForgeDirection.UP, ForgeDirection.NORTH), UPZPOS(ForgeDirection.UP, ForgeDirection.SOUTH), XNEG(ForgeDirection.WEST, ForgeDirection.DOWN), XPOS(ForgeDirection.EAST, ForgeDirection.DOWN), ZNEG(ForgeDirection.NORTH, ForgeDirection.DOWN), ZPOS(ForgeDirection.SOUTH, ForgeDirection.DOWN), DOWNXNEG(ForgeDirection.DOWN, ForgeDirection.WEST), DOWNXPOS(ForgeDirection.DOWN, ForgeDirection.EAST), DOWNZNEG(ForgeDirection.DOWN, ForgeDirection.NORTH), DOWNZPOS(ForgeDirection.DOWN, ForgeDirection.SOUTH);

  ForgeDirection normal;
  ForgeDirection ypos;
  ForgeDirection xpos;

  public static PaintingPlacement of(int id) {
    return values()[(id % values().length)];
  }

  public static PaintingPlacement of(Vec3 vec, int face) {
    ForgeDirection dir = ForgeDirection.getOrientation(face);
    switch (1. 
       
       
      $SwitchMap$net$minecraftforge$common$util$ForgeDirection[dir.ordinal()])
    {
    case 1:
        return ZPOS;
      case 2:
        return ZNEG;
      case 3:
        return XNEG;
      case 4:
        return XPOS;
      case 5:
        if (Math.abs(vec.field_72450_a) > Math.abs(vec.field_72449_c)) {
          return vec.field_72450_a > 0.0D ? DOWNXNEG : DOWNXPOS;
        }
        return vec.field_72449_c > 0.0D ? DOWNZNEG : DOWNZPOS;
      case 6:
        if (Math.abs(vec.field_72450_a) > Math.abs(vec.field_72449_c)) {
          return vec.field_72450_a > 0.0D ? UPXNEG : UPXPOS;
        }
        return vec.field_72449_c > 0.0D ? UPZNEG : UPZPOS;
    }
    return null;
  }

  private PaintingPlacement(ForgeDirection normal, ForgeDirection ypos) {
    this.normal = normal;
    this.ypos = ypos;
    this.xpos = normal.getRotation(ypos);
  }

  public float[] painting2blockWithShift(float x, float y, float shift) {
    float[] point = new float[3];
    point[0] = ((1 - (this.xpos.offsetX + this.ypos.offsetX + this.normal.offsetX)) / 2);
    point[1] = ((1 - (this.xpos.offsetY + this.ypos.offsetY + this.normal.offsetY)) / 2);
    point[2] = ((1 - (this.xpos.offsetZ + this.ypos.offsetZ + this.normal.offsetZ)) / 2);
    point[0] += this.xpos.offsetX * x + this.ypos.offsetX * y + this.normal.offsetX * shift;
    point[1] += this.xpos.offsetY * x + this.ypos.offsetY * y + this.normal.offsetY * shift;
    point[2] += this.xpos.offsetZ * x + this.ypos.offsetZ * y + this.normal.offsetZ * shift;
    return point;
  }

  public float[] painting2block(float x, float y) {
    return painting2blockWithShift(x, y, 0.0625F);
  }

  public float[] block2painting(float x, float y, float z) {
    float[] point = new float[2];
    point[0] = ((1 - this.xpos.offsetX - this.xpos.offsetY - this.xpos.offsetZ) / 2);
    point[1] = ((1 - this.ypos.offsetX - this.ypos.offsetY - this.ypos.offsetZ) / 2);
    point[0] += this.xpos.offsetX * x + this.xpos.offsetY * y + this.xpos.offsetZ * z;
    point[1] += this.ypos.offsetX * x + this.ypos.offsetY * y + this.ypos.offsetZ * z;
    return point;
  }

  public void setBlockBounds(Block b) {
    b.func_149676_a(0 + (1 - this.normal.offsetX) / 2, 0 + (1 - this.normal.offsetY) / 2, 0 + (1 - this.normal.offsetZ) / 2, 1 - (1 + this.normal.offsetX) / 2, 1 - (1 + this.normal.offsetY) / 2, 1 - (1 + this.normal.offsetZ) / 2);
  }
}
