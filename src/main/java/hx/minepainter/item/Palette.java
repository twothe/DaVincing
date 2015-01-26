package hx.minepainter.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hx.minepainter.ModMinePainter;
import hx.minepainter.painting.PaintingEntity;
import hx.minepainter.painting.PaintingPlacement;
import hx.utils.BlockLoader;
import hx.utils.Utils;
import java.awt.image.BufferedImage;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class Palette
        extends Item {

  private IIcon[] colors = new IIcon[6];

  public Palette() {
    setCreativeTab(ModMinePainter.tabMinePainter);
    setMaxStackSize(1);
    setTextureName("minepainter:palette");
    setUnlocalizedName("palette");
  }

  public boolean func_77623_v() {
    return true;
  }

  @Override
  public int getRenderPasses(int metadata) {
    return 7;
  }

  @Override
  public IIcon getIcon(ItemStack is, int renderPass) {
    if (renderPass == 0) {
      return this.itemIcon;
    }
    return this.colors[(renderPass - 1)];
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void registerIcons(IIconRegister par1IconRegister) {
    super.registerIcons(par1IconRegister);
    for (int i = 0; i < 6; i++) {
      this.colors[i] = par1IconRegister.registerIcon(func_111208_A() + i);
    }
  }

  @Override
  public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
    int[] itemColors = getColors(par1ItemStack);
    if (par2 == 0) {
      return super.getColorFromItemStack(par1ItemStack, par2);
    }
    return itemColors[(par2 - 1)];
  }

  public static int[] getColors(ItemStack is) {
    NBTTagCompound nbt = is.getTagCompound();
    if (nbt == null) {
      is.writeToNBT(nbt = new NBTTagCompound());
    }
    NBTTagCompound palette = nbt.getCompoundTag("palette");
    int[] colors = palette.getIntArray("colors");
    if (colors.length == 0) {
      colors = new int[]{-1, -1, -1, -1, -1, -1};
    }
    palette.setIntArray("colors", colors);
    nbt.setTag("palette", palette);

    return colors;
  }

  @Override
  public boolean onItemUse(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int par7, float _x, float _y, float _z) {
    if (w.getBlock(x, y, z) == ModMinePainter.painting.block) {
      int face = w.getBlockMetadata(x, y, z);
      PaintingEntity pe = (PaintingEntity) Utils.getTE(w, x, y, z);
      PaintingPlacement pp = PaintingPlacement.of(face);
      float[] point = pp.block2painting(_x, _y, _z);

      int[] itemColors = getColors(is);
      itemColors[0] = pe.getImg().getRGB((int) (point[0] * 16.0F), (int) (point[1] * 16.0F));
      setColors(is, itemColors);
      return true;
    }
    return false;
  }

  public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer ep) {
    setColors(is, shift(getColors(is)));
    return is;
  }

  public static int[] shift(int[] colors) {
    int t = colors[0];
    for (int i = 1; i < colors.length; i++) {
      colors[(i - 1)] = colors[i];
    }
    colors[(colors.length - 1)] = t;

    return colors;
  }

  public static void setColors(ItemStack is, int[] colors) {
    NBTTagCompound nbt = is.getTagCompound();
    if (nbt == null) {
      is.writeToNBT(nbt = new NBTTagCompound());
    }
    NBTTagCompound palette = nbt.getCompoundTag("palette");
    palette.setIntArray("colors", colors);
    nbt.setTag("palette", palette);
  }

  public void func_77624_a(ItemStack is, EntityPlayer ep, List list, boolean help) {
    int color = getColors(is)[0];
    list.add("Alpha : " + (color >> 24 & 0xFF));
    list.add("§cRed : " + (color >> 16 & 0xFF));
    list.add("§aGreen : " + (color >> 8 & 0xFF));
    list.add("§9Blue : " + (color >> 0 & 0xFF));
  }
}
