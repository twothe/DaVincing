package two.davincing.item;

import two.davincing.DaVincing;
import two.davincing.painting.PaintingEntity;
import two.davincing.painting.PaintingPlacement;
import two.davincing.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import two.davincing.ProxyBase;

public class CanvasItem extends Item {

  public CanvasItem() {
    super();
    this.setCreativeTab(DaVincing.tabDaVincing);
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

    PaintingPlacement pp = PaintingPlacement.of(ep.getLookVec(), face);
    w.setBlock(_x, _y, _z, ProxyBase.blockPainting.getBlock(), pp.ordinal(), 3);
    PaintingEntity pe = Utils.getTE(w, _x, _y, _z);
    pe.readFromNBTToImage(is.getTagCompound());

    if (!ep.capabilities.isCreativeMode) {
      is.stackSize--;
    }

    return true;
  }

}
