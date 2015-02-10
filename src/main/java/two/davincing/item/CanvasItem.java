package two.davincing.item;

import java.awt.image.BufferedImage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import two.davincing.DaVincing;
import two.davincing.ProxyBase;
import two.davincing.painting.PaintingEntity;
import two.davincing.painting.PaintingPlacement;
import two.davincing.utils.Utils;

public class CanvasItem extends ItemBase {

  public CanvasItem() {
    super();
    this.setFull3D();
    this.setUnlocalizedName("canvas");
    this.setTextureName("minepainter:canvas");
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
  public boolean onItemUse(final ItemStack itemStack, final EntityPlayer player, final World world, int x, int y, int z, int face, float xs, float ys, float zs) {
    if (world.getBlock(x, y, z).getMaterial().isSolid()) {
      final ForgeDirection dirFacing = ForgeDirection.getOrientation(face); // the direction the player is looking
      int inFrontX = x + dirFacing.offsetX;
      int inFrontY = y + dirFacing.offsetY;
      int inFrontZ = z + dirFacing.offsetZ;

      if (world.isAirBlock(inFrontX, inFrontY, inFrontZ) && player.canPlayerEdit(x, y, z, face, itemStack)) {
        final PaintingPlacement pp = PaintingPlacement.of(player.getLookVec(), face);
        world.setBlock(inFrontX, inFrontY, inFrontZ, ProxyBase.blockPainting.getBlock(), pp.ordinal(), 3);
        final BufferedImage paintingImage = PaintingEntity.getPaintingFromItem(itemStack);
        final TileEntity tileEntity = world.getTileEntity(inFrontX, inFrontY, inFrontZ);
        if (tileEntity instanceof PaintingEntity) {
          ((PaintingEntity) tileEntity).getTexture().setRGB(paintingImage);
          if ((player.capabilities.isCreativeMode == false) || (player.capabilities.isCreativeMode && player.isSneaking())) {
            --itemStack.stackSize;
          }
        } else {
          DaVincing.log.warn("[CanvasItem.onItemUse] failed: expected PaintingEntity at %d, %d, %d, but got %s", x, y, z, Utils.getClassName(tileEntity));
        }
        return true;
      } else {
        return false;
      }
    } else {
      return false; // canvas cannot be placed on non-solid blocks
    }
  }

}
