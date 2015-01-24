package hx.minepainter.painting;

import hx.minepainter.ModMinePainter;
import hx.utils.ItemLoader;
import hx.utils.Utils;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PaintingBlock extends BlockContainer {

  public boolean ignore_bounds_on_state;

  public PaintingBlock() {
    super(Material.cloth);
    setBlockTextureName("minepainter:palette");
    setHardness(0.2F);
    setBlockName("painting");
  }

  public void func_149651_a(IIconRegister register) {
  }

  public TileEntity func_149915_a(World var1, int var2) {
    return new PaintingEntity();
  }

  public boolean func_149662_c() {
    return false;
  }

  public boolean func_149686_d() {
    return false;
  }

  public AxisAlignedBB func_149668_a(World par1World, int par2, int par3, int par4) {
    return null;
  }

  public void func_149719_a(IBlockAccess iba, int x, int y, int z) {
    if (this.ignore_bounds_on_state) {
      return;
    }
    PaintingPlacement placement = PaintingPlacement.of(iba.getBlockMetadata(x, y, z));
    placement.setBlockBounds(this);
  }

  public void func_149683_g() {
    func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }

  public int func_149645_b() {
    return -1;
  }

  public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
    ItemStack is = new ItemStack(ModMinePainter.canvas.item);
    NBTTagCompound nbt = new NBTTagCompound();
    PaintingEntity pe = (PaintingEntity) Utils.getTE(world, x, y, z);

    pe.writeImageToNBT(nbt);
    is.writeToNBT(nbt);
    return is;
  }

  public void func_149695_a(World w, int x, int y, int z, Block block) {
    PaintingPlacement pp = PaintingPlacement.of(w.getBlockMetadata(x, y, z));
    int tx = x - pp.normal.offsetX;
    int ty = y - pp.normal.offsetY;
    int tz = z - pp.normal.offsetZ;
    if (w.getBlock(tx, ty, tz).getMaterial().func_76220_a()) {
      return;
    }
    w.func_147468_f(x, y, z);
  }

  protected ItemStack func_149644_j(int p_149644_1_) {
    return null;
  }

  public Item func_149650_a(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
    return null;
  }

  public void func_149749_a(World w, int x, int y, int z, Block b, int meta) {
    ItemStack is = new ItemStack(ModMinePainter.canvas.item);
    NBTTagCompound nbt = new NBTTagCompound();
    PaintingEntity pe = (PaintingEntity) Utils.getTE(w, x, y, z);

    pe.writeImageToNBT(nbt);
    is.writeToNBT(nbt);
    func_149642_a(w, x, y, z, is);

    super.func_149749_a(w, x, y, z, b, meta);
  }
}
