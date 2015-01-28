package hx.minepainter.sculpture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hx.utils.Debug;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;

public class Sculpture {

	// layers of data.
  // combine bits at [x*8 + y] & (1<<z) for index
  // possible indexes : 2^#layers
  // start with 1 layer
  byte[][] layers;

	// block type index.
  // starts with 0 - air
  int[] block_ids;
  byte[] block_metas;
  int[] usage_count;

  Rotation r = new Rotation();

  public Sculpture() {
    normalize();
  }

  public Rotation getRotation() {
    return r;
  }

  public void write(NBTTagCompound nbt) {
    nbt.setIntArray("block_ids", block_ids);
    nbt.setByteArray("block_metas", block_metas);
    for (int i = 0; i < layers.length; i++) {
      nbt.setByteArray("layer" + i, layers[i]);
    }
    nbt.setByteArray("rotation", r.r);
  }

  public void read(NBTTagCompound nbt) {
    block_ids = nbt.getIntArray("block_ids");
    block_metas = nbt.getByteArray("block_metas");
    r.r = nbt.getByteArray("rotation");
    layers = new byte[log(block_ids.length)][];
    for (int i = 0; i < layers.length; i++) {
      layers[i] = nbt.getByteArray("layer" + i);
    }

    normalize();
  }

  public Block getBlockAt(int x, int y, int z, BlockSlice slice) {
    if (!contains(x, y, z)) {
      return slice.getBlock(x, y, z);
    }

    int index = getIndex(x, y, z);

    return Block.getBlockById(block_ids[index]);
  }

  public int getMetaAt(int x, int y, int z, BlockSlice slice) {
    if (!contains(x, y, z)) {
      return slice.getBlockMetadata(x, y, z);
    }

    int index = getIndex(x, y, z);

    return block_metas[index];
  }

  public boolean setBlockAt(int x, int y, int z, Block block, byte meta) {
    if (!contains(x, y, z)) {
      return false;
    }

    int index = findIndexForBlock(Block.getIdFromBlock(block), meta);
    if (index < 0) {
      grow();
      index = block_ids.length / 2;
      block_ids[index] = Block.getIdFromBlock(block);
      block_metas[index] = meta;
    }

    setIndex(x, y, z, index);
    return true;
  }

  public boolean isEmpty() {
    int s = 0;
    for (int i = 0; i < block_ids.length; i++) {
      if (block_ids[i] == 0) {
        s += usage_count[i];
      }
    }
    return s == 512;
  }

  public boolean isFull() {
    int s = 0;
    for (int i = 0; i < block_ids.length; i++) {
      if (block_ids[i] == 0) {
        s += usage_count[i];
      }
    }
    return s == 0;
  }

  private int findIndexForBlock(int blockID, byte meta) {
    for (int i = 0; i < block_ids.length; i++) {
      if (block_ids[i] == blockID && block_metas[i] == meta) {
        return i;
      }
    }
    for (int i = 0; i < block_ids.length; i++) {
      if (usage_count[i] == 0) {
        block_ids[i] = blockID;
        block_metas[i] = meta;
        return i;
      }
    }
    return -1;
  }

  int getIndex(int x, int y, int z) {
    r.apply(x, y, z);
    int index = 0;
    for (int l = 0; l < layers.length; l++) {
      if ((layers[l][r.x * 8 + r.y] & (1 << r.z)) > 0) {
        index |= (1 << l);
      }
    }
    return index;
  }

  void setIndex(int x, int y, int z, int index) {

    int prev = getIndex(x, y, z);
    usage_count[prev]--;
    usage_count[index]++;

    r.apply(x, y, z);
    for (int l = 0; l < layers.length; l++) {
      if ((index & (1 << l)) > 0) {
        layers[l][r.x * 8 + r.y] |= (1 << r.z);
      } else {
        layers[l][r.x * 8 + r.y] &= ~(1 << r.z);
      }
    }
  }

  public static boolean contains(int x, int y, int z) {
    return x >= 0 && y >= 0 && z >= 0 && x < 8 && y < 8 && z < 8;
  }

  private boolean check() {
    if (block_ids == null) {
      return false;
    }
    if (block_metas == null) {
      return false;
    }
    if (layers == null) {
      return false;
    }
    if (r.r == null) {
      return false;
    }
    for (int i = 0; i < layers.length; i++) {
      if (layers[i] == null) {
        Debug.log("layer " + i + " is null!");
        return false;
      }
      if (layers[i].length != 64) {
        Debug.log("layer " + i + " is " + layers[i].length + " long!");
        return false;
      }
    }
    if (block_ids.length != (1 << layers.length)) {
      return false;
    }
    if (block_ids.length != block_metas.length) {
      return false;
    }

    if (usage_count.length != block_ids.length) {
      usage_count = new int[block_ids.length];
    }
    return true;
  }

  private void grow() {
    byte[][] nlayers = new byte[layers.length + 1][];
    for (int i = 0; i < layers.length; i++) {
      nlayers[i] = layers[i];
    }
    nlayers[layers.length] = new byte[64];
    layers = nlayers;

    int[] ids = new int[block_ids.length * 2];
    for (int i = 0; i < block_ids.length; i++) {
      ids[i] = block_ids[i];
    }
    block_ids = ids;

    byte[] metas = new byte[block_metas.length * 2];
    for (int i = 0; i < block_metas.length; i++) {
      metas[i] = block_metas[i];
    }
    block_metas = metas;

    int[] usage = new int[usage_count.length * 2];
    for (int i = 0; i < usage_count.length; i++) {
      usage[i] = usage_count[i];
    }
    usage_count = usage;
  }

  private void normalize() {
    if (!check()) {
      layers = new byte[1][64];
      block_ids = new int[2];
      block_metas = new byte[2];
      usage_count = new int[2];
    }
    for (int i = 0; i < usage_count.length; i++) {
      usage_count[i] = 0;
    }
    for (int i = 0; i < 512; i++) {
      int index = getIndex(i >> 6, (i >> 3) & 7, i & 7);
      usage_count[index]++;
    }
  }

  private int log(int num) {
    int i = 0;
    while (num > 1) {
      num = num >> 1;
      i++;
    }
    return i;
  }

  // returns max light value
  public int getLight() {
    int light = 0;
    int current = 0;
    for (int i = 0; i < usage_count.length; i++) {
      if (usage_count[i] <= 0) {
        continue;
      }
      current = Block.getBlockById(block_ids[i]).getLightValue();
      if (current > light) {
        light = current;
      }
    }
    return light;
  }

  public int[][] getBlockSigs() {
    int[][] result = new int[2][usage_count.length];
    int s = 0;
    for (int i = 0; i < usage_count.length; i++) {
      if (Block.getBlockById(block_ids[i]) == Blocks.air || usage_count[i] == 0) {
        continue;
      }
      result[0][s] = (block_ids[i] << 4) + block_metas[i];
      result[1][s] = usage_count[i];
      s++;
    }
    return result;
  }

  @SideOnly(Side.CLIENT)
  public boolean needRenderPass(int pass) {
    for (int i = 0; i < usage_count.length; i++) {
      Block that = Block.getBlockById(block_ids[i]);
      if (that == Blocks.air || usage_count[i] == 0) {
        continue;
      }

      if (that.canRenderInPass(pass)) {
        return true;
      }
    }
    return false;
  }
}
