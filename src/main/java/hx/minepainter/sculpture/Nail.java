package hx.minepainter.sculpture;

import hx.minepainter.ModMinePainter;
import hx.utils.Utils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;

//TODO render nails on a sculpture
//TODO keep nails on transposition
//TODO remove and place sculpture set together
//TODO look for hinge in the entire sculpture set
//TODO 
public class Nail {

	public static final Nail None = new Nail();
	public static final Nail All = new Nail();
	static{
		All.flag = -1;
	}
	
	byte flag = 0;
	
	public void readFrom(NBTTagCompound nbt){
		flag = nbt.getByte("nail_flag");
	}
	
	public void writeTo(NBTTagCompound nbt){
		nbt.setByte("nail_flag", flag);
	}
	
	public void setFace(int face){
		flag |= (1 << face);
	}
	
	public boolean isOnFace(int face){
		return (flag & (1 << face)) > 0;
	}
	
	public static Nail fromSculpture(IBlockAccess iba, int x, int y, int z){
//		if(iba.getBlock(x, y, z) != ModMinePainter.sculpture.block)return Nail.None;
//		SculptureEntity se = Utils.getTE(iba, x, y, z);
//		if(se == null)return Nail.None;
//		
//		return se.getNail();
		return Nail.None;
	}
}
