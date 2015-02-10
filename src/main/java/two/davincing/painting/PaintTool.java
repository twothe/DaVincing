package two.davincing.painting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import two.davincing.DaVincing;
import two.davincing.ProxyBase;
import two.davincing.item.ItemBase;
import two.davincing.item.Palette;
import two.davincing.network.PaintingOperationMessage;
import two.davincing.utils.PixelData;
import two.davincing.utils.Utils;

public class PaintTool extends ItemBase {

  public PaintTool() {
    this.setMaxStackSize(1);
  }

  @Override
  public boolean onItemUse(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs) {

    if (!w.isRemote) {
      return false;
    }

    boolean changed = paintAt(w, x, y, z, xs, ys, zs, getColor(ep, is), ep.isSneaking());

    if (changed) {
      DaVincing.network.sendToServer(new PaintingOperationMessage(this, x, y, z, xs, ys, zs, getColor(ep, is)));
    }

    return changed;
  }

  public int getColor(EntityPlayer ep, ItemStack is) {
    int size = ep.inventory.getSizeInventory();
    for (int i = 0; i < size; i++) {
      ItemStack slot = ep.inventory.getStackInSlot(i);
      if (slot == null) {
        continue;
      }
      if (!(slot.getItem() instanceof Palette)) {
        continue;
      }

      return Palette.getColors(slot)[0];
    }
    return 0;
  }

  public boolean apply(PixelData img, float[] point, int color, boolean isSneaking) {
    return false;
  }

  public boolean inBounds(int x, int y) {
    return x >= 0 && x < 16 && y >= 0 && y < 16;
  }

  public boolean paintAt(final World world, int x, int y, int z, float xs, float ys, float zs, int color, boolean isSneaking) {
    if (world.getBlock(x, y, z) != ProxyBase.blockPainting.getBlock()) {
      return false;
    }
    final TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (tileEntity instanceof PaintingEntity) {
      final PaintingPlacement place = PaintingPlacement.of(world.getBlockMetadata(x, y, z));
      final float[] point = place.block2painting(xs, ys, zs);

      boolean changed = false;
      for (int rotX = -1; rotX <= 1; rotX++) {
        for (int rotY = -1; rotY <= 1; rotY++) {
          final int _x = x + place.xpos.offsetX * rotX + place.ypos.offsetX * rotY;
          final int _y = y + place.xpos.offsetY * rotX + place.ypos.offsetY * rotY;
          final int _z = z + place.xpos.offsetZ * rotX + place.ypos.offsetZ * rotY;

          if (world.getBlock(_x, _y, _z) != ProxyBase.blockPainting.getBlock()) {
            continue;
          }
          if (world.getBlockMetadata(_x, _y, _z) != place.ordinal()) {
            continue;
          }

          final TileEntity _tileEntity = world.getTileEntity(_x, _y, _z);
          if (_tileEntity instanceof PaintingEntity) {
            final PaintingEntity painting = (PaintingEntity) _tileEntity;

            point[0] -= rotX;
            point[1] -= rotY;
            boolean _changed = apply(painting.getTexture(), point, color, isSneaking);
            point[0] += rotX;
            point[1] += rotY;

            if (_changed) {
              if (world.isRemote == false) {
                world.markBlockForUpdate(_x, _y, _z);
              }
              changed = true;
            }
          } else {
            DaVincing.log.warn("[PaintTool.paintAt subpixel] failed: expected PaintingEntity at %d, %d, %d, but got %s", _x, _y, _z, Utils.getClassName(tileEntity));
          }
        }
      }
      return changed;
    } else {
      DaVincing.log.warn("[PaintTool.paintAt] failed: expected PaintingEntity at %d, %d, %d, but got %s", x, y, z, Utils.getClassName(tileEntity));
      return false;
    }
  }

  public static class Mini extends PaintTool {

    public Mini() {
      super();
      this.setUnlocalizedName("mini_brush");
      this.setTextureName("minepainter:brush_small");
    }

    @Override
    public boolean apply(final PixelData pixelData, float[] point, int color, boolean isSneaking) {

      int x = (int) (point[0] * 16 + 16) - 16;
      int y = (int) (point[1] * 16 + 16) - 16;

      if (!inBounds(x, y)) {
        return false;
      }

      pixelData.setRGB(x, y, color);
      return true;
    }
  }

  public static class Bucket extends PaintTool {

    IIcon fill;

    public Bucket() {
      super();
      this.setUnlocalizedName("paint_bucket");
      this.setTextureName("minepainter:bucket");
      this.setHasSubtypes(true);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World w, EntityPlayer ep) {
      MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(w, ep, true);
      if (mop == null) {
        return is;
      }
      Material m = w.getBlock(mop.blockX, mop.blockY, mop.blockZ).getMaterial();
      if (m.isLiquid()) {
        return new ItemStack(Items.bucket);
      }
      return is;
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
      return true;
    }

    @Override
    public int getRenderPasses(int metadata) {
      return 2;
    }

    @Override
    public IIcon getIcon(ItemStack is, int renderPass) {
      if (renderPass == 0) {
        return itemIcon;
      }
      return fill;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister par1IconRegister) {
      super.registerIcons(par1IconRegister);
      fill = par1IconRegister.registerIcon("minepainter:bucket_fill");
    }

    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
      if (par2 == 1) {
        return getColor(par1ItemStack.getItemDamage(), 0);
      }
      return super.getColorFromItemStack(par1ItemStack, par2);
    }

    @Override
    public int getColor(EntityPlayer ep, ItemStack is) {
      return getColor(is.getItemDamage(), 0xff000000);
    }

    private int getColor(int dmg, int mask) {
      if (dmg < 16) {
        return ItemDye.field_150922_c[dmg] | mask;
      }
      return 0xffffff;
    }

    @Override
    public boolean apply(final PixelData img, float[] point, int color, boolean isSneaking) {
      final int x = (int) (point[0] * 16 + 16) - 16;
      final int y = (int) (point[1] * 16 + 16) - 16;

      if (inBounds(x, y)) {
        img.fillRGB(color);
        return true;
      }
      return false;
    }
  }

  public static class Mixer extends PaintTool {

    public Mixer() {
      super();
      this.setUnlocalizedName("mixer_brush").setTextureName("minepainter:brush");
    }

    @Override
    public boolean apply(final PixelData img, float[] point, int color, boolean isSneaking) {

      int x = (int) (point[0] * 16 + 16) - 16;
      int y = (int) (point[1] * 16 + 16) - 16;

      int a75 = (int) (((color >> 24) & 0xff) * 0.75f) << 24;
      a75 += color & 0xffffff;
      int a50 = (int) (((color >> 24) & 0xff) * 0.5f) << 24;
      a50 += color & 0xffffff;

      boolean changed = false;
      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
          if (!inBounds(x + i, y + j)) {
            continue;
          }
          changed = true;

          int to_blend = Math.abs(i) + Math.abs(j);
          if (to_blend == 0) {
            to_blend = color;
          } else if (to_blend == 1) {
            to_blend = a75;
          } else {
            to_blend = a50;
          }

          img.setRGB(x + i, y + j, mix(to_blend, img.getRGB(x + i, y + j)));
        }
      }
      return changed;
    }

    private int mix(int color, int original) {
      float a_alpha = (color >> 24 & 0xFF) / 255.0F;
      float b_alpha = (original >> 24 & 0xFF) / 255.0F;
      float c_alpha = a_alpha + b_alpha * (1.0F - a_alpha);
      int result = 0;

      for (int b = 0; b < 24; b += 8) {
        int ca = color >> b & 0xFF;
        ca = (int) (ca * a_alpha);
        int cb = original >> b & 0xFF;
        cb = (int) (cb * b_alpha * (1.0F - a_alpha));
        result += ((int) ((ca + cb) / c_alpha) << b);
      }

      result += ((int) (255.0F * c_alpha) << 24);
      return result;
    }

  }

  public static class Eraser extends Mixer {

    public Eraser() {
      super();
      this.setUnlocalizedName("eraser").setTextureName("minepainter:eraser");
    }

    @Override
    public boolean apply(final PixelData img, float[] point, int color, boolean isSneaking) {

      int x = (int) (point[0] * 16 + 16) - 16;
      int y = (int) (point[1] * 16 + 16) - 16;
      int mixColor;

      boolean changed = false;
      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
          if (isSneaking && (i != 0 || j != 0)) {
            continue;
          }
          if (!inBounds(x + i, y + j)) {
            continue;
          }
          changed = true;

          mixColor = img.getRGB(x + i, y + j);
          int a75 = (int) (((mixColor >> 24) & 0xff) * 0.75f) << 24;
          a75 += mixColor & 0xffffff;
          int a50 = (int) (((mixColor >> 24) & 0xff) * 0.5f) << 24;
          a50 += mixColor & 0xffffff;

          int to_blend = Math.abs(i) + Math.abs(j);
          if (to_blend == 0) {
            to_blend = 0;
          } else if (to_blend == 1) {
            to_blend = a50;
          } else {
            to_blend = a75;
          }

          img.setRGB(x + i, y + j, to_blend);
        }
      }
      return changed;
    }
  }
}
