package two.davincing.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import two.davincing.DaVincing;
import two.davincing.sculpture.Sculpture;
import two.davincing.sculpture.SculptureBlock;
import two.davincing.sculpture.SculptureEntity;
import two.davincing.utils.Utils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import two.davincing.ProxyBase;

public class DroppedSculptureItem extends Item {

  public DroppedSculptureItem() {
    super();
    this.setUnlocalizedName("dropped_sculpture");
  }

  public void readTo(ItemStack is, Sculpture sculpture) {
    if (is.hasTagCompound()) {
      sculpture.read(is.getTagCompound());
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void registerIcons(IIconRegister r) {
  }

  @Override
  public boolean getShareTag() {
    return true;
  }

  @Override
  public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
    return armorType == 0;
  }

  @Override
  public boolean onItemUse(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs) {

    if (!w.getBlock(x, y, z).getMaterial().isSolid()) {
      return false;
    }

    ForgeDirection dir = ForgeDirection.getOrientation(face);
    int _x = x + dir.offsetX;
    int _y = y + dir.offsetY;
    int _z = z + dir.offsetZ;

    if (!w.isAirBlock(_x, _y, _z)) {
      return false;
    }
    if (!ep.canPlayerEdit(x, y, z, face, is)) {
      return false;
    }

    w.setBlock(_x, _y, _z, ProxyBase.blockSculpture.getBlock());
    SculptureEntity se = Utils.getTE(w, _x, _y, _z);
    se.sculpture().read(is.getTagCompound());
    SculptureBlock.applyPlayerRotation(se.sculpture().getRotation(), ep, false);

    if (!ep.capabilities.isCreativeMode) {
      is.stackSize--;
    }

    return true;
  }

}
