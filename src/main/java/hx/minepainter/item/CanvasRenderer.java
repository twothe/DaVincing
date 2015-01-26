package hx.minepainter.item;

import hx.minepainter.ModMinePainter;
import hx.minepainter.painting.PaintingCache;
import hx.minepainter.painting.PaintingIcon;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class CanvasRenderer
        implements IItemRenderer {

  @Override
  public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
    return (type == IItemRenderer.ItemRenderType.INVENTORY) || (type == IItemRenderer.ItemRenderType.EQUIPPED) || (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) || (type == IItemRenderer.ItemRenderType.ENTITY);
  }

  @Override
  public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
    if (type == IItemRenderer.ItemRenderType.ENTITY) {
      return (helper == IItemRenderer.ItemRendererHelper.ENTITY_ROTATION) || (helper == IItemRenderer.ItemRendererHelper.ENTITY_BOBBING);
    }
    return false;
  }

  @Override
  public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
    IIcon icon = ((CanvasItem) ModMinePainter.canvas.item).getIconFromDamage(0);
    if (item.isOnItemFrame()) {
      PaintingIcon pi = PaintingCache.get(item);
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, pi.glTexId());
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
    tes.addVertexWithUV(1.0D, 1.0D, 0.0D, icon.getMinU(), icon.getMinV());
    tes.addVertexWithUV(1.0D, 15.0D, 0.0D, icon.getMinU(), icon.getMaxV());
    tes.addVertexWithUV(15.0D, 15.0D, 0.0D, icon.getMaxU(), icon.getMaxV());
    tes.addVertexWithUV(15.0D, 1.0D, 0.0D, icon.getMaxU(), icon.getMinV());
    tes.draw();
  }

  private void renderEquipped(IIcon icon) {
    Tessellator var5 = Tessellator.instance;
    float var7 = icon.getMinU();
    float var8 = icon.getMaxU();
    float var9 = icon.getMinV();
    float var10 = icon.getMaxV();
    ItemRenderer.renderItemIn2D(var5, var8, var9, var7, var10, 256, 256, 0.0625F);
  }
}
