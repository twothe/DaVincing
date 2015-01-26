package hx.minepainter.painting;

import java.util.HashSet;
import java.util.Iterator;
import net.minecraft.client.renderer.texture.TextureUtil;

public class PaintingSheet {

  final int resolution;
  int glTexId = -1;
  HashSet<PaintingIcon> icons = new HashSet();

  public PaintingSheet(int res) {
    this.glTexId = TextureUtil.glGenTextures();
    this.resolution = res;

    int total = this.resolution * this.resolution / 256;
    for (int i = 0; i < total; i++) {
      this.icons.add(new PaintingIcon(this, i));
    }
    TextureUtil.allocateTexture(this.glTexId, this.resolution, this.resolution);
  }

  public boolean isEmpty() {
    return this.icons.isEmpty();
  }

  public PaintingIcon get() {
    Iterator i$ = this.icons.iterator();
    if (i$.hasNext()) {
      PaintingIcon pi = (PaintingIcon) i$.next();
      this.icons.remove(pi);
      return pi;
    }
    throw new IllegalStateException("painting slots depleted!");
  }
}
