package hx.minepainter.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hx.minepainter.ModMinePainter;
import hx.minepainter.sculpture.SculptureBlock;
import hx.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class PieceRenderer implements IItemRenderer {

  private RenderItem renderItem = new RenderItem();
  private ItemStack is;
  private Minecraft mc = Minecraft.getMinecraft();

  @Override
  public boolean handleRenderType(ItemStack item, ItemRenderType type) {
    return type == ItemRenderType.INVENTORY
            || type == ItemRenderType.ENTITY
            || type == ItemRenderType.EQUIPPED
            || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
  }

  @Override
  public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
          ItemRendererHelper helper) {
    return type == ItemRenderType.ENTITY || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
  }

  @Override
  public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

    if (is == null) {
      is = new ItemStack(ModMinePainter.sculpture.block);
    }
    SculptureBlock sculpture = ModMinePainter.sculpture.block;
    PieceItem piece = Utils.getItem(item);
    sculpture.setCurrentBlock(piece.getEditBlock(item), piece.getEditMeta(item));
    setBounds(sculpture);

    if (type == ItemRenderType.INVENTORY) {
      RenderHelper.enableGUIStandardItemLighting();
      renderItem.renderItemIntoGUI(
              Minecraft.getMinecraft().fontRenderer,
              Minecraft.getMinecraft().renderEngine, is, 0, 0);
    } else if (type == ItemRenderType.ENTITY) {
      EntityItem eis = (EntityItem) data[1];
      GL11.glScalef(.5f, .5f, .5f);
      mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
      RenderBlocks rb = (RenderBlocks) (data[0]);
      rb.renderBlockAsItem(sculpture, 0, 1f);

    } else {
      Minecraft.getMinecraft().entityRenderer.itemRenderer.renderItem((EntityLivingBase) data[1],
              is, 0, type);
    }

    sculpture.setCurrentBlock(null, 0);
    sculpture.setBlockBounds(0, 0, 0, 1, 1, 1);
  }

  protected void setBounds(SculptureBlock sculpture) {
    sculpture.setBlockBounds(0.3f, 0.3f, 0.3f, 0.7f, 0.7f, 0.7f);
  }

  public static class Bar extends PieceRenderer {

    @Override
    protected void setBounds(SculptureBlock sculpture) {
      sculpture.setBlockBounds(0.3f, 0.0f, 0.3f, 0.7f, 1.0f, 0.7f);
    }
  }

  public static class Cover extends PieceRenderer {

    @Override
    protected void setBounds(SculptureBlock sculpture) {
      sculpture.setBlockBounds(0.3f, 0.0f, 0.0f, 0.7f, 1.0f, 1.0f);
    }
  }
}
