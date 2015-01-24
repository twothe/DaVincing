/*  1:   */ package hx.minepainter.item;
/*  2:   */ 
/*  3:   */ import hx.minepainter.ModMinePainter;
/*  4:   */ import hx.minepainter.painting.PaintingCache;
/*  5:   */ import hx.minepainter.painting.PaintingIcon;
/*  6:   */ import hx.utils.ItemLoader;
/*  7:   */ import net.minecraft.client.renderer.ItemRenderer;
/*  8:   */ import net.minecraft.client.renderer.Tessellator;
/*  9:   */ import net.minecraft.item.ItemStack;
/* 10:   */ import net.minecraft.util.IIcon;
/* 11:   */ import net.minecraftforge.client.IItemRenderer;
/* 12:   */ import net.minecraftforge.client.IItemRenderer.ItemRenderType;
/* 13:   */ import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
/* 14:   */ import org.lwjgl.opengl.GL11;
/* 15:   */ 
/* 16:   */ public class CanvasRenderer
/* 17:   */   implements IItemRenderer
/* 18:   */ {
/* 19:   */   public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type)
/* 20:   */   {
/* 21:20 */     return (type == IItemRenderer.ItemRenderType.INVENTORY) || (type == IItemRenderer.ItemRenderType.EQUIPPED) || (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) || (type == IItemRenderer.ItemRenderType.ENTITY);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper)
/* 25:   */   {
/* 26:28 */     if (type == IItemRenderer.ItemRenderType.ENTITY) {
/* 27:29 */       return (helper == IItemRenderer.ItemRendererHelper.ENTITY_ROTATION) || (helper == IItemRenderer.ItemRendererHelper.ENTITY_BOBBING);
/* 28:   */     }
/* 29:32 */     return false;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data)
/* 33:   */   {
/* 34:38 */     IIcon icon = ((CanvasItem)ModMinePainter.canvas.item).func_77617_a(0);
/* 35:39 */     if (item.func_77942_o())
/* 36:   */     {
/* 37:40 */       PaintingIcon pi = PaintingCache.get(item);
/* 38:41 */       GL11.glBindTexture(3553, pi.glTexId());
/* 39:42 */       icon = pi;
/* 40:   */     }
/* 41:44 */     if (type == IItemRenderer.ItemRenderType.INVENTORY)
/* 42:   */     {
/* 43:45 */       renderInventory(icon);
/* 44:   */     }
/* 45:46 */     else if ((type == IItemRenderer.ItemRenderType.EQUIPPED) || (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON))
/* 46:   */     {
/* 47:47 */       renderEquipped(icon);
/* 48:   */     }
/* 49:   */     else
/* 50:   */     {
/* 51:49 */       GL11.glTranslatef(-0.5F, 0.0F, 0.0F);
/* 52:50 */       renderEquipped(icon);
/* 53:   */     }
/* 54:   */   }
/* 55:   */   
/* 56:   */   private void renderInventory(IIcon icon)
/* 57:   */   {
/* 58:56 */     Tessellator tes = Tessellator.field_78398_a;
/* 59:57 */     tes.func_78382_b();
/* 60:58 */     tes.func_78374_a(1.0D, 1.0D, 0.0D, icon.func_94209_e(), icon.func_94206_g());
/* 61:59 */     tes.func_78374_a(1.0D, 15.0D, 0.0D, icon.func_94209_e(), icon.func_94210_h());
/* 62:60 */     tes.func_78374_a(15.0D, 15.0D, 0.0D, icon.func_94212_f(), icon.func_94210_h());
/* 63:61 */     tes.func_78374_a(15.0D, 1.0D, 0.0D, icon.func_94212_f(), icon.func_94206_g());
/* 64:62 */     tes.func_78381_a();
/* 65:   */   }
/* 66:   */   
/* 67:   */   private void renderEquipped(IIcon icon)
/* 68:   */   {
/* 69:67 */     Tessellator var5 = Tessellator.field_78398_a;
/* 70:68 */     float var7 = icon.func_94209_e();
/* 71:69 */     float var8 = icon.func_94212_f();
/* 72:70 */     float var9 = icon.func_94206_g();
/* 73:71 */     float var10 = icon.func_94210_h();
/* 74:72 */     ItemRenderer.func_78439_a(var5, var8, var9, var7, var10, 256, 256, 0.0625F);
/* 75:   */   }
/* 76:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.item.CanvasRenderer
 * JD-Core Version:    0.7.0.1
 */