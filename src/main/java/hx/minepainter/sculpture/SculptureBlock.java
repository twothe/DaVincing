package hx.minepainter.sculpture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hx.minepainter.ModMinePainter;
import hx.utils.BlockLoader;
import hx.utils.ItemLoader;
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
    this.renderID = that.func_149645_b();
  }

  public void setSubCoordinate(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void dropScrap(World w, int x, int y, int z, ItemStack is) {
    func_149642_a(w, x, y, z, is);
  }

  public SculptureBlock() {
    super(Material.field_151576_e);
    setHardness(1.0F);
  }

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

  public void func_149743_a(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
    SculptureEntity tile = (SculptureEntity) Utils.getTE(par1World, par2, par3, par4);
    Sculpture sculpture = tile.sculpture();
    for (int x = 0; x < 8; x++) {
      for (int y = 0; y < 8; y++) {
        for (int z = 0; z < 8; z++) {
          if (sculpture.getBlockAt(x, y, z, null) != Blocks.air) {
            setBlockBounds(x / 8.0F, y / 8.0F, z / 8.0F, (x + 1) / 8.0F, (y + 1) / 8.0F, (z + 1) / 8.0F);
            super.func_149743_a(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
          }
        }
      }
    }
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }

  public boolean func_149646_a(IBlockAccess iba, int x, int y, int z, int side) {
    if (iba.getBlock(x, y, z) == this.current) {
      return false;
    }
    return (iba.isAirBlock(x, y, z)) || (!iba.getBlock(x, y, z).func_149662_c());
  }

  public TileEntity createNewTileEntity(World var1, int var2) {
    return new SculptureEntity();
  }

  @SideOnly(Side.CLIENT)
  public void func_149651_a(IIconRegister p_149651_1_) {
  }

  @SideOnly(Side.CLIENT)
  public IIcon func_149691_a(int side, int meta) {
    return this.current.func_149691_a(side, this.meta);
  }

  @SideOnly(Side.CLIENT)
  public int func_149645_b() {
    if (this.renderID == -1) {
      return ModMinePainter.sculpture.renderID;
    }
    return this.renderID;
  }

  public boolean func_149662_c() {
    return false;
  }

  public boolean func_149686_d() {
    return false;
  }

  public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
    SculptureEntity se = (SculptureEntity) Utils.getTE(world, x, y, z);
    NBTTagCompound nbt = new NBTTagCompound();
    ItemStack is = new ItemStack(ModMinePainter.droppedSculpture.item);

    se.sculpture.write(nbt);
    is.writeToNBT(nbt);
    return is;
  }

  public int getLightValue(IBlockAccess world, int x, int y, int z) {
    TileEntity te = world.getTileEntity(x, y, z);
    if ((te == null) || (!(te instanceof SculptureEntity))) {
      return super.getLightValue(world, x, y, z);
    }
    SculptureEntity se = (SculptureEntity) te;
    return se.sculpture.getLight();
  }

  protected ItemStack createStackedBlock(int p_149644_1_) {
    return null;
  }

  public Item func_149650_a(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
    return null;
  }

  public void func_149749_a(World w, int x, int y, int z, Block b, int meta) {
    SculptureEntity se = (SculptureEntity) Utils.getTE(w, x, y, z);
    if ((se == null) || (se.sculpture().isEmpty())) {
      super.func_149749_a(w, x, y, z, b, meta);
      return;
    }
    NBTTagCompound nbt = new NBTTagCompound();
    ItemStack is = new ItemStack(ModMinePainter.droppedSculpture.item);

    se.sculpture.write(nbt);
    is.writeToNBT(nbt);
    func_149642_a(w, x, y, z, is);
    super.func_149749_a(w, x, y, z, b, meta);
  }
}
