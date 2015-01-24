package hx.minepainter.painting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.IIcon;

public class PaintingIcon implements IIcon {

  final PaintingSheet sheet;
  int index;
  float umax;
  float umin;
  float vmax;
  float vmin;

  public PaintingIcon(PaintingSheet sheet, int index) {
    this.index = index;
    this.sheet = sheet;
    int slots = sheet.resolution / 16;
    int xind = index / slots;
    int yind = index % slots;
    float unit = 1.0F / slots;

    this.umin = (1.0F * xind / slots);
    this.vmin = (1.0F * yind / slots);
    this.umax = (this.umin + unit);
    this.vmax = (this.vmin + unit);
  }

  @SideOnly(Side.CLIENT)
  public int func_94211_a() {
    return 16;
  }

  @SideOnly(Side.CLIENT)
  public int func_94216_b() {
    return 16;
  }

  @SideOnly(Side.CLIENT)
  public float func_94209_e() {
    return this.umin;
  }

  @SideOnly(Side.CLIENT)
  public float func_94212_f() {
    return this.umax;
  }

  @SideOnly(Side.CLIENT)
  public float func_94214_a(double var1) {
    return (float) (this.umin + (this.umax - this.umin) * var1 / 16.0D);
  }

  @SideOnly(Side.CLIENT)
  public float func_94206_g() {
    return this.vmin;
  }

  @SideOnly(Side.CLIENT)
  public float func_94210_h() {
    return this.vmax;
  }

  @SideOnly(Side.CLIENT)
  public float func_94207_b(double var1) {
    return (float) (this.vmin + (this.vmax - this.vmin) * var1 / 16.0D);
  }

  @SideOnly(Side.CLIENT)
  public String getIconName() {
    return "painting";
  }

  public void fill(BufferedImage img) {
    TextureUtil.func_110995_a(this.sheet.glTexId, img, (int) (this.umin * this.sheet.resolution), (int) (this.vmin * this.sheet.resolution), false, false);
  }

  public void release() {
    this.sheet.icons.add(this);
  }

  public int glTexId() {
    return this.sheet.glTexId;
  }
}
