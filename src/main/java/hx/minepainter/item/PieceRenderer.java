package hx.minepainter.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hx.minepainter.ModMinePainter;
import hx.minepainter.sculpture.SculptureBlock;
import hx.utils.BlockLoader;
import hx.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class PieceRenderer
        implements IItemRenderer {

  private RenderItem renderItem;
  private ItemStack is;
  private Minecraft mc;

  public PieceRenderer() {
    this.renderItem = new RenderItem();

    this.mc = Minecraft.getMinecraft();
  }

  public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
    return (type == IItemRenderer.ItemRenderType.INVENTORY) || (type == IItemRenderer.ItemRenderType.ENTITY) || (type == IItemRenderer.ItemRenderType.EQUIPPED) || (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON);
  }

  public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
    return (type == IItemRenderer.ItemRenderType.ENTITY) || (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON);
  }

  public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
    if (this.is == null) {
      this.is = new ItemStack(ModMinePainter.sculpture.block);
    }
    SculptureBlock sculpture = (SculptureBlock) ModMinePainter.sculpture.block;
    PieceItem piece = (PieceItem) Utils.getItem(item);
    sculpture.setCurrentBlock(piece.getEditBlock(item), piece.getEditMeta(item));
    setBounds(sculpture);
    if (type == IItemRenderer.ItemRenderType.INVENTORY) {
      RenderHelper.func_74520_c();
      this.renderItem.func_77015_a(Minecraft.getMinecraft().field_71466_p, Minecraft.getMinecraft().getTextureManager(), this.is, 0, 0);
    } else if (type == IItemRenderer.ItemRenderType.ENTITY) {
      EntityItem eis = (EntityItem) data[1];
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      this.mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
      RenderBlocks rb = (RenderBlocks) data[0];
      rb.func_147800_a(sculpture, 0, 1.0F);
    } else {
      Minecraft.getMinecraft().field_71460_t.field_78516_c.renderItem((EntityLivingBase) data[1], this.is, 0, type);
    }
    sculpture.setCurrentBlock(null, 0);
    sculpture.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }

  protected void setBounds(SculptureBlock sculpture) {
    sculpture.setBlockBounds(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);
  }

  public static class Bar
          extends PieceRenderer {

    protected void setBounds(SculptureBlock sculpture) {
      sculpture.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 1.0F, 0.7F);
    }
  }

  public static class Cover
          extends PieceRenderer {

    protected void setBounds(SculptureBlock sculpture) {
      sculpture.setBlockBounds(0.3F, 0.0F, 0.0F, 0.7F, 1.0F, 1.0F);
    }
  }
}
