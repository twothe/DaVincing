package hx.minepainter.sculpture;

import hx.utils.Debug;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

public class Sculpture {

  byte[][] layers;
  int[] block_ids;
  byte[] block_metas;
  int[] usage_count;
  Rotation r = new Rotation();

  public Sculpture() {
    normalize();
  }

  public Rotation getRotation() {
    return this.r;
  }

  public void write(NBTTagCompound nbt) {
    nbt.setIntArray("block_ids", this.block_ids);
    nbt.setByteArray("block_metas", this.block_metas);
    for (int i = 0; i < this.layers.length; i++) {
      nbt.setByteArray("layer" + i, this.layers[i]);
    }
    nbt.setByteArray("rotation", this.r.r);
  }

  public void read(NBTTagCompound nbt) {
    this.block_ids = nbt.getIntArray("block_ids");
    this.block_metas = nbt.getByteArray("block_metas");
    this.r.r = nbt.getByteArray("rotation");
    this.layers = new byte[log(this.block_ids.length)][];
    for (int i = 0; i < this.layers.length; i++) {
      this.layers[i] = nbt.getByteArray("layer" + i);
    }
    normalize();
  }

  public Block getBlockAt(int x, int y, int z, BlockSlice slice) {
    if (!contains(x, y, z)) {
      return slice.getBlock(x, y, z);
    }
    int index = getIndex(x, y, z);

    return Block.getBlockById(this.block_ids[index]);
  }

  public int getMetaAt(int x, int y, int z, BlockSlice slice) {
    if (!contains(x, y, z)) {
      return slice.getBlockMetadata(x, y, z);
    }
    int index = getIndex(x, y, z);

    return this.block_metas[index];
  }

  public boolean setBlockAt(int x, int y, int z, Block block, byte meta) {
    if (!contains(x, y, z)) {
      return false;
    }
    int index = findIndexForBlock(Block.getIdFromBlock(block), meta);
    if (index < 0) {
      grow();
      index = this.block_ids.length / 2;
      this.block_ids[index] = Block.getIdFromBlock(block);
      this.block_metas[index] = meta;
    }
    setIndex(x, y, z, index);
    return true;
  }

  public boolean isEmpty() {
    int s = 0;
    for (int i = 0; i < this.block_ids.length; i++) {
      if (this.block_ids[i] == 0) {
        s += this.usage_count[i];
      }
    }
    return s == 512;
  }

  public boolean isFull() {
    int s = 0;
    for (int i = 0; i < this.block_ids.length; i++) {
      if (this.block_ids[i] == 0) {
        s += this.usage_count[i];
      }
    }
    return s == 0;
  }

  private int findIndexForBlock(int blockID, byte meta) {
    for (int i = 0; i < this.block_ids.length; i++) {
      if ((this.block_ids[i] == blockID) && (this.block_metas[i] == meta)) {
        return i;
      }
      if (this.usage_count[i] == 0) {
        this.block_ids[i] = blockID;
        this.block_metas[i] = meta;
        return i;
      }
    }
    return -1;
  }

  int getIndex(int x, int y, int z) {
    this.r.apply(x, y, z);
    int index = 0;
    for (int l = 0; l < this.layers.length; l++) {
      if ((this.layers[l][(this.r.x * 8 + this.r.y)] & 1 << this.r.z) > 0) {
        index |= 1 << l;
      }
    }
    return index;
  }

  void setIndex(int x, int y, int z, int index) {
    int prev = getIndex(x, y, z);
    this.usage_count[prev] -= 1;
    this.usage_count[index] += 1;

    this.r.apply(x, y, z);
    for (int l = 0; l < this.layers.length; l++) {
      if ((index & 1 << l) > 0) {
        int tmp89_88 = (this.r.x * 8 + this.r.y);
        byte[] tmp89_70 = this.layers[l];
        tmp89_70[tmp89_88] = ((byte) (tmp89_70[tmp89_88] | 1 << this.r.z));
      } else {
        int tmp131_130 = (this.r.x * 8 + this.r.y);
        byte[] tmp131_112 = this.layers[l];
        tmp131_112[tmp131_130] = ((byte) (tmp131_112[tmp131_130] & (1 << this.r.z ^ 0xFFFFFFFF)));
      }
    }
  }

  public static boolean contains(int x, int y, int z) {
    return (x >= 0) && (y >= 0) && (z >= 0) && (x < 8) && (y < 8) && (z < 8);
  }

  private boolean check() {
    if (this.block_ids == null) {
      return false;
    }
    if (this.block_metas == null) {
      return false;
    }
    if (this.layers == null) {
      return false;
    }
    if (this.r.r == null) {
      return false;
    }
    for (int i = 0; i < this.layers.length; i++) {
      if (this.layers[i] == null) {
        Debug.log(new String[]{"layer " + i + " is null!"});
        return false;
      }
      if (this.layers[i].length != 64) {
        Debug.log(new String[]{"layer " + i + " is " + this.layers[i].length + " long!"});
        return false;
      }
    }
    if (this.block_ids.length != 1 << this.layers.length) {
      return false;
    }
    if (this.block_ids.length != this.block_metas.length) {
      return false;
    }
    if (this.usage_count.length != this.block_ids.length) {
      this.usage_count = new int[this.block_ids.length];
    }
    return true;
  }

  private void grow() {
    byte[][] nlayers = new byte[this.layers.length + 1][];
    System.arraycopy(this.layers, 0, nlayers, 0, this.layers.length);
    nlayers[this.layers.length] = new byte[64];
    this.layers = nlayers;

    int[] ids = new int[this.block_ids.length * 2];
    System.arraycopy(this.block_ids, 0, ids, 0, this.block_ids.length);
    this.block_ids = ids;

    byte[] metas = new byte[this.block_metas.length * 2];
    System.arraycopy(this.block_metas, 0, metas, 0, this.block_metas.length);
    this.block_metas = metas;

    int[] usage = new int[this.usage_count.length * 2];
    System.arraycopy(this.usage_count, 0, usage, 0, this.usage_count.length);
    this.usage_count = usage;
  }

  private void normalize() {
    if (!check()) {
      this.layers = new byte[1][64];
      this.block_ids = new int[2];
      this.block_metas = new byte[2];
      this.usage_count = new int[2];
    }
    for (int i = 0; i < this.usage_count.length; i++) {
      this.usage_count[i] = 0;
    }
    for (int i = 0; i < 512; i++) {
      int index = getIndex(i >> 6, i >> 3 & 0x7, i & 0x7);
      this.usage_count[index] += 1;
    }
  }

  private int log(int num) {
    int i = 0;
    while (num > 1) {
      num >>= 1;
      i++;
    }
    return i;
  }

  public int getLight() {
    int light = 0;
    int current;
    for (int i = 0; i < this.usage_count.length; i++) {
      current = Block.getBlockById(this.block_ids[i]).getLightValue();
      if (current > light) {
        light = current;
      }
    }
    return light;
  }
}
