package hx.minepainter.sculpture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

@SideOnly(Side.CLIENT)
public class BlockSlice
        implements IBlockAccess {

  IBlockAccess iba;
  int x;
  int y;
  int z;
  Sculpture sculpture;
  int brightness;
  private static BlockSlice instance = new BlockSlice();

  public static BlockSlice at(IBlockAccess iba, int x, int y, int z) {
    instance.iba = iba;
    instance.x = x;
    instance.y = y;
    instance.z = z;

    TileEntity te = iba.func_147438_o(x, y, z);
    if ((te != null) && ((te instanceof SculptureEntity))) {
      instance.sculpture = ((SculptureEntity) te).sculpture;
    } else {
      instance.sculpture = null;
    }
    return instance;
  }

  public static BlockSlice of(Sculpture sculpture, int brightness) {
    instance.iba = null;
    instance.sculpture = sculpture;
    instance.brightness = brightness;
    return instance;
  }

  public static void clear() {
    instance.iba = null;
  }

  public Block getBlock(int x, int y, int z) {
    if ((this.sculpture != null) && (Sculpture.contains(x, y, z))) {
      return this.sculpture.getBlockAt(x, y, z, this);
    }
    if (this.iba == null) {
      return Blocks.field_150350_a;
    }
    return this.iba.getBlock(this.x + cap(x), this.y + cap(y), this.z + cap(z));
  }

  public TileEntity func_147438_o(int x, int y, int z) {
    if (this.iba == null) {
      return null;
    }
    return this.iba.func_147438_o(this.x + cap(x), this.y + cap(y), this.z + cap(z));
  }

  @SideOnly(Side.CLIENT)
  public int func_72802_i(int x, int y, int z, int var4) {
    if (this.iba == null) {
      return this.brightness;
    }
    return this.iba.func_72802_i(this.x + cap(x), this.y + cap(y), this.z + cap(z), var4);
  }

  public int getBlockMetadata(int x, int y, int z) {
    if ((this.sculpture != null) && (Sculpture.contains(x, y, z))) {
      return this.sculpture.getMetaAt(x, y, z, this);
    }
    if (this.iba == null) {
      return 0;
    }
    return this.iba.getBlockMetadata(this.x + cap(x), this.y + cap(y), this.z + cap(z));
  }

  public boolean func_147437_c(int x, int y, int z) {
    if ((this.sculpture != null) && (Sculpture.contains(x, y, z))) {
      return this.sculpture.getBlockAt(x, y, z, this) == Blocks.field_150350_a;
    }
    if (this.iba == null) {
      return true;
    }
    return this.iba.func_147437_c(this.x + cap(x), this.y + cap(y), this.z + cap(z));
  }

  @SideOnly(Side.CLIENT)
  public BiomeGenBase func_72807_a(int var1, int var2) {
    return null;
  }

  @SideOnly(Side.CLIENT)
  public int func_72800_K() {
    if (this.iba == null) {
      return 256;
    }
    return this.iba.func_72800_K();
  }

  @SideOnly(Side.CLIENT)
  public boolean func_72806_N() {
    if (this.iba == null) {
      return false;
    }
    return this.iba.func_72806_N();
  }

  public Vec3Pool func_82732_R() {
    if (this.iba == null) {
      return null;
    }
    return this.iba.func_82732_R();
  }

  public int func_72879_k(int x, int y, int z, int var4) {
    if (this.iba == null) {
      return 0;
    }
    return this.iba.func_72879_k(this.x + cap(x), this.y + cap(y), this.z + cap(z), var4);
  }

  public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
    if (this.iba == null) {
      return false;
    }
    return this.iba.isSideSolid(this.x + cap(x), this.y + cap(y), this.z + cap(z), side, _default);
  }

  private static int cap(int original) {
    return original >= 0 ? 0 : original > 7 ? original - 7 : original;
  }
}
