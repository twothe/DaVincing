package hx.minepainter.item;

import hx.minepainter.sculpture.Operations;
import hx.utils.Debug;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PieceItem extends ChiselItem{

	public PieceItem(){
		this.setCreativeTab(null);
		this.setUnlocalizedName("sculpture_piece");
		this.setTextureName("");
		this.setHasSubtypes(true);
		this.setMaxStackSize(64);
		this.setMaxDamage(0);
		this.setContainerItem(null);
	}
	
	@Override public void registerIcons(IIconRegister r){}
	
	@Override
	public Block getEditBlock(ItemStack is){
		return Block.getBlockById( (is.getItemDamage() >> 4) & 0xfff);
	}
	
	@Override
	public int getEditMeta(ItemStack is){
		return is.getItemDamage() & 0xf;
	}
	
	@Override
	public int getChiselFlags(EntityPlayer ep){
		return Operations.PLACE | Operations.CONSUME;
	}
	
	public int getWorthPiece(){
		return 1;
	}
	
	public static class Bar extends PieceItem{
		@Override
		public int getChiselFlags(EntityPlayer ep){
			int axis = Operations.getLookingAxis(ep);
			switch(axis){
			case 0 : return Operations.PLACE | Operations.ALLX | Operations.CONSUME;
			case 1 : return Operations.PLACE | Operations.ALLY | Operations.CONSUME;
			case 2 : return Operations.PLACE | Operations.ALLZ | Operations.CONSUME;
			}
			return Operations.PLACE;
		}
		
		public int getWorthPiece(){
			return 8;
		}
	}
	
	public static class Cover extends PieceItem{
		@Override
		public int getChiselFlags(EntityPlayer ep){
			int axis = Operations.getLookingAxis(ep);
			switch(axis){
			case 0 : return Operations.PLACE | Operations.ALLY | Operations.ALLZ | Operations.CONSUME;
			case 1 : return Operations.PLACE | Operations.ALLX | Operations.ALLZ | Operations.CONSUME;
			case 2 : return Operations.PLACE | Operations.ALLX | Operations.ALLY | Operations.CONSUME;
			}
			return Operations.PLACE;
		}
		
		public int getWorthPiece(){
			return 64;
		}
	}
}
