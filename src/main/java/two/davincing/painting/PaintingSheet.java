package two.davincing.painting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashSet;
import net.minecraft.client.renderer.texture.TextureUtil;

@SideOnly(Side.CLIENT)
public class PaintingSheet {

  final int resolution;
  int glTexId = -1;

  HashSet<PaintingIcon> icons = new HashSet<PaintingIcon>();

  public PaintingSheet(int res) {
    glTexId = TextureUtil.glGenTextures();
    this.resolution = res;

    int total = resolution * resolution / 256;
    for (int i = 0; i < total; i++) {
      icons.add(new PaintingIcon(this, i));
    }

    TextureUtil.allocateTexture(glTexId, resolution, resolution);
  }

  public boolean isEmpty() {
    return icons.isEmpty();
  }

  public PaintingIcon get() {
    for (PaintingIcon pi : icons) {
      icons.remove(pi);
      return pi;
    }
    throw new IllegalStateException("painting slots depleted!");
  }
}
