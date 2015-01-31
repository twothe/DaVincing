package two.davincing.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import net.minecraft.item.ItemStack;
import two.davincing.utils.ExpirablePool;

@SideOnly(Side.CLIENT)
public class PaintingCache {

  public static final int res = 256;

  private static final LinkedList<PaintingSheet> sheets = new LinkedList<PaintingSheet>();
  private static final ExpirablePool<ItemStack, PaintingIcon> item_pool;

  static {
    item_pool = new ExpirablePool<ItemStack, PaintingIcon>() {

      @Override
      protected void release(final PaintingIcon v) {
        v.release();
      }

      @Override
      protected PaintingIcon create() {
        return PaintingCache.get();
      }

    };
    item_pool.start();
  }

  public static PaintingIcon get() {
    for (PaintingSheet sheet : sheets) {
      if (sheet.isEmpty()) {
        continue;
      }
      return sheet.get();
    }
    PaintingSheet sheet = new PaintingSheet(res);
    sheets.add(sheet);
    return sheet.get();
  }

  public static PaintingIcon get(final ItemStack is) {
    boolean upload = !item_pool.contains(is);
    PaintingIcon pi = item_pool.get(is);
    if (upload) {
      try {
        byte[] data = is.getTagCompound().getByteArray("image_data");
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        BufferedImage img = ImageIO.read(bais);
        pi.fill(img);
      } catch (IOException e) {
      }
    }
    return pi;
  }
  
  public static void clear() {
    item_pool.clear();
  }
}
