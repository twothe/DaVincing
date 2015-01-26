package hx.minepainter.item;

import hx.minepainter.ModMinePainter;
import hx.minepainter.painting.PaintingEntity;
import hx.minepainter.painting.PaintingPlacement;
import hx.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class CanvasItem
        extends Item {

  public CanvasItem() {
    setCreativeTab(ModMinePainter.tabMinePainter);
    setFull3D();
    setUnlocalizedName("canvas");
    setTextureName("minepainter:canvas");
  }

  public boolean func_77651_p() {
    return true;
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
    w.setBlock(_x, _y, _z, ModMinePainter.painting.block, pp.ordinal(), 3);
    PaintingEntity pe = (PaintingEntity) Utils.getTE(w, _x, _y, _z);
    pe.readFromNBTToImage(is.getTagCompound());
    if (!ep.capabilities.isCreativeMode) {
      is.stackSize -= 1;
    }
    return true;
  }
}
