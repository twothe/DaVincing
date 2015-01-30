package two.davincing.sculpture;

import two.davincing.DaVincing;
import two.davincing.utils.Utils;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import two.davincing.ProxyBase;

public class Operations {

  public static int editSubBlock(World w, int[] minmax, int x, int y, int z, Block block, byte meta) {
    int tx, ty, tz;
    int s = 0;

    LinkedList<int[]> droplist = new LinkedList<int[]>();

    for (int _x = minmax[0]; _x < minmax[3]; _x++) {
      for (int _y = minmax[1]; _y < minmax[4]; _y++) {
        for (int _z = minmax[2]; _z < minmax[5]; _z++) {

          tx = x;
          ty = y;
          tz = z;

          while (_x > 7) {
            _x -= 8;
            tx++;
          }
          while (_y > 7) {
            _y -= 8;
            ty++;
          }
          while (_z > 7) {
            _z -= 8;
            tz++;
          }
          while (_x < 0) {
            _x += 8;
            tx--;
          }
          while (_y < 0) {
            _y += 8;
            ty--;
          }
          while (_z < 0) {
            _z += 8;
            tz--;
          }

          Block tgt_block = w.getBlock(tx, ty, tz);
          int tgt_meta = w.getBlockMetadata(tx, ty, tz);

          if (tgt_block == Blocks.air && block != Blocks.air) {
            w.setBlock(tx, ty, tz, ProxyBase.blockSculpture.getBlock());
          } else if (sculptable(tgt_block, tgt_meta)) {
            convertToFullSculpture(w, tx, ty, tz);
          }

          if (w.getBlock(tx, ty, tz) != ProxyBase.blockSculpture.getBlock()) {
            continue;
          }

          final TileEntity tileEntity = w.getTileEntity(tx, ty, tz);
          if (tileEntity instanceof SculptureEntity) {
            final SculptureEntity se = (SculptureEntity) tileEntity;
            Block former = se.sculpture.getBlockAt(_x, _y, _z, null);
            int metaFormer = se.sculpture.getMetaAt(_x, _y, _z, null);
            addDrop(droplist, former, metaFormer);
            se.sculpture.setBlockAt(_x, _y, _z, block, meta);
            if (se.sculpture.isEmpty()) {
              w.setBlock(tx, ty, tz, Blocks.air);
            }
            if (w.isRemote) {
              se.getRender().changed = true;
            } else {
              w.markBlockForUpdate(tx, ty, tz);
            }
          } else {
            DaVincing.log.error("[Operations.editSubBlock]: expected SculptureEntity at %d, %d, %d, but got %s", tx, ty, tz, (tileEntity == null ? "null" : tileEntity.getClass().getName()));
          }
          s++;
        }
      }
    }
    for (int[] drop : droplist) {
      if (drop[0] == 0) {
        continue;
      }
      dropScrap(w, x, y, z, Block.getBlockById(drop[0]), (byte) drop[1], drop[2]);
    }

    return s;
  }

  private static void addDrop(List<int[]> drops, Block block, int meta) {
    int id = Block.getIdFromBlock(block);
    for (int[] drop : drops) {
      if (drop[0] == id && drop[1] == meta) {
        drop[2]++;
        return;
      }
    }
    drops.add(new int[]{id, meta, 1});
  }

  public static void dropScrap(World w, int x, int y, int z, Block block, byte meta, int amount) {
//		Debug.log("dropping " + block.getUnlocalizedName() + " on " + (w.isRemote ? "client" : "server"));
    if (block == Blocks.air) {
      return;
    }

    int covers = amount / 64;
    amount %= 64;
    int bars = amount / 8;
    amount %= 8;

    if (covers > 0) {
      ItemStack is = new ItemStack(ProxyBase.itemCover.getItem());
      is.stackSize = covers;
      is.setItemDamage((Block.getIdFromBlock(block) << 4) + meta);
      ProxyBase.blockSculpture.getBlock().dropScrap(w, x, y, z, is);
    }

    if (bars > 0) {
      ItemStack is = new ItemStack(ProxyBase.itemBar.getItem());
      is.stackSize = bars;
      is.setItemDamage((Block.getIdFromBlock(block) << 4) + meta);
      ProxyBase.blockSculpture.getBlock().dropScrap(w, x, y, z, is);
    }

    if (amount > 0) {
      ItemStack is = new ItemStack(ProxyBase.itemPiece.getItem());
      is.stackSize = amount;
      is.setItemDamage((Block.getIdFromBlock(block) << 4) + meta);
      ProxyBase.blockSculpture.getBlock().dropScrap(w, x, y, z, is);
    }
  }

  public static boolean sculptable(Block b, int blockMeta) {
    if (b == null) {
      return false;
    }

    if (b == Blocks.grass) {
      return false;
    }
    if (b == Blocks.bedrock) {
      return false;
    }
    if (b == Blocks.cactus) {
      return false;
    }
    if (b == Blocks.glass) {
      return true;
    }
    if (b == Blocks.stained_glass) {
      return true;
    }
    if (b == Blocks.leaves) {
      return false;
    }

    if (b.hasTileEntity(blockMeta)) {
      return false;
    }
    if (!b.renderAsNormalBlock()) {
      return false;
    }

    if (b.getBlockBoundsMaxX() != 1.0f) {
      return false;
    }
    if (b.getBlockBoundsMaxY() != 1.0f) {
      return false;
    }
    if (b.getBlockBoundsMaxZ() != 1.0f) {
      return false;
    }
    if (b.getBlockBoundsMinX() != 0.0f) {
      return false;
    }
    if (b.getBlockBoundsMinY() != 0.0f) {
      return false;
    }
    if (b.getBlockBoundsMinZ() != 0.0f) {
      return false;
    }

    return true;
  }

  public static void convertToFullSculpture(World w, int x, int y, int z) {
    Block was = w.getBlock(x, y, z);
    int meta = w.getBlockMetadata(x, y, z);
    w.setBlock(x, y, z, ProxyBase.blockSculpture.getBlock());
    final TileEntity tileEntity = w.getTileEntity(x, y, z);
    if (tileEntity instanceof SculptureEntity) {
      final SculptureEntity se = (SculptureEntity) tileEntity;
      for (int i = 0; i < 512; i++) {
        se.sculpture.setBlockAt((i >> 6) & 7, (i >> 3) & 7, (i >> 0) & 7, was, (byte) meta);
      }
    } else {
      DaVincing.log.error("[Operations.convertToFullSculpture]: expected SculptureEntity at %d, %d, %d, but got %s", x, y, z, (tileEntity == null ? "null" : tileEntity.getClass().getName()));
    }
  }

  static double length;
  static int[] xyzf = new int[]{-1, -1, -1, -1};

  public static int[] raytrace(int x, int y, int z, EntityPlayer ep) {
    Block sculpture = ep.worldObj.getBlock(x, y, z);
    Sculpture the_sculpture = null;
    if (sculpture == ProxyBase.blockSculpture.getBlock()) {
      SculptureEntity se = Utils.getTE(ep.worldObj, x, y, z);
      the_sculpture = se.sculpture();
    }

    Vec3 from = ep.getPosition(1.0f);
    from = from.addVector(-x, -y, -z);
    Vec3 look = ep.getLookVec();

    return raytrace(the_sculpture, from, from.addVector(look.xCoord * 5, look.yCoord * 5, look.zCoord * 5));
  }

  public static int[] raytrace(Sculpture sculpture, Vec3 start, Vec3 end) {
    xyzf[0] = xyzf[1] = xyzf[2] = xyzf[3] = -1;
    length = Double.MAX_VALUE;

    for (int x = 0; x <= 8; x++) {

      Vec3 hit = start.getIntermediateWithXValue(end, x / 8f);
      if (hit == null) {
        continue;
      }

      if (hit.yCoord < 0) {
        continue;
      }
      if (hit.zCoord < 0) {
        continue;
      }
      int y = (int) (hit.yCoord * 8);
      int z = (int) (hit.zCoord * 8);

      if (end.xCoord > start.xCoord) {
        updateRaytraceResult(sculpture, x, y, z, ForgeDirection.WEST.ordinal(),
                hit.subtract(start).lengthVector());
      } else {
        updateRaytraceResult(sculpture, x - 1, y, z, ForgeDirection.EAST.ordinal(),
                hit.subtract(start).lengthVector());
      }
    }

    for (int y = 0; y <= 8; y++) {

      Vec3 hit = start.getIntermediateWithYValue(end, y / 8f);
      if (hit == null) {
        continue;
      }

      if (hit.xCoord < 0) {
        continue;
      }
      if (hit.zCoord < 0) {
        continue;
      }
      int x = (int) (hit.xCoord * 8);
      int z = (int) (hit.zCoord * 8);

      if (end.yCoord > start.yCoord) {
        updateRaytraceResult(sculpture, x, y, z, ForgeDirection.DOWN.ordinal(),
                hit.subtract(start).lengthVector());
      } else {
        updateRaytraceResult(sculpture, x, y - 1, z, ForgeDirection.UP.ordinal(),
                hit.subtract(start).lengthVector());
      }
    }

    for (int z = 0; z <= 8; z++) {

      Vec3 hit = start.getIntermediateWithZValue(end, z / 8f);
      if (hit == null) {
        continue;
      }

      if (hit.xCoord < 0) {
        continue;
      }
      if (hit.yCoord < 0) {
        continue;
      }
      int x = (int) (hit.xCoord * 8);
      int y = (int) (hit.yCoord * 8);

      if (end.zCoord > start.zCoord) {
        updateRaytraceResult(sculpture, x, y, z, ForgeDirection.NORTH.ordinal(),
                hit.subtract(start).lengthVector());
      } else {
        updateRaytraceResult(sculpture, x, y, z - 1, ForgeDirection.SOUTH.ordinal(),
                hit.subtract(start).lengthVector());
      }
    }

    return xyzf;
  }

  private static void updateRaytraceResult(Sculpture sculpture, int x, int y, int z, int f, double len) {
    if (!Sculpture.contains(x, y, z)) {
      return;
    }
    if (sculpture != null) {
      if (sculpture.getBlockAt(x, y, z, null) == Blocks.air) {
        return;
      }
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

  public static final int PLACE = 1;
  public static final int ALLX = 2;
  public static final int ALLY = 4;
  public static final int ALLZ = 8;
  public static final int DAMAGE = 16;
  public static final int CONSUME = 32;
  public static final int TRANSMUTE = 64;

  public static void setBlockBoundsFromRaytrace(int[] pos, Block block, int type) {
    pos = pos.clone();
    if (hasFlag(type, PLACE)) {
      ForgeDirection dir = ForgeDirection.getOrientation(pos[3]);
      pos[0] += dir.offsetX;
      pos[1] += dir.offsetY;
      pos[2] += dir.offsetZ;
    }

    int x = 0, y = 0, z = 0;

    while (pos[0] < 0) {
      pos[0] += 8;
      x--;
    }
    while (pos[0] > 7) {
      pos[0] -= 8;
      x++;
    }
    while (pos[1] < 0) {
      pos[1] += 8;
      y--;
    }
    while (pos[1] > 7) {
      pos[1] -= 8;
      y++;
    }
    while (pos[2] < 0) {
      pos[2] += 8;
      z--;
    }
    while (pos[2] > 7) {
      pos[2] -= 8;
      z++;
    }

    boolean allx = (type & ALLX) != 0;
    boolean ally = (type & ALLY) != 0;
    boolean allz = (type & ALLZ) != 0;
    block.setBlockBounds(allx ? x + 0 : x + pos[0] / 8f,
            ally ? y + 0 : y + pos[1] / 8f,
            allz ? z + 0 : z + pos[2] / 8f,
            allx ? x + 1 : x + (pos[0] + 1) / 8f,
            ally ? y + 1 : y + (pos[1] + 1) / 8f,
            allz ? z + 1 : z + (pos[2] + 1) / 8f);
  }

  public static boolean validOperation(World worldObj, int x, int y, int z,
          int[] pos, int chiselFlags) {

    pos = pos.clone();
    if (hasFlag(chiselFlags, PLACE)) {
      ForgeDirection dir = ForgeDirection.getOrientation(pos[3]);
      pos[0] += dir.offsetX;
      pos[1] += dir.offsetY;
      pos[2] += dir.offsetZ;
    }

    while (pos[0] < 0) {
      pos[0] += 8;
      x--;
    }
    while (pos[0] > 7) {
      pos[0] -= 8;
      x++;
    }
    while (pos[1] < 0) {
      pos[1] += 8;
      y--;
    }
    while (pos[1] > 7) {
      pos[1] -= 8;
      y++;
    }
    while (pos[2] < 0) {
      pos[2] += 8;
      z--;
    }
    while (pos[2] > 7) {
      pos[2] -= 8;
      z++;
    }

    Block b = worldObj.getBlock(x, y, z);
    if (hasFlag(chiselFlags, PLACE)) {
      if (b == Blocks.air) {
        return true;
      }
      if (b == ProxyBase.blockSculpture.getBlock()) {
        return true;
      }
      return false;
    } else {
      int meta = worldObj.getBlockMetadata(x, y, z);
      if (b == Blocks.air) {
        return false;
      }
      if (b == ProxyBase.blockSculpture.getBlock()) {
        return true;
      }
      if (sculptable(b, meta)) {
        return true;
      }
      return false;
    }
  }

  private static boolean hasFlag(int flags, int mask) {
    return (flags & mask) > 0;
  }

  public static boolean applyOperation(World w, int x, int y, int z,
          int[] pos, int flags, Block editBlock, int editMeta) {

    if (hasFlag(flags, TRANSMUTE)) {
      SculptureEntity se = Utils.getTE(w, x, y, z);
      int index = se.sculpture.getIndex(pos[0], pos[1], pos[2]);
      se.sculpture.block_ids[index] = Block.getIdFromBlock(editBlock);
      se.sculpture.block_metas[index] = (byte) editMeta;

      if (se.sculpture.isEmpty()) {
        w.setBlock(x, y, z, Blocks.air);
      }
      if (w.isRemote) {
        se.getRender().changed = true;
      } else {
        w.markBlockForUpdate(x, y, z);
      }

      return true;
    }

    pos = pos.clone();
    if (hasFlag(flags, PLACE)) {
      ForgeDirection dir = ForgeDirection.getOrientation(pos[3]);
      pos[0] += dir.offsetX;
      pos[1] += dir.offsetY;
      pos[2] += dir.offsetZ;
    }

    while (pos[0] < 0) {
      pos[0] += 8;
      x--;
    }
    while (pos[0] > 7) {
      pos[0] -= 8;
      x++;
    }
    while (pos[1] < 0) {
      pos[1] += 8;
      y--;
    }
    while (pos[1] > 7) {
      pos[1] -= 8;
      y++;
    }
    while (pos[2] < 0) {
      pos[2] += 8;
      z--;
    }
    while (pos[2] > 7) {
      pos[2] -= 8;
      z++;
    }

    int[] minmax = new int[6];
    boolean allx = hasFlag(flags, ALLX);
    boolean ally = hasFlag(flags, ALLY);
    boolean allz = hasFlag(flags, ALLZ);
    minmax[0] = allx ? 0 : pos[0];
    minmax[1] = ally ? 0 : pos[1];
    minmax[2] = allz ? 0 : pos[2];
    minmax[3] = allx ? 8 : (pos[0] + 1);
    minmax[4] = ally ? 8 : (pos[1] + 1);
    minmax[5] = allz ? 8 : (pos[2] + 1);

    int blocks = editSubBlock(w, minmax, x, y, z, editBlock, (byte) editMeta);

    return blocks > 0;
  }

  public static int getLookingAxis(EntityPlayer ep) {
    Vec3 vec = ep.getLookVec();
    double x = Math.abs(vec.xCoord);
    double y = Math.abs(vec.yCoord);
    double z = Math.abs(vec.zCoord);
    if (x >= y && x >= z) {
      return 0;
    }
    if (y >= x && y >= z) {
      return 1;
    }
    if (z >= x && z >= y) {
      return 2;
    }
    return 0;
  }
}
