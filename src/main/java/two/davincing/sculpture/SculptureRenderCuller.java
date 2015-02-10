package two.davincing.sculpture;

import net.minecraft.block.Block;

public class SculptureRenderCuller {

  public static final SculptureRenderCuller culler = new SculptureRenderCuller();

  // 0 = merge x, 1 = merge y,  2 = merge z, 3 = unmerged
  private static final int TYPE_X = 0x0;
  private static final int TYPE_Y = 0x1;
  private static final int TYPE_Z = 0x2;
  private static final int TYPE_S = 0x3;
  private static final int BIT_XLEN = 0x1 << 2;
  private static final int BIT_YLEN = 0x1 << 5;
  private static final int BIT_ZLEN = 0x1 << 8;
  private static final int BIT_INDEX = 0x1 << 11;


  public static boolean isMergeable(final Block block) {
    return (block.getLightOpacity() >= 255);
  }

  private static boolean isMergeable(int id) {
    return isMergeable(Block.getBlockById(id));
  }

  public int[][][] getMergeMap(final Sculpture sculpture) {
    final int[][][] result = new int[8][8][8];
    // cull z direction
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        int prev = -1;
        int source = -1;
        for (int k = 0; k < 8; k++) {
          int now = sculpture.getIndex(i, j, k);
          if (now == prev && isMergeable(sculpture.block_ids[now])) {
            result[i][j][k] = TYPE_Z;
            result[i][j][source] += BIT_ZLEN;
          } else {
            source = k;
            result[i][j][source] = TYPE_S | (now * BIT_INDEX);
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

          if ((result[i][j][k] & 3) != TYPE_S) {
            prev = -1;
            source = -1;
            continue;
          }

          int now = result[i][j][k];
          if (now == prev && isMergeable(sculpture.block_ids[now / BIT_INDEX])) {
            result[i][j][k] = TYPE_Y;
            result[i][source][k] += BIT_YLEN;
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

          if ((result[i][j][k] & 3) != TYPE_S) {
            prev = -1;
            source = -1;
            continue;
          }

          int now = result[i][j][k];
          if (now == prev && isMergeable(sculpture.block_ids[now / BIT_INDEX])) {
            result[i][j][k] = TYPE_X;
            result[source][j][k] += BIT_XLEN;
          } else {
            source = i;
          }
          prev = now;
        }

      }
    }

    return result;
  }
}
