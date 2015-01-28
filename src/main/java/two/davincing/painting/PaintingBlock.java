package two.davincing.painting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import two.davincing.DaVincing;
import two.davincing.utils.Utils;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class PaintingBlock extends BlockContainer {

  public boolean ignore_bounds_on_state;

  public PaintingBlock() {
    super(Material.cloth);
    this.setBlockTextureName("minepainter:palette");
    this.setHardness(0.2f);
    this.setBlockName("painting");
  }

  @Override
  public void registerBlockIcons(IIconRegister register) {
  }

  @Override
  public TileEntity createNewTileEntity(World var1, int var2) {
    return new PaintingEntity();
  }

  @Override
  public boolean isOpaqueCube() {
    return false;
  }

  @Override
  public boolean renderAsNormalBlock() {
    return false;
  }

  @Override
  public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
    return null;
  }

  @Override
  public void setBlockBoundsBasedOnState(IBlockAccess iba, int x, int y, int z) {
    if (ignore_bounds_on_state) {
      return;
    }
    PaintingPlacement placement = PaintingPlacement.of(iba.getBlockMetadata(x, y, z));
    placement.setBlockBounds(this);
  }

  @Override
  public void setBlockBoundsForItemRender() {
    this.setBlockBounds(0, 0, 0, 1, 1, 1);
  }

  @Override
  public int getRenderType() {
    return -1;
  }

  @Override
  public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
    ItemStack is = new ItemStack(DaVincing.canvas.item);
    NBTTagCompound nbt = new NBTTagCompound();
    PaintingEntity pe = Utils.getTE(world, x, y, z);

    pe.writeImageToNBT(nbt);
    is.setTagCompound(nbt);
    return is;
  }

  @Override
  public void onNeighborBlockChange(World w, int x, int y, int z, Block block) {
    PaintingPlacement pp = PaintingPlacement.of(w.getBlockMetadata(x, y, z));
    int tx = x - pp.normal.offsetX;
    int ty = y - pp.normal.offsetY;
    int tz = z - pp.normal.offsetZ;
    if (w.getBlock(tx, ty, tz).getMaterial().isSolid()) {
      return;
    }

    w.setBlockToAir(x, y, z);
  }

  protected ItemStack createStackedBlock(int p_149644_1_) {
    return null;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int side, int meta) {
    return Blocks.wool.getIcon(side, 0);
  }

  public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
    return null;
  }

  @Override
  public void breakBlock(World w, int x, int y, int z, Block b, int meta) {

    ItemStack is = new ItemStack(DaVincing.canvas.item);
    NBTTagCompound nbt = new NBTTagCompound();
    PaintingEntity pe = Utils.getTE(w, x, y, z);

    pe.writeImageToNBT(nbt);
    is.setTagCompound(nbt);
    this.dropBlockAsItem(w, x, y, z, is);

    super.breakBlock(w, x, y, z, b, meta);
  }
}
