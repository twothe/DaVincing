package two.davincing.painting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class PaintingCache {

  public static final int res = 256;

  private static LinkedList<PaintingSheet> sheets = new LinkedList<PaintingSheet>();
  private static ExpirablePool<ItemStack, PaintingIcon> item_pool = new ExpirablePool<ItemStack, PaintingIcon>(12) {

    @Override
    public void release(PaintingIcon v) {
      v.release();
    }

    @Override
    public PaintingIcon get() {
      return PaintingCache.get();
    }

  };

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

  public static PaintingIcon get(ItemStack is) {
    boolean upload = !item_pool.contains(is);
    PaintingIcon pi = item_pool.get(is);
    if (!item_pool.running) {
      item_pool.start();
    }
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
}
