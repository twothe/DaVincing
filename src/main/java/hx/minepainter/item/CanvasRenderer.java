package hx.minepainter.item;

import hx.minepainter.ModMinePainter;
import hx.minepainter.painting.PaintingCache;
import hx.minepainter.painting.PaintingIcon;
import hx.utils.ItemLoader;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;

public class CanvasRenderer
        implements IItemRenderer {

  public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
    return (type == IItemRenderer.ItemRenderType.INVENTORY) || (type == IItemRenderer.ItemRenderType.EQUIPPED) || (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) || (type == IItemRenderer.ItemRenderType.ENTITY);
  }

  public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
    if (type == IItemRenderer.ItemRenderType.ENTITY) {
      return (helper == IItemRenderer.ItemRendererHelper.ENTITY_ROTATION) || (helper == IItemRenderer.ItemRendererHelper.ENTITY_BOBBING);
    }
    return false;
  }

  public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
    IIcon icon = ((CanvasItem) ModMinePainter.canvas.item).func_77617_a(0);
    if (item.func_77942_o()) {
      PaintingIcon pi = PaintingCache.get(item);
      GL11.glBindTexture(3553, pi.glTexId());
      icon = pi;
    }
    if (type == IItemRenderer.ItemRenderType.INVENTORY) {
      renderInventory(icon);
    } else if ((type == IItemRenderer.ItemRenderType.EQUIPPED) || (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON)) {
      renderEquipped(icon);
    } else {
      GL11.glTranslatef(-0.5F, 0.0F, 0.0F);
      renderEquipped(icon);
    }
  }

  private void renderInventory(IIcon icon) {
    Tessellator tes = Tessellator.instance;
    tes.startDrawingQuads();
    tes.func_78374_a(1.0D, 1.0D, 0.0D, icon.func_94209_e(), icon.func_94206_g());
    tes.func_78374_a(1.0D, 15.0D, 0.0D, icon.func_94209_e(), icon.func_94210_h());
    tes.func_78374_a(15.0D, 15.0D, 0.0D, icon.func_94212_f(), icon.func_94210_h());
    tes.func_78374_a(15.0D, 1.0D, 0.0D, icon.func_94212_f(), icon.func_94206_g());
    tes.draw();
  }

  private void renderEquipped(IIcon icon) {
    Tessellator var5 = Tessellator.instance;
    float var7 = icon.func_94209_e();
    float var8 = icon.func_94212_f();
    float var9 = icon.func_94206_g();
    float var10 = icon.func_94210_h();
    ItemRenderer.func_78439_a(var5, var8, var9, var7, var10, 256, 256, 0.0625F);
  }
}
