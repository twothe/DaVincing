/*
 */
package two.davincing.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import java.util.Arrays;
import net.minecraft.item.ItemDye;
import org.lwjgl.opengl.GL11;
import two.davincing.utils.Texture;

/**
 * @author Two
 */
public class PaintingTexture extends Texture {

  public static final int IMAGE_WIDTH = 16;
  public static final int IMAGE_HEIGHT = 16;

  public PaintingTexture() {
    super(IMAGE_WIDTH, IMAGE_HEIGHT);

    Arrays.fill(this.pixels, Color.WHITE.getRGB());
    final int max = Math.min(ItemDye.field_150922_c.length, this.pixels.length);
    for (int i = 0; i < max; ++i) {
      this.pixels[i] = (ItemDye.field_150922_c[i] | 0xff000000);
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void setFilters() {
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
  }
  @Override
  public String getIconName() {
    return "painting";
  }

}
