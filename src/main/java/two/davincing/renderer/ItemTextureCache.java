/*
 */
package two.davincing.renderer;

import java.awt.image.BufferedImage;
import net.minecraft.item.ItemStack;
import two.davincing.DaVincing;
import two.davincing.painting.PaintingEntity;
import two.davincing.utils.ExpirablePool;

/**
 * @author Two
 */
public class ItemTextureCache extends ExpirablePool<ItemStack, PaintingTexture> {

  public static final ItemTextureCache instance = create();

  protected static ItemTextureCache create() {
    final ItemTextureCache result = new ItemTextureCache();
    result.start();
    return result;
  }

  @Override
  protected void release(final PaintingTexture paintingIcon) {
    DaVincing.glTasks.add(new Runnable() {

      @Override
      public void run() {
        paintingIcon.dispose();
      }
    });
  }

  @Override
  protected PaintingTexture create(final ItemStack key) {
    final BufferedImage image = PaintingEntity.getPaintingFromItem(key);
    final PaintingTexture result = new PaintingTexture();
    result.setRGB(image);
    result.initializeGL();
    return result;
  }
}
