package two.davincing.sculpture;

import net.minecraft.block.Block;

public class SculptureRenderCuller {

  public static SculptureRenderCuller culler = new SculptureRenderCuller();

  // 0 = merge x, 1 = merge y,  2 = merge z, 3 = unmerged
  private static final int TYPE_X = 0x0;
  private static final int TYPE_Y = 0x1;
  private static final int TYPE_Z = 0x2;
  private static final int TYPE_S = 0x3;
  private static final int BIT_XLEN = 0x1 << 2;
  private static final int BIT_YLEN = 0x1 << 5;
  private static final int BIT_ZLEN = 0x1 << 8;
  private static final int BIT_INDEX = 0x1 << 11;

  private int[][][] mergeMap = new int[8][8][8];

  public static boolean isMergeable(Block b) {
    if (b.getLightOpacity() < 255) {
      return false;
    }
    return true;
  }

  private static boolean isMergeable(int id) {
    return isMergeable(Block.getBlockById(id));
  }

  public int[][][] getMergeMap(Sculpture sculpture) {

    // cull z direction
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        int prev = -1;
        int source = -1;
        for (int k = 0; k < 8; k++) {
          int now = sculpture.getIndex(i, j, k);
          if (now == prev && isMergeable(sculpture.block_ids[now])) {
            mergeMap[i][j][k] = TYPE_Z;
            mergeMap[i][j][source] += BIT_ZLEN;
          } else {
            source = k;
            mergeMap[i][j][source] = TYPE_S | (now * BIT_INDEX);
          }
          prev = now;
        }
      }
    }

    // cull y direction
    for (int i = 0; i < 8; i++) {
      for (int k = 0; k < 8; k++) {
        int prev = -1;
        int source = -1;
        for (int j = 0; j < 8; j++) {

          if ((mergeMap[i][j][k] & 3) != TYPE_S) {
            prev = -1;
            source = -1;
            continue;
          }

          int now = mergeMap[i][j][k];
          if (now == prev && isMergeable(sculpture.block_ids[now / BIT_INDEX])) {
            mergeMap[i][j][k] = TYPE_Y;
            mergeMap[i][source][k] += BIT_YLEN;
          } else {
            source = j;
          }
          prev = now;
        }
      }
    }

    for (int j = 0; j < 8; j++) {
      for (int k = 0; k < 8; k++) {
        int prev = -1;
        int source = -1;
        for (int i = 0; i < 8; i++) {

          if ((mergeMap[i][j][k] & 3) != TYPE_S) {
            prev = -1;
            source = -1;
            continue;
          }

          int now = mergeMap[i][j][k];
          if (now == prev && isMergeable(sculpture.block_ids[now / BIT_INDEX])) {
            mergeMap[i][j][k] = TYPE_X;
            mergeMap[source][j][k] += BIT_XLEN;
          } else {
            source = i;
          }
          prev = now;
        }

      }
    }

    return mergeMap;
  }
}
