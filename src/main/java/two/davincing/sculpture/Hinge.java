package two.davincing.sculpture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import two.davincing.DaVincing;
import two.davincing.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import two.davincing.ProxyBase;

public enum Hinge {

  Y0Z0(ForgeDirection.DOWN, ForgeDirection.NORTH),
  Y0Z1(ForgeDirection.DOWN, ForgeDirection.SOUTH),
  Y1Z0(ForgeDirection.UP, ForgeDirection.NORTH),
  Y1Z1(ForgeDirection.UP, ForgeDirection.SOUTH),
  Z0X0(ForgeDirection.NORTH, ForgeDirection.WEST),
  Z0X1(ForgeDirection.NORTH, ForgeDirection.EAST),
  Z1X0(ForgeDirection.SOUTH, ForgeDirection.WEST),
  Z1X1(ForgeDirection.SOUTH, ForgeDirection.EAST),
  X0Y0(ForgeDirection.WEST, ForgeDirection.DOWN),
  X0Y1(ForgeDirection.WEST, ForgeDirection.UP),
  X1Y0(ForgeDirection.EAST, ForgeDirection.DOWN),
  X1Y1(ForgeDirection.EAST, ForgeDirection.UP);

  public final ForgeDirection dir1, dir2;
  private static float[] bounds = new float[]{-0.05f, 0.1f, 0.9f, 1.05f};

  Hinge(ForgeDirection dir1, ForgeDirection dir2) {
    this.dir1 = dir1;
    this.dir2 = dir2;
  }

  public void toNBT(NBTTagCompound nbt) {
    nbt.setByte("hinge", (byte) (this.ordinal() + 1));
  }

  public int getRotationFace(ForgeDirection push) {
    if (push == dir1 || push == dir2.getOpposite()) {
      return dir1.getRotation(dir2).ordinal();
    }
    if (push == dir2 || push == dir1.getOpposite()) {
      return dir2.getRotation(dir1).ordinal();
    }
    return -1;
  }

  public ForgeDirection getShift(ForgeDirection push) {
    if (push == dir1 || push == dir2.getOpposite()) {
      return dir1;
    }
    if (push == dir2 || push == dir1.getOpposite()) {
      return dir2;
    }
    return null;
  }

  @SideOnly(Side.CLIENT)
  public void setRenderBounds(Block block) {
    int x = dir1.offsetX + dir2.offsetX + 1;
    int y = dir1.offsetY + dir2.offsetY + 1;
    int z = dir1.offsetZ + dir2.offsetZ + 1;
//		Debug.log("bounds : ", x,y,z);
    block.setBlockBounds(bounds[x], bounds[y], bounds[z],
            bounds[x + 1], bounds[y + 1], bounds[z + 1]);
  }

  /** get a hinge from sub-block x,y,z coords.
   */
  public static Hinge placedAt(float x, float y, float z) {
    float dx = Math.abs(x - 0.5f);
    float dy = Math.abs(y - 0.5f);
    float dz = Math.abs(z - 0.5f);

    int ordinal = 0;
    if (dx <= dy && dx <= dz) {
      ordinal = 0;
      if (y > 0.5f) {
        ordinal += 2;
      }
      if (z > 0.5f) {
        ordinal += 1;
      }
    } else if (dy <= dx && dy <= dz) {
      ordinal = 4;
      if (z > 0.5f) {
        ordinal += 2;
      }
      if (x > 0.5f) {
        ordinal += 1;
      }
    } else if (dz <= dy && dz <= dx) {
      ordinal = 8;
      if (x > 0.5f) {
        ordinal += 2;
      }
      if (y > 0.5f) {
        ordinal += 1;
      }
    }

    return Hinge.values()[ordinal];
  }

  public static Hinge fromSculpture(IBlockAccess iba, int x, int y, int z) {
    if (iba.getBlock(x, y, z) != ProxyBase.blockSculpture.getBlock()) {
      return null;
    }
    SculptureEntity se = Utils.getTE(iba, x, y, z);
    return fromSculpture(se);
  }

  public static Hinge fromSculpture(SculptureEntity se) {
    if (se == null) {
      return null;
    }
    return se.getHinge();
  }

  public static Hinge fromNBT(NBTTagCompound nbt) {
    byte thing = nbt.getByte("hinge");
    if (thing == 0) {
      return null;
    }
    return Hinge.values()[thing - 1];
  }
}
