package hx.minepainter.item;

import hx.minepainter.ModMinePainter;
import hx.minepainter.sculpture.SculptureEntity;
import hx.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WrenchItem extends Item{

	public WrenchItem(){
		this.setCreativeTab(ModMinePainter.tabMinePainter);
		this.setUnlocalizedName("wrench");
		this.setTextureName("minepainter:wrench");
		this.setMaxStackSize(1);
	}
	
	@Override
    public boolean onItemUse(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs) {
		if(w.getBlock(x, y, z) != ModMinePainter.sculpture.block)return false;
		
		SculptureEntity se = Utils.getTE(w, x, y, z);
		
		if(ep.isSneaking())se.sculpture().getRotation().rotate(face);
		else se.sculpture().getRotation().rotate(face ^ 1);
		
		if(se.getHinge() != null){
			se.setHinge(null);
			ItemStack nis = new ItemStack(ModMinePainter.hinge.item);
    		ModMinePainter.sculpture.block.dropScrap(w, x, y, z, nis);
		}
		
		if(w.isRemote)se.getRender().changed = true;
		else w.markBlockForUpdate(x, y, z);
		
		w.playSoundEffect(x+0.5d, y+0.5d, z+0.5d, "tile.piston.out", 0.5f, 0.5f);
		
		return true;
	}
}
