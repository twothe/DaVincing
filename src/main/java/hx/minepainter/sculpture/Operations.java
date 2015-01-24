package hx.minepainter.sculpture;

import hx.minepainter.ModMinePainter;
import hx.utils.BlockLoader;
import hx.utils.ItemLoader;
import hx.utils.Utils;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class Operations {

  static double length;

  public static int editSubBlock(World w, int[] minmax, int x, int y, int z, Block block, byte meta) {
    int s = 0;

    LinkedList<int[]> droplist = new LinkedList();
    for (int _x = minmax[0]; _x < minmax[3]; _x++) {
      for (int _y = minmax[1]; _y < minmax[4]; _y++) {
        for (int _z = minmax[2]; _z < minmax[5]; _z++) {
          int tx = x;
          int ty = y;
          int tz = z;
          for (; _x > 7; tx++) {
            _x -= 8;
          }
          for (; _y > 7; ty++) {
            _y -= 8;
          }
          for (; _z > 7; tz++) {
            _z -= 8;
          }
          for (; _x < 0; tx--) {
            _x += 8;
          }
          for (; _y < 0; ty--) {
            _y += 8;
          }
          for (; _z < 0; tz--) {
            _z += 8;
          }
          Block tgt_block = w.getBlock(tx, ty, tz);
          int tgt_meta = w.getBlockMetadata(tx, ty, tz);
          if ((tgt_block == Blocks.field_150350_a) && (block != Blocks.field_150350_a)) {
            w.func_147449_b(tx, ty, tz, ModMinePainter.sculpture.block);
          } else if (sculptable(tgt_block, tgt_meta)) {
            convertToFullSculpture(w, tx, ty, tz);
          }
          if (w.getBlock(tx, ty, tz) == ModMinePainter.sculpture.block) {
            SculptureEntity se = (SculptureEntity) w.func_147438_o(tx, ty, tz);
            Block former = se.sculpture.getBlockAt(_x, _y, _z, null);
            int metaFormer = se.sculpture.getMetaAt(_x, _y, _z, null);
            addDrop(droplist, former, metaFormer);
            se.sculpture.setBlockAt(_x, _y, _z, block, meta);
            if (se.sculpture.isEmpty()) {
              w.func_147449_b(x, y, z, Blocks.field_150350_a);
            }
            if (w.isRemote) {
              se.getRender().changed = true;
            } else {
              w.func_147471_g(tx, ty, tz);
            }
            s++;
          }
        }
      }
    }
    for (int[] drop : droplist) {
      if (drop[0] != 0) {
        dropScrap(w, x, y, z, Block.getBlockById(drop[0]), (byte) drop[1], drop[2]);
      }
    }
    return s;
  }

  private static void addDrop(List<int[]> drops, Block block, int meta) {
    int id = Block.getIdFromBlock(block);
    for (int[] drop : drops) {
      if ((drop[0] == id) && (drop[1] == meta)) {
        drop[2] += 1;
        return;
      }
    }
    drops.add(new int[]{id, meta, 1});
  }

  public static void dropScrap(World w, int x, int y, int z, Block block, byte meta, int amount) {
    if (block == Blocks.field_150350_a) {
      return;
    }
    int covers = amount / 64;
    amount %= 64;
    int bars = amount / 8;
    amount %= 8;
    if (covers > 0) {
      ItemStack is = new ItemStack(ModMinePainter.cover.item);
      is.field_77994_a = covers;
      is.func_77964_b((Block.getIdFromBlock(block) << 4) + meta);
      ((SculptureBlock) ModMinePainter.sculpture.block).dropScrap(w, x, y, z, is);
    }
    if (bars > 0) {
      ItemStack is = new ItemStack(ModMinePainter.bar.item);
      is.field_77994_a = bars;
      is.func_77964_b((Block.getIdFromBlock(block) << 4) + meta);
      ((SculptureBlock) ModMinePainter.sculpture.block).dropScrap(w, x, y, z, is);
    }
    if (amount > 0) {
      ItemStack is = new ItemStack(ModMinePainter.piece.item);
      is.field_77994_a = amount;
      is.func_77964_b((Block.getIdFromBlock(block) << 4) + meta);
      ((SculptureBlock) ModMinePainter.sculpture.block).dropScrap(w, x, y, z, is);
    }
  }

  public static boolean sculptable(Block b, int blockMeta) {
    if (b == null) {
      return false;
    }
    if (b == Blocks.field_150349_c) {
      return false;
    }
    if (b == Blocks.field_150357_h) {
      return false;
    }
    if (b == Blocks.field_150434_aF) {
      return false;
    }
    if (b == Blocks.field_150359_w) {
      return true;
    }
    if (b == Blocks.field_150399_cn) {
      return true;
    }
    if (b == Blocks.field_150362_t) {
      return false;
    }
    if (b.hasTileEntity(blockMeta)) {
      return false;
    }
    if (!b.func_149686_d()) {
      return false;
    }
    if (b.func_149753_y() != 1.0D) {
      return false;
    }
    if (b.func_149669_A() != 1.0D) {
      return false;
    }
    if (b.func_149693_C() != 1.0D) {
      return false;
    }
    if (b.func_149704_x() != 0.0D) {
      return false;
    }
    if (b.func_149665_z() != 0.0D) {
      return false;
    }
    if (b.func_149706_B() != 0.0D) {
      return false;
    }
    return true;
  }

  public static void convertToFullSculpture(World w, int x, int y, int z) {
    Block was = w.getBlock(x, y, z);
    int meta = w.getBlockMetadata(x, y, z);
    w.func_147449_b(x, y, z, ModMinePainter.sculpture.block);
    SculptureEntity se = (SculptureEntity) w.func_147438_o(x, y, z);
    for (int i = 0; i < 512; i++) {
      se.sculpture.setBlockAt(i >> 6 & 0x7, i >> 3 & 0x7, i >> 0 & 0x7, was, (byte) meta);
    }
  }

  static int[] xyzf = {-1, -1, -1, -1};
  public static final int PLACE = 1;
  public static final int ALLX = 2;
  public static final int ALLY = 4;
  public static final int ALLZ = 8;
  public static final int DAMAGE = 16;
  public static final int CONSUME = 32;

  public static int[] raytrace(int x, int y, int z, EntityPlayer ep) {
    Block sculpture = ep.worldObj.getBlock(x, y, z);
    Sculpture the_sculpture = null;
    if (sculpture == ModMinePainter.sculpture.block) {
      SculptureEntity se = (SculptureEntity) Utils.getTE(ep.worldObj, x, y, z);
      the_sculpture = se.sculpture();
    }
    Vec3 from = ep.func_70666_h(1.0F);
    from = from.func_72441_c(-x, -y, -z);
    Vec3 look = ep.func_70040_Z();

    return raytrace(the_sculpture, from, from.func_72441_c(look.field_72450_a * 5.0D, look.field_72448_b * 5.0D, look.field_72449_c * 5.0D));
  }

  public static int[] raytrace(Sculpture sculpture, Vec3 start, Vec3 end) {
    byte tmp21_20 = (xyzf[2] = xyzf[3] = -1);
    xyzf[1] = tmp21_20;
    xyzf[0] = tmp21_20;
    length = 1.7976931348623157E+308D;
    for (int x = 0; x <= 8; x++) {
      Vec3 hit = start.func_72429_b(end, x / 8.0F);
      if (hit != null) {
        if ((hit.field_72448_b >= 0.0D)
                && (hit.field_72449_c >= 0.0D)) {
          int y = (int) (hit.field_72448_b * 8.0D);
          int z = (int) (hit.field_72449_c * 8.0D);
          if (end.field_72450_a > start.field_72450_a) {
            updateRaytraceResult(sculpture, x, y, z, ForgeDirection.WEST.ordinal(), hit.func_72444_a(start).func_72433_c());
          } else {
            updateRaytraceResult(sculpture, x - 1, y, z, ForgeDirection.EAST.ordinal(), hit.func_72444_a(start).func_72433_c());
          }
        }
      }
    }
    for (int y = 0; y <= 8; y++) {
      Vec3 hit = start.func_72435_c(end, y / 8.0F);
      if (hit != null) {
        if ((hit.field_72450_a >= 0.0D)
                && (hit.field_72449_c >= 0.0D)) {
          int x = (int) (hit.field_72450_a * 8.0D);
          int z = (int) (hit.field_72449_c * 8.0D);
          if (end.field_72448_b > start.field_72448_b) {
            updateRaytraceResult(sculpture, x, y, z, ForgeDirection.DOWN.ordinal(), hit.func_72444_a(start).func_72433_c());
          } else {
            updateRaytraceResult(sculpture, x, y - 1, z, ForgeDirection.UP.ordinal(), hit.func_72444_a(start).func_72433_c());
          }
        }
      }
    }
    for (int z = 0; z <= 8; z++) {
      Vec3 hit = start.func_72434_d(end, z / 8.0F);
      if (hit != null) {
        if ((hit.field_72450_a >= 0.0D)
                && (hit.field_72448_b >= 0.0D)) {
          int x = (int) (hit.field_72450_a * 8.0D);
          int y = (int) (hit.field_72448_b * 8.0D);
          if (end.field_72449_c > start.field_72449_c) {
            updateRaytraceResult(sculpture, x, y, z, ForgeDirection.NORTH.ordinal(), hit.func_72444_a(start).func_72433_c());
          } else {
            updateRaytraceResult(sculpture, x, y, z - 1, ForgeDirection.SOUTH.ordinal(), hit.func_72444_a(start).func_72433_c());
          }
        }
      }
    }
    return xyzf;
  }

  private static void updateRaytraceResult(Sculpture sculpture, int x, int y, int z, int f, double len) {
    if (!Sculpture.contains(x, y, z)) {
      return;
    }
    if ((sculpture != null)
            && (sculpture.getBlockAt(x, y, z, null) == Blocks.field_150350_a)) {
      return;
    }
    if (len >= length) {
      return;
    }
    length = len;
    xyzf[0] = x;
    xyzf[1] = y;
    xyzf[2] = z;
    xyzf[3] = f;
  }

  public static void setBlockBoundsFromRaytrace(int[] pos, Block block, int type) {
    pos = (int[]) pos.clone();
    if (hasFlag(type, 1)) {
      ForgeDirection dir = ForgeDirection.getOrientation(pos[3]);
      pos[0] += dir.offsetX;
      pos[1] += dir.offsetY;
      pos[2] += dir.offsetZ;
    }
    int x = 0;
    int y = 0;
    int z = 0;
    for (; pos[0] < 0; x--) {
      pos[0] += 8;
    }
    for (; pos[0] > 7; x++) {
      pos[0] -= 8;
    }
    for (; pos[1] < 0; y--) {
      pos[1] += 8;
    }
    for (; pos[1] > 7; y++) {
      pos[1] -= 8;
    }
    for (; pos[2] < 0; z--) {
      pos[2] += 8;
    }
    for (; pos[2] > 7; z++) {
      pos[2] -= 8;
    }
    boolean allx = (type & 0x2) > 0;
    boolean ally = (type & 0x4) > 0;
    boolean allz = (type & 0x8) > 0;
    block.func_149676_a(allx ? x + 0 : x + pos[0] / 8.0F, ally ? y + 0 : y + pos[1] / 8.0F, allz ? z + 0 : z + pos[2] / 8.0F, allx ? x + 1 : x + (pos[0] + 1) / 8.0F, ally ? y + 1 : y + (pos[1] + 1) / 8.0F, allz ? z + 1 : z + (pos[2] + 1) / 8.0F);
  }

  public static boolean validOperation(World worldObj, int x, int y, int z, int[] pos, int chiselFlags) {
    pos = (int[]) pos.clone();
    if (hasFlag(chiselFlags, 1)) {
      ForgeDirection dir = ForgeDirection.getOrientation(pos[3]);
      pos[0] += dir.offsetX;
      pos[1] += dir.offsetY;
      pos[2] += dir.offsetZ;
    }
    for (; pos[0] < 0; x--) {
      pos[0] += 8;
    }
    for (; pos[0] > 7; x++) {
      pos[0] -= 8;
    }
    for (; pos[1] < 0; y--) {
      pos[1] += 8;
    }
    for (; pos[1] > 7; y++) {
      pos[1] -= 8;
    }
    for (; pos[2] < 0; z--) {
      pos[2] += 8;
    }
    for (; pos[2] > 7; z++) {
      pos[2] -= 8;
    }
    Block b = worldObj.getBlock(x, y, z);
    if (hasFlag(chiselFlags, 1)) {
      if (b == Blocks.field_150350_a) {
        return true;
      }
      if (b == ModMinePainter.sculpture.block) {
        return true;
      }
      return false;
    }
    int meta = worldObj.getBlockMetadata(x, y, z);
    if (b == Blocks.field_150350_a) {
      return false;
    }
    if (b == ModMinePainter.sculpture.block) {
      return true;
    }
    if (sculptable(b, meta)) {
      return true;
    }
    return false;
  }

  private static boolean hasFlag(int flags, int mask) {
    return (flags & mask) > 0;
  }

  public static boolean applyOperation(World w, int x, int y, int z, int[] pos, int flags, Block editBlock, int editMeta) {
    pos = (int[]) pos.clone();
    if (hasFlag(flags, 1)) {
      ForgeDirection dir = ForgeDirection.getOrientation(pos[3]);
      pos[0] += dir.offsetX;
      pos[1] += dir.offsetY;
      pos[2] += dir.offsetZ;
    }
    for (; pos[0] < 0; x--) {
      pos[0] += 8;
    }
    for (; pos[0] > 7; x++) {
      pos[0] -= 8;
    }
    for (; pos[1] < 0; y--) {
      pos[1] += 8;
    }
    for (; pos[1] > 7; y++) {
      pos[1] -= 8;
    }
    for (; pos[2] < 0; z--) {
      pos[2] += 8;
    }
    for (; pos[2] > 7; z++) {
      pos[2] -= 8;
    }
    int[] minmax = new int[6];
    boolean allx = hasFlag(flags, 2);
    boolean ally = hasFlag(flags, 4);
    boolean allz = hasFlag(flags, 8);
    minmax[0] = (allx ? 0 : pos[0]);
    minmax[1] = (ally ? 0 : pos[1]);
    minmax[2] = (allz ? 0 : pos[2]);
    minmax[3] = (allx ? 8 : pos[0] + 1);
    minmax[4] = (ally ? 8 : pos[1] + 1);
    minmax[5] = (allz ? 8 : pos[2] + 1);

    int blocks = editSubBlock(w, minmax, x, y, z, editBlock, (byte) editMeta);

    return blocks > 0;
  }

  public static int getLookingAxis(EntityPlayer ep) {
    Vec3 vec = ep.func_70040_Z();
    double x = Math.abs(vec.field_72450_a);
    double y = Math.abs(vec.field_72448_b);
    double z = Math.abs(vec.field_72449_c);
    if ((x >= y) && (x >= z)) {
      return 0;
    }
    if ((y >= x) && (y >= z)) {
      return 1;
    }
    if ((z >= x) && (z >= y)) {
      return 2;
    }
    return 0;
  }
}
