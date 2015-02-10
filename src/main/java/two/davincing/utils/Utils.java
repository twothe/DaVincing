package two.davincing.utils;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class Utils {

  public static void forEachInv(IInventory inv, IInvTraversal traversal) {
    int size = inv.getSizeInventory();
    for (int i = 0; i < size; i++) {
      ItemStack is = inv.getStackInSlot(i);
      if (traversal.visit(i, is)) {
        return;
      }
    }
  }

  public static interface IInvTraversal {

    public boolean visit(int i, ItemStack is);
  }

  public static String getClassName(final Object o) {
    return o == null ? "null" : o.getClass().getName();
  }

  public static String getSimpleClassName(final Object o) {
    return o == null ? "null" : o.getClass().getSimpleName();
  }

  public static String blockToString(final Block block) {
    if (block == null) {
      return "null";
    }
    final String blockIDName = GameData.getBlockRegistry().getNameForObject(block);
    return blockIDName == null ? block.getUnlocalizedName() : blockIDName;
  }

  public static String itemToString(final Item item) {
    if (item == null) {
      return "null";
    } else {
      final String itemIDName = GameData.getItemRegistry().getNameForObject(item);
      return itemIDName == null ? item.getUnlocalizedName() : itemIDName;
    }
  }
}
