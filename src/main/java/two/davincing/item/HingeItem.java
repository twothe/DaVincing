package two.davincing.item;

import two.davincing.DaVincing;
import two.davincing.sculpture.Hinge;
import two.davincing.sculpture.SculptureEntity;
import two.davincing.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class HingeItem extends Item {

  public HingeItem() {
    this.setCreativeTab(DaVincing.tabDaVincing);
    this.setUnlocalizedName("hinge");
    this.setTextureName("minepainter:hinge");
    this.setMaxStackSize(16);
  }

  @Override
  public boolean onItemUse(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs) {
    if (w.getBlock(x, y, z) != DaVincing.sculpture.getBlock()) {
      return false;
    }

    SculptureEntity se = Utils.getTE(w, x, y, z);

    se.setHinge(Hinge.placedAt(xs, ys, zs));

    if (w.isRemote) {
      se.getRender().changed = true;
    } else {
      w.markBlockForUpdate(x, y, z);
    }

    if (!ep.capabilities.isCreativeMode) {
      is.stackSize--;
    }

    w.playSoundEffect(x + 0.5d, y + 0.5d, z + 0.5d, Blocks.iron_block.stepSound.getBreakSound(), 0.5f, 0.5f);

    return true;
  }
}