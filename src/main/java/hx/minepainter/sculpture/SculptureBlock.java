package hx.minepainter.sculpture;

import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import hx.minepainter.ModMinePainter;
import hx.utils.Debug;
import hx.utils.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

//TODO [DEFER] add hooks for transparent blocks
public class SculptureBlock extends BlockContainer{

	private int x,y,z,meta = 0;
	private Block current = Blocks.stone;
	private int renderID = -1;
	public void setCurrentBlock(Block that, int meta){
		if(that == null){
			meta = 0;
			renderID = -1;
			current = Blocks.stone;
			return;
		}
		current = that;
		this.meta = meta;
		renderID = that.getRenderType();
		if(!SculptureRenderCuller.isMergeable(that))
			renderID = 0;
	}
	public void useStandardRendering(){
		renderID = 0;
	}
	public void setSubCoordinate(int x,int y,int z){
		this.x = x; this.y = y; this.z = z;
	}
	
	public void dropScrap(World w, int x, int y, int z, ItemStack is ){
		this.dropBlockAsItem(w, x, y, z, is);
	}
	
	
	public SculptureBlock() {
		super(Material.rock);
		this.setHardness(1.0f);
		this.setBlockName("sculpture");
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World w, int x, int y, int z, Vec3 st, Vec3 ed)
	{
		SculptureEntity tile = Utils.getTE(w, x, y, z);
		Sculpture sculpture = tile.sculpture();

		int[] pos = Operations.raytrace(sculpture, st.addVector(-x, -y, -z), ed.addVector(-x,-y,-z));
		if(pos[0] == -1)return null;

		ForgeDirection dir = ForgeDirection.getOrientation(pos[3]);
		Vec3 hit = null;
		if(dir.offsetX != 0)hit = st.getIntermediateWithXValue(ed, x + pos[0]/8f + (dir.offsetX+1)/16f);
		else if(dir.offsetY != 0)hit = st.getIntermediateWithYValue(ed, y + pos[1]/8f + (dir.offsetY+1)/16f);
		else if(dir.offsetZ != 0)hit = st.getIntermediateWithZValue(ed, z + pos[2]/8f + (dir.offsetZ+1)/16f);
		if(hit == null){
			if(sculpture.isEmpty())return super.collisionRayTrace(w, x, y, z, st, ed);
			return null;
		}
		
		return new MovingObjectPosition(x,y,z,pos[3], hit);
	}
	
    @Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
	{
		SculptureEntity tile = Utils.getTE(par1World, par2, par3, par4);
		Sculpture sculpture = tile.sculpture();
		
		for(int x = 0 ; x < 8 ; x ++)
			for(int y = 0 ; y < 8 ; y ++)
				for(int z = 0 ; z < 8 ; z ++){
					if(sculpture.getBlockAt(x, y, z, null) == Blocks.air)continue;
					this.setBlockBounds(x/8f, y/8f,z/8f, (x+1)/8f, (y+1)/8f,(z+1)/8f);
					super.addCollisionBoxesToList(par1World,par2,par3,par4,par5AxisAlignedBB,par6List,par7Entity);
				}
		this.setBlockBounds(0, 0, 0, 1, 1, 1);
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess iba, int x, int y, int z, int side){
//		if(x>=0 && y>=0 && z>=0 && x<8 && y<8 && z<8)
//			return iba.isAirBlock(x, y, z);
		if(iba.getBlock(x, y, z) == this.current)return false;
		return iba.isAirBlock(x, y, z) || !iba.getBlock(x, y, z).isOpaqueCube();
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new SculptureEntity();
	}
	
	@Override @SideOnly(Side.CLIENT) public void registerBlockIcons(IIconRegister p_149651_1_){}
	
	@Override @SideOnly(Side.CLIENT) public IIcon getIcon(int side, int meta){
		return current.getIcon(side, this.meta);
	}
	
	@Override @SideOnly(Side.CLIENT) public int getRenderType()
	{
		if(renderID == -1)
			return ModMinePainter.sculpture.renderID;
		return renderID;
	}
	
    @Override public boolean isOpaqueCube()
	{
		return false;
	}

    @Override public boolean renderAsNormalBlock()
	{
		return false;
	}
    
    @Override public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z){
    	SculptureEntity se = Utils.getTE(world, x, y, z);
    	NBTTagCompound nbt = new NBTTagCompound();
    	ItemStack is = new ItemStack(ModMinePainter.droppedSculpture.item);
    	
    	se.sculpture.write(nbt);
    	is.setTagCompound(nbt);
    	return is;
    }
  
    @Override public int getLightValue(IBlockAccess world,int x,int y,int z){
    	TileEntity te = world.getTileEntity(x, y, z);
    	if(te == null || !(te instanceof SculptureEntity))return super.getLightValue(world, x, y, z);
    	SculptureEntity se = (SculptureEntity) te;
    	return se.sculpture.getLight();
    }
    
    protected ItemStack createStackedBlock(int p_149644_1_){return null;}
    
    public boolean dropSculptureToPlayer(World w, EntityPlayer ep, int x,int y,int z){
    	SculptureEntity se = Utils.getTE(w, x, y, z);
    	if(se == null || se.sculpture().isEmpty()){
    		Debug.log("hey this is null!");
    		return false;
    	}
    	NBTTagCompound nbt = new NBTTagCompound();
    	ItemStack is = new ItemStack(ModMinePainter.droppedSculpture.item);
    	
    	applyPlayerRotation(se.sculpture.r, ep, true);
    	se.sculpture.write(nbt);
    	applyPlayerRotation(se.sculpture.r, ep, false);
    	is.setTagCompound(nbt);
    	this.dropBlockAsItem(w, x, y, z, is);
    	
    	if(se.getHinge() != null){
    		is = new ItemStack(ModMinePainter.hinge.item);
    		this.dropBlockAsItem(w, x, y, z, is);
    	}
    	
    	return true;
    }
    
    public boolean removedByPlayer(World w, EntityPlayer ep, int x, int y, int z)
    {
    	dropSculptureToPlayer(w,ep,x,y,z);
        return super.removedByPlayer(w, ep, x, y, z);
    }
    
    public static void applyPlayerRotation(Rotation r, EntityPlayer ep, boolean reverse){
    	Vec3 look  = ep.getLookVec();
    	double dx = Math.abs(look.xCoord);
    	double dz = Math.abs(look.zCoord);
    	
    	int rotation = 0;
    	if(dx > dz)rotation = look.xCoord > 0 ? 3 : 1;
    	else rotation = look.zCoord > 0 ? 2 : 0;
    	
    	if(reverse)rotation = (4 - rotation) % 4;
//    	Debug.log("rotation : " + rotation);
    	
    	for(int i = 0; i < rotation; i ++)r.rotate(1);
    }
    
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
    	return null;
    }
    
    public boolean onBlockActivated(World w,int x,int y,int z,EntityPlayer ep, int face, float xs, float ys, float zs){
//    	if(ep.getCurrentEquippedItem() != null)
//    		return false;
    	
    	if(ep.isSneaking())
    		return push(w,x,y,z,face^1);
    	else
    		return push(w,x,y,z,face);
    }
    
    public boolean transpose(World w, int x,int y, int z, int dx, int dy, int dz, int rotate, ForgeDirection shift){
    	
    	int tx = x+shift.offsetX, ty = y+shift.offsetY, tz = z+shift.offsetZ;
    	
    	Rotation r = new Rotation();
    	r.rotate(rotate);
    	r.applyUnbounded(dx, dy, dz);
    	tx += r.x; ty += r.y; tz += r.z;
    	x+=dx; y+=dy; z+=dz;
    	
//    	Debug.log("from " + x + "," + y + "," + z + " to " + tx + "," + ty + "," + tz);
    	
    	if(!w.isAirBlock(tx,ty,tz))return false;
    	
    	SculptureEntity se = Utils.getTE(w, x, y, z);
    	Sculpture sculpture = se.sculpture;
    	sculpture.r.rotate(rotate);
    	Hinge hinge = se.getHinge();
    	
    	w.setBlockToAir(x, y, z);
    	w.setBlock(tx, ty, tz, ModMinePainter.sculpture.block);
    	se = Utils.getTE(w, tx, ty, tz);
    	se.sculpture = sculpture;
    	if(hinge != null)se.setHinge(HingeRotationTable.rotate(hinge, rotate));
    	
    	return true;
    }
    
    TreeSet<Location> sculpture_set = new TreeSet<Location>();
    public boolean push(World w, int x,int y,int z, int face){
    	Hinge hinge = Hinge.fromSculpture(w, x, y, z);
    	if(hinge == null)return false;
    	
    	ForgeDirection push = ForgeDirection.getOrientation(face^1);
    	int rotate = hinge.getRotationFace(push);
    	ForgeDirection shift = hinge.getShift(push);
    	
    	
    	if(shift == null)return false;
    	
    	sculpture_set.clear();
    	add_connected(w,x,y,z);
    	if(sculpture_set.size() > 256)return false;
    	
    	boolean flag = true;
    	for(Location loc : sculpture_set)
    		flag &= transpose(w,x,y,z,loc.x-x,loc.y-y,loc.z-z,rotate,shift);
    	
    	sculpture_set.clear();
    	return flag;
    }
    
    private void add_connected(World w,int x,int y,int z){
    	if(w.getBlock(x, y, z) != ModMinePainter.sculpture.block)return;
    	
    	Location loc = new Location(x,y,z);
    	if(sculpture_set.contains(loc))return;    	
    	
    	sculpture_set.add(loc);
    	if(sculpture_set.size() > 256)return;
    	Nail nail = Nail.fromSculpture(w, x, y, z);
    	for(int i = 0; i < 6; i ++){
    		if(!nail.isOnFace(i))continue;
    		ForgeDirection dir = ForgeDirection.getOrientation(i);
    		add_connected(w,x+dir.offsetX,y+dir.offsetY,z+dir.offsetZ);
    	}
    }
}
