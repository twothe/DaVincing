package hx.minepainter.sculpture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hx.minepainter.ModMinePainter;
import hx.utils.Utils;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class SculptureBlock
        extends BlockContainer {

  private int x;
  private int y;
  private int z;
  private int meta = 0;
  private Block current = Blocks.stone;
  private int renderID = -1;

  public void setCurrentBlock(Block that, int meta) {
    if (that == null) {
      meta = 0;
      this.renderID = -1;
      this.current = Blocks.stone;
      return;
    }
    this.current = that;
    this.meta = meta;
    this.renderID = that.getRenderType();
  }

  public void setSubCoordinate(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void dropScrap(World w, int x, int y, int z, ItemStack is) {
    dropBlockAsItem(w, x, y, z, is);
  }

  public SculptureBlock() {
    super(Material.rock);
    setHardness(1.0F);
  }

  @Override
  public MovingObjectPosition collisionRayTrace(World w, int x, int y, int z, Vec3 st, Vec3 ed) {
    SculptureEntity tile = (SculptureEntity) Utils.getTE(w, x, y, z);
    Sculpture sculpture = tile.sculpture();

    int[] pos = Operations.raytrace(sculpture, st.addVector(-x, -y, -z), ed.addVector(-x, -y, -z));
    if (pos[0] == -1) {
      return null;
    }
    ForgeDirection dir = ForgeDirection.getOrientation(pos[3]);
    Vec3 hit = null;
    if (dir.offsetX != 0) {
      hit = st.getIntermediateWithXValue(ed, x + pos[0] / 8.0F + (dir.offsetX + 1) / 16.0F);
    } else if (dir.offsetY != 0) {
      hit = st.getIntermediateWithYValue(ed, y + pos[1] / 8.0F + (dir.offsetY + 1) / 16.0F);
    } else if (dir.offsetZ != 0) {
      hit = st.getIntermediateWithZValue(ed, z + pos[2] / 8.0F + (dir.offsetZ + 1) / 16.0F);
    }
    if (hit == null) {
      if (sculpture.isEmpty()) {
        return super.collisionRayTrace(w, x, y, z, st, ed);
      }
      return null;
    }
    return new MovingObjectPosition(x, y, z, pos[3], hit);
  }

  @Override
  public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
    SculptureEntity tile = (SculptureEntity) Utils.getTE(par1World, par2, par3, par4);
    Sculpture sculpture = tile.sculpture();
    for (int posX = 0; posX < 8; posX++) {
      for (int posY = 0; posY < 8; posY++) {
        for (int posZ = 0; posZ < 8; posZ++) {
          if (sculpture.getBlockAt(posX, posY, posZ, null) != Blocks.air) {
            setBlockBounds(posX / 8.0F, posY / 8.0F, posZ / 8.0F, (posX + 1) / 8.0F, (posY + 1) / 8.0F, (posZ + 1) / 8.0F);
            super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
          }
        }
      }
    }
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }

  @Override
  public boolean isBlockSolid(IBlockAccess iba, int x, int y, int z, int side) {
    if (iba.getBlock(x, y, z) == this.current) {
      return false;
    }
    return (iba.isAirBlock(x, y, z)) || (!iba.getBlock(x, y, z).isOpaqueCube());
  }

  @Override
  public TileEntity createNewTileEntity(World var1, int var2) {
    return new SculptureEntity();
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void registerBlockIcons(IIconRegister p_149651_1_) {
  }

  @SideOnly(Side.CLIENT)
  @Override
  public IIcon getIcon(int side, int meta) {
    return this.current.getIcon(side, this.meta);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public int getRenderType() {
    if (this.renderID == -1) {
      return ModMinePainter.sculpture.renderID;
    }
    return this.renderID;
  }

  @Override
  public boolean isOpaqueCube() {
    return false;
  }

  @Override
  public boolean renderAsNormalBlock() {
    return false;
  }

  @Override
  public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
    SculptureEntity se = (SculptureEntity) Utils.getTE(world, x, y, z);
    NBTTagCompound nbt = new NBTTagCompound();
    ItemStack is = new ItemStack(ModMinePainter.droppedSculpture.item);

    se.sculpture.write(nbt);
    is.writeToNBT(nbt);
    return is;
  }

  @Override
  public int getLightValue(IBlockAccess world, int x, int y, int z) {
    TileEntity te = world.getTileEntity(x, y, z);
    if ((te == null) || (!(te instanceof SculptureEntity))) {
      return super.getLightValue(world, x, y, z);
    }
    SculptureEntity se = (SculptureEntity) te;
    return se.sculpture.getLight();
  }

  @Override
  protected ItemStack createStackedBlock(int p_149644_1_) {
    return null;
  }

  @Override
  public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
    return null;
  }

  @Override
  public void breakBlock(World w, int x, int y, int z, Block b, int meta) {
    SculptureEntity se = (SculptureEntity) Utils.getTE(w, x, y, z);
    if ((se == null) || (se.sculpture().isEmpty())) {
      super.breakBlock(w, x, y, z, b, meta);
      return;
    }
    NBTTagCompound nbt = new NBTTagCompound();
    ItemStack is = new ItemStack(ModMinePainter.droppedSculpture.item);

    se.sculpture.write(nbt);
    is.writeToNBT(nbt);
    dropBlockAsItem(w, x, y, z, is);
    super.breakBlock(w, x, y, z, b, meta);
  }
}
