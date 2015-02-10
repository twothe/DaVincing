package two.davincing.sculpture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import two.davincing.DaVincing;
import two.davincing.ProxyBase;
import two.davincing.item.ItemBase;
import two.davincing.utils.Utils;

public class DroppedSculptureItem extends ItemBase {

  public DroppedSculptureItem() {
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
  public boolean onItemUse(final ItemStack heldItem, final EntityPlayer player, final World world, int x, int y, int z, int face, float xs, float ys, float zs) {
    if (world.getBlock(x, y, z).getMaterial().isSolid()) {
      ForgeDirection dir = ForgeDirection.getOrientation(face);
      int inFrontX = x + dir.offsetX;
      int inFrontY = y + dir.offsetY;
      int inFrontZ = z + dir.offsetZ;

      if (world.isAirBlock(inFrontX, inFrontY, inFrontZ) && player.canPlayerEdit(x, y, z, face, heldItem)) {
        world.setBlock(inFrontX, inFrontY, inFrontZ, ProxyBase.blockSculpture.getBlock());
        final TileEntity tileEntity = world.getTileEntity(inFrontX, inFrontY, inFrontZ);
        if (tileEntity instanceof SculptureEntity) {
          final SculptureEntity se = (SculptureEntity) tileEntity;
          se.sculpture().read(heldItem.getTagCompound());
          SculptureBlock.applyPlayerRotation(se.sculpture().getRotation(), player, false);

          if (!player.capabilities.isCreativeMode) {
            heldItem.stackSize--;
          }
          return true;
        } else {
          DaVincing.log.warn("[PieceRenderer.renderItem] failed: expected SculptureEntity, but got %s", Utils.getClassName(tileEntity));
        }
      }
    }
    return false;
  }
}
