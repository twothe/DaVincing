package two.davincing.item;

import two.davincing.DaVincing;
import two.davincing.sculpture.Operations;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class TransmuterItem extends ChiselItem {

  public TransmuterItem() {
    this.setCreativeTab(DaVincing.tabDaVincing);
    this.setUnlocalizedName("transmuter");
    this.setTextureName("minepainter:transmuter");
    this.setMaxStackSize(1);
  }

  @Override
  public boolean getShareTag() {
    return true;
  }

  public int getBlockAndMeta(ItemStack is) {
    NBTTagCompound nbt = is.getTagCompound();
    if (nbt == null) {
      return 0;
    }
    return nbt.getInteger("TBID");
  }

  public void setBlockAndMeta(ItemStack is, int tbid) {
    NBTTagCompound nbt = is.getTagCompound();
    if (nbt == null) {
      is.setTagCompound(nbt = new NBTTagCompound());
    }
    nbt.setInteger("TBID", tbid);
  }

  public void addInformation(ItemStack is, EntityPlayer ep, List list, boolean thing) {
    int tbid = this.getBlockAndMeta(is);
    Block block = Block.getBlockById(tbid >>> 4);
    int meta = tbid & 0xf;
    if (block == null) {
      return;
    }
    if (block == Blocks.air) {
      return;
    }
    list.add(new ItemStack(block, 1, meta).getDisplayName());
  }

  public boolean onItemUse(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs) {
    if (w.getBlock(x, y, z) == DaVincing.sculpture.block) {
      return super.onItemUse(is, ep, w, x, y, z, face, xs, ys, zs);
    }

    MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(w, ep, true);
    x = mop.blockX;
    y = mop.blockY;
    z = mop.blockZ;
    Block that = w.getBlock(x, y, z);
    if (that == null) {
      return false;
    }
    int meta = w.getBlockMetadata(x, y, z);
    this.setBlockAndMeta(is, (Block.getIdFromBlock(that) << 4) + meta);
    return true;
  }

  @Override
  public Block getEditBlock(ItemStack is) {
    return Block.getBlockById(getBlockAndMeta(is) >>> 4);
  }

  @Override
  public int getEditMeta(ItemStack is) {
    return getBlockAndMeta(is) & 0xf;
  }

  @Override
  public int getChiselFlags(EntityPlayer ep) {
    return Operations.TRANSMUTE;
  }
}
