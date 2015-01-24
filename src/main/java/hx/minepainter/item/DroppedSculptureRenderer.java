package hx.minepainter.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hx.minepainter.ModMinePainter;
import hx.minepainter.painting.ExpirablePool;
import hx.minepainter.sculpture.Sculpture;
import hx.minepainter.sculpture.SculptureBlock;
import hx.minepainter.sculpture.SculptureRenderBlocks;
import hx.utils.BlockLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class DroppedSculptureRenderer
        implements IItemRenderer {

  SculptureRenderBlocks rb;
  RenderItem renderItem;
  ItemStack is;
  ExpirablePool<ItemStack, CompiledRender> renders;

  public DroppedSculptureRenderer() {
    this.rb = new SculptureRenderBlocks();
    this.renderItem = new RenderItem();

    this.renders = new ExpirablePool<ItemStack, DroppedSculptureRenderer.CompiledRender>(12) {
      @Override
      public void release(DroppedSculptureRenderer.CompiledRender v) {
        v.clear();
      }

      @Override
      public DroppedSculptureRenderer.CompiledRender get() {
        return new DroppedSculptureRenderer.CompiledRender(DroppedSculptureRenderer.this, null);
      }
    };
  }

  public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
    return (type == IItemRenderer.ItemRenderType.INVENTORY) || (type == IItemRenderer.ItemRenderType.ENTITY) || (type == IItemRenderer.ItemRenderType.EQUIPPED) || (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON);
  }

  public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
    return type == IItemRenderer.ItemRenderType.ENTITY;
  }

  public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
    CompiledRender cr = (CompiledRender) this.renders.get(item);
    if (!cr.compiled(type)) {
      cr.compile(item.getTagCompound(), type, data);
    }
    TextureManager tm = Minecraft.getMinecraft().getTextureManager();
    tm.bindTexture(TextureMap.locationItemsTexture);
    GL11.glCallList(cr.glDispList);
  }

  private class CompiledRender {

    int glDispList = -1;
    IItemRenderer.ItemRenderType type = null;
    Sculpture sculpture = new Sculpture();

    private CompiledRender() {
    }

    public boolean compiled(IItemRenderer.ItemRenderType type) {
      return (this.glDispList >= 0) && (this.type == type);
    }

    public void clear() {
      if (this.glDispList >= 0) {
        GLAllocation.func_74523_b(this.glDispList);
      }
    }

    public void compile(NBTTagCompound nbt, IItemRenderer.ItemRenderType type, Object... data) {
      this.type = type;
      this.sculpture.read(nbt);
      if (this.glDispList < 0) {
        this.glDispList = GLAllocation.func_74526_a(1);
      }
      if (DroppedSculptureRenderer.this.is == null) {
        DroppedSculptureRenderer.this.is = new ItemStack(ModMinePainter.sculpture.block);
      }
      GL11.glNewList(this.glDispList, 4864);
      TextureManager tm = Minecraft.getMinecraft().getTextureManager();
      tm.bindTexture(TextureMap.locationItemsTexture);
      SculptureBlock sb = (SculptureBlock) ModMinePainter.sculpture.block;
      if (type == IItemRenderer.ItemRenderType.INVENTORY) {
        RenderHelper.func_74520_c();
      }
      for (int i = 0; i < 512; i++) {
        int x = i >> 6 & 0x7;
        int y = i >> 3 & 0x7;
        int z = i >> 0 & 0x7;
        if (this.sculpture.getBlockAt(x, y, z, null) != Blocks.field_150350_a) {
          sb.setCurrentBlock(this.sculpture.getBlockAt(x, y, z, null), this.sculpture.getMetaAt(x, y, z, null));
          sb.func_149676_a(x / 8.0F, y / 8.0F, z / 8.0F, (x + 1) / 8.0F, (y + 1) / 8.0F, (z + 1) / 8.0F);
          if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            OpenGlHelper.func_148821_a(770, 771, 1, 0);
            GL11.glTranslatef(-2.0F, 3.0F, 47.0F);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            DroppedSculptureRenderer.this.rb.cull(this.sculpture, x, y, z);
            DroppedSculptureRenderer.this.rb.func_147800_a(sb, 0, 1.0F);
            GL11.glEnable(2884);
            GL11.glPopMatrix();
          } else {
            GL11.glPushMatrix();
            DroppedSculptureRenderer.this.rb.cull(this.sculpture, x, y, z);
            DroppedSculptureRenderer.this.rb.func_147800_a(sb, 0, 1.0F);
            GL11.glPopMatrix();
          }
        }
      }
      GL11.glEndList();

      sb.setCurrentBlock(null, 0);
      sb.func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
  }
}
