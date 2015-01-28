package hx.minepainter.sculpture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
 
@SideOnly(Side.CLIENT)
public class BlockSlice implements IBlockAccess{

	IBlockAccess iba;
	int x;
	int y;
	int z;
	Sculpture sculpture;
	int brightness;
	
	private static BlockSlice instance = new BlockSlice();	
	private BlockSlice(){};
	
	public static BlockSlice at(IBlockAccess iba, int x,int y,int z){
		instance.iba = iba;
		instance.x = x;
		instance.y = y;
		instance.z = z;
		
		TileEntity te = iba.getTileEntity(x, y, z);
		if(te != null && te instanceof SculptureEntity)
			instance.sculpture = ((SculptureEntity)te).sculpture;
		else
			instance.sculpture = null;
		
		return instance;
	}
	
	public static BlockSlice of(Sculpture sculpture,int brightness){
		instance.iba = null;
		instance.sculpture = sculpture;
		instance.brightness = brightness;
		return instance;
	}
	
	public static void clear(){
		instance.iba = null;
	}

	@Override
	public Block getBlock(int x, int y, int z) {
		if(sculpture != null && sculpture.contains(x, y, z))
			return sculpture.getBlockAt(x, y, z, this);
		if(iba == null)return Blocks.air;
		return iba.getBlock(this.x + cap(x), this.y + cap(y), this.z + cap(z));
	}

	@Override
	public TileEntity getTileEntity(int x, int y, int z) {
		if(iba == null)return null;
		return iba.getTileEntity(this.x + cap(x), this.y + cap(y), this.z + cap(z));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getLightBrightnessForSkyBlocks(int x, int y, int z, int var4) {
		if(iba == null)return brightness;
		return iba.getLightBrightnessForSkyBlocks(this.x + cap(x), this.y + cap(y), this.z + cap(z), var4);
	}

	@Override
	public int getBlockMetadata(int x,int y,int z) {
		if(sculpture != null && sculpture.contains(x, y, z))
			return sculpture.getMetaAt(x, y, z, this);
		if(iba == null)return 0;
		return iba.getBlockMetadata(this.x + cap(x), this.y + cap(y), this.z + cap(z));
	}

	@Override
	public boolean isAirBlock(int x, int y, int z) {
		if(sculpture != null && sculpture.contains(x, y, z))
			return sculpture.getBlockAt(x, y, z, this) == Blocks.air;
		if(iba == null)return true;
		return iba.isAirBlock(this.x + cap(x), this.y + cap(y), this.z + cap(z));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BiomeGenBase getBiomeGenForCoords(int var1, int var2) {
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getHeight() {
		if(iba == null)return 256;
		return iba.getHeight();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean extendedLevelsInChunkCache() {
		if(iba == null)return false;
		return iba.extendedLevelsInChunkCache();
	}

	@Override
	public Vec3Pool getWorldVec3Pool() {
		if(iba == null)return null;
		return iba.getWorldVec3Pool();
	}

	@Override
	public int isBlockProvidingPowerTo(int x, int y, int z, int var4) {
		if(iba == null)return 0;
		return iba.isBlockProvidingPowerTo(this.x + cap(x), this.y + cap(y), this.z + cap(z), var4);
	}

	@Override
	public boolean isSideSolid(int x, int y, int z, ForgeDirection side,
			boolean _default) {
		if(iba == null)return false;
		return iba.isSideSolid(this.x + cap(x), this.y + cap(y), this.z + cap(z), side, _default);
	}
	
	private static int cap(int original){ return original > 7 ? original - 7 : (original >= 0 ? 0 : original);}
}
