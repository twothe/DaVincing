package hx.minepainter.item;

import hx.minepainter.ModMinePainter;
import hx.minepainter.painting.PaintingEntity;
import hx.minepainter.painting.PaintingPlacement;
import hx.utils.BlockLoader;
import hx.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class CanvasItem
        extends Item {

  public CanvasItem() {
    setCreativeTab(ModMinePainter.tabMinePainter);
    func_77664_n();
    setUnlocalizedName("canvas");
    setTextureName("minepainter:canvas");
  }

  public boolean func_77651_p() {
    return true;
  }

  public boolean func_77648_a(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs) {
    if (!w.getBlock(x, y, z).getMaterial().func_76220_a()) {
      return false;
    }
    ForgeDirection dir = ForgeDirection.getOrientation(face);
    int _x = x + dir.offsetX;
    int _y = y + dir.offsetY;
    int _z = z + dir.offsetZ;
    if (!w.isAirBlock(_x, _y, _z)) {
      return false;
    }
    if (!ep.func_82247_a(x, y, z, face, is)) {
      return false;
    }
    PaintingPlacement pp = PaintingPlacement.of(ep.getLook(), face);
    w.func_147465_d(_x, _y, _z, ModMinePainter.painting.block, pp.ordinal(), 3);
    PaintingEntity pe = (PaintingEntity) Utils.getTE(w, _x, _y, _z);
    pe.readFromNBTToImage(is.getTagCompound());
    if (!ep.field_71075_bZ.field_75098_d) {
      is.stackSize -= 1;
    }
    return true;
  }
}
