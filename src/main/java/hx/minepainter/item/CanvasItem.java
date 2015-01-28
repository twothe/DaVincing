package hx.minepainter.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hx.minepainter.ModMinePainter;
import hx.minepainter.painting.PaintingEntity;
import hx.minepainter.painting.PaintingPlacement;
import hx.utils.Utils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class CanvasItem extends Item{

	public CanvasItem(){
		super();
		this.setCreativeTab(ModMinePainter.tabMinePainter);
		this.setFull3D();
		this.setUnlocalizedName("canvas");
		this.setTextureName("minepainter:canvas");
	}
	
	@Override public boolean getShareTag(){
        return true;
    }
	
	@Override public boolean isValidArmor(ItemStack stack, int armorType, Entity entity){
		return armorType == 0;
	}
	
	 @Override
    public boolean onItemUse(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs) {

		if(!w.getBlock(x, y, z).getMaterial().isSolid())return false;
		 
        ForgeDirection dir = ForgeDirection.getOrientation(face);
        int _x = x + dir.offsetX;
        int _y = y + dir.offsetY;
        int _z = z + dir.offsetZ;
		
        if(!w.isAirBlock(_x, _y, _z))return false;
        if(!ep.canPlayerEdit(x,y,z,face,is))return false;
		 
        PaintingPlacement pp = PaintingPlacement.of(ep.getLookVec(), face);
        w.setBlock(_x,_y,_z,ModMinePainter.painting.block, pp.ordinal(), 3);
        PaintingEntity pe = Utils.getTE(w, _x, _y, _z);
        pe.readFromNBTToImage(is.getTagCompound());
        
        if(!ep.capabilities.isCreativeMode)
        	is.stackSize--;
        
        return true;
	}
	 
	
}
