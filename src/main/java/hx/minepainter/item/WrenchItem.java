package hx.minepainter.item;

import hx.minepainter.ModMinePainter;
import hx.minepainter.sculpture.SculptureEntity;
import hx.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WrenchItem
        extends Item {

  public WrenchItem() {
    setCreativeTab(ModMinePainter.tabMinePainter);
    setUnlocalizedName("wrench");
    setTextureName("minepainter:wrench");
    setMaxStackSize(1);
  }

  public boolean onItemUse(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs) {
    if (w.getBlock(x, y, z) != ModMinePainter.sculpture.block) {
      return false;
    }
    SculptureEntity se = (SculptureEntity) Utils.getTE(w, x, y, z);
    if (ep.func_70093_af()) {
      se.sculpture().getRotation().rotate(face);
    } else {
      se.sculpture().getRotation().rotate(face ^ 0x1);
    }
    if (w.isRemote) {
      se.getRender().changed = true;
    } else {
      w.markBlockForUpdate(x, y, z);
    }
    w.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "tile.piston.out", 0.5F, 0.5F);

    return true;
  }
}
