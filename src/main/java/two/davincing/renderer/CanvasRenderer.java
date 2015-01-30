package two.davincing.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;
import two.davincing.ProxyBase;

@SideOnly(Side.CLIENT)
public class CanvasRenderer implements IItemRenderer {

  public static boolean overrideUseRenderHelper = false;

  @Override
  public boolean handleRenderType(ItemStack item, ItemRenderType type) {
    return type == ItemRenderType.INVENTORY
            || type == ItemRenderType.EQUIPPED
            || type == ItemRenderType.EQUIPPED_FIRST_PERSON
            || type == ItemRenderType.ENTITY;
  }

  @Override
  public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
    if (overrideUseRenderHelper) {
      return true;
    }
    if (type == ItemRenderType.ENTITY) {
      return helper == ItemRendererHelper.ENTITY_ROTATION
              || helper == ItemRendererHelper.ENTITY_BOBBING;
    }
    return false;
  }

  @Override
  public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

    final Item canvas = ProxyBase.itemCanvas;
    IIcon icon = canvas.getIconFromDamage(0);
    if (item.hasTagCompound()) {
      PaintingIcon pi = PaintingCache.get(item);
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, pi.glTexId());
      icon = pi;
    }
    if (type == ItemRenderType.INVENTORY) {
      renderInventory(icon);
    } else if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
      renderEquipped(icon);
    } else {
      GL11.glTranslatef(-0.5f, 0, 0);
      renderEquipped(icon);
    }
  }

  private void renderInventory(IIcon icon) {
    Tessellator tes = Tessellator.instance;
    tes.startDrawingQuads();
    tes.addVertexWithUV(1, 1, 0, icon.getMinU(), icon.getMinV());
    tes.addVertexWithUV(1, 15, 0, icon.getMinU(), icon.getMaxV());
    tes.addVertexWithUV(15, 15, 0, icon.getMaxU(), icon.getMaxV());
    tes.addVertexWithUV(15, 1, 0, icon.getMaxU(), icon.getMinV());
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
