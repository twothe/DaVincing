package hx.minepainter.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hx.minepainter.ModMinePainter;
import hx.minepainter.sculpture.Sculpture;
import hx.minepainter.sculpture.SculptureEntity;
import hx.utils.BlockLoader;
import hx.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class DroppedSculptureItem
        extends Item {

  public DroppedSculptureItem() {
    setUnlocalizedName("dropped_sculpture");
  }

  public void readTo(ItemStack is, Sculpture sculpture) {
    if (is.func_77942_o()) {
      sculpture.read(is.getTagCompound());
    }
  }

  @SideOnly(Side.CLIENT)
  public void func_94581_a(IIconRegister r) {
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
    if (!w.func_147437_c(_x, _y, _z)) {
      return false;
    }
    if (!ep.func_82247_a(x, y, z, face, is)) {
      return false;
    }
    w.func_147449_b(_x, _y, _z, ModMinePainter.sculpture.block);
    SculptureEntity se = (SculptureEntity) Utils.getTE(w, _x, _y, _z);
    se.sculpture().read(is.getTagCompound());
    if (!ep.field_71075_bZ.field_75098_d) {
      is.field_77994_a -= 1;
    }
    return true;
  }
}
