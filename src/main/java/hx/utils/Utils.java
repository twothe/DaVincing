package hx.utils;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class Utils {

  public static TileEntity getTE(IBlockAccess iba, int x, int y, int z) {
    return iba.getTileEntity(x, y, z);
  }

  public static Item getItem(ItemStack is) {
    return is.getItem();
  }

  public static void forEachInv(IInventory inv, IInvTraversal traversal) {
    int size = inv.getSizeInventory();
    for (int i = 0; i < size; i++) {
      ItemStack is = inv.getStackInSlot(i);
      if (traversal.visit(i, is)) {
        return;
      }
    }
  }

  public static abstract interface IInvTraversal {

    public abstract boolean visit(int paramInt, ItemStack paramItemStack);
  }
}
