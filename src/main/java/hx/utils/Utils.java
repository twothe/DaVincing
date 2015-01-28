package hx.utils;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class Utils {

	
	public static <T extends TileEntity> T getTE(IBlockAccess iba, int x,int y,int z){
		return (T) iba.getTileEntity(x, y, z);
	}
	
	public static <T extends Item> T getItem(ItemStack is){
		return (T) is.getItem();
	}
	
	public static void forEachInv(IInventory inv, IInvTraversal traversal){
		int size = inv.getSizeInventory();
		for(int i = 0; i < size; i ++){
			ItemStack is = inv.getStackInSlot(i);
			if(traversal.visit(i, is))return;
		}
	}
	
	public static interface IInvTraversal{
		public boolean visit(int i ,ItemStack is);
	}
}
