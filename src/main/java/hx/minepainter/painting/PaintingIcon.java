package hx.minepainter.painting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.IIcon;

@SideOnly(Side.CLIENT)
public class PaintingIcon implements IIcon {

  final PaintingSheet sheet;
  int index;
  float umax, umin, vmax, vmin;

  public PaintingIcon(PaintingSheet sheet, int index) {
    this.index = index;
    this.sheet = sheet;
    int slots = sheet.resolution / 16;
    int xind = index / slots;
    int yind = index % slots;
    float unit = 1.0f / slots;

    umin = 1.0f * xind / slots;
    vmin = 1.0f * yind / slots;
    umax = umin + unit;
    vmax = vmin + unit;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public int getIconWidth() {
    return 16;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public int getIconHeight() {
    return 16;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public float getMinU() {
    return umin;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public float getMaxU() {
    return umax;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public float getInterpolatedU(double var1) {
    return (float) (umin + (umax - umin) * var1 / 16d);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public float getMinV() {
    return vmin;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public float getMaxV() {
    return vmax;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public float getInterpolatedV(double var1) {
    return (float) (vmin + (vmax - vmin) * var1 / 16d);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public String getIconName() {
    return "painting";
  }

  public void fill(BufferedImage img) {
    TextureUtil.uploadTextureImageSub(sheet.glTexId, img,
            (int) (umin * sheet.resolution), (int) (vmin * sheet.resolution), false, false);
  }

  public void release() {
    this.sheet.icons.add(this);
  }

  public int glTexId() {
    return this.sheet.glTexId;
  }
}
