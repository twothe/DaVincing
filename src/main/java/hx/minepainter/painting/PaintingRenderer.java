/*  1:   */ package hx.minepainter.painting;
/*  2:   */ 
/*  3:   */ import net.minecraft.client.renderer.RenderHelper;
/*  4:   */ import net.minecraft.client.renderer.Tessellator;
/*  5:   */ import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
/*  6:   */ import net.minecraft.tileentity.TileEntity;
/*  7:   */ import net.minecraft.util.IIcon;
/*  8:   */ import org.lwjgl.opengl.GL11;
/*  9:   */ 
/* 10:   */ public class PaintingRenderer
/* 11:   */   extends TileEntitySpecialRenderer
/* 12:   */ {
/* 13:   */   public void func_147500_a(TileEntity entity, double x, double y, double z, float partial)
/* 14:   */   {
/* 15:20 */     PaintingEntity pe = (PaintingEntity)entity;
/* 16:21 */     Tessellator tes = Tessellator.field_78398_a;
/* 17:22 */     PaintingIcon icon = pe.getIcon();
/* 18:23 */     PaintingPlacement placement = PaintingPlacement.of(pe.func_145832_p());
/* 19:   */     
/* 20:25 */     GL11.glPushMatrix();
/* 21:26 */     GL11.glBindTexture(3553, icon.sheet.glTexId);
/* 22:27 */     RenderHelper.func_74518_a();
/* 23:28 */     GL11.glBlendFunc(770, 771);
/* 24:29 */     GL11.glEnable(3042);
/* 25:30 */     GL11.glTranslated(x, y, z);
/* 26:   */     
/* 27:   */ 
/* 28:   */ 
/* 29:34 */     tes.func_78382_b();
/* 30:35 */     addPoint(placement, 0, 0, icon);
/* 31:36 */     addPoint(placement, 0, 1, icon);
/* 32:37 */     addPoint(placement, 1, 1, icon);
/* 33:38 */     addPoint(placement, 1, 0, icon);
/* 34:39 */     tes.func_78381_a();
/* 35:   */     
/* 36:41 */     GL11.glPopMatrix();
/* 37:42 */     GL11.glDisable(3042);
/* 38:   */   }
/* 39:   */   
/* 40:   */   private void addPoint(PaintingPlacement pp, int x, int y, IIcon icon)
/* 41:   */   {
/* 42:46 */     float[] pos = pp.painting2blockWithShift(x, y, 0.003F);
/* 43:47 */     Tessellator.field_78398_a.func_78374_a(pos[0], pos[1], pos[2], x == 0 ? icon.func_94209_e() : icon.func_94212_f(), y == 0 ? icon.func_94206_g() : icon.func_94210_h());
/* 44:   */   }
/* 45:   */   
/* 46:   */   private void drawSides(Tessellator tes, PaintingPlacement placement)
/* 47:   */   {
/* 48:55 */     tes.func_78382_b();
/* 49:56 */     addColoredPoint(placement, 0, 0, 0.0625F);
/* 50:57 */     addColoredPoint(placement, 1, 0, 0.0625F);
/* 51:58 */     addColoredPoint(placement, 1, 0, 0.0F);
/* 52:59 */     addColoredPoint(placement, 0, 0, 0.0F);
/* 53:60 */     tes.func_78381_a();
/* 54:   */     
/* 55:62 */     tes.func_78382_b();
/* 56:63 */     addColoredPoint(placement, 1, 0, 0.0625F);
/* 57:64 */     addColoredPoint(placement, 1, 1, 0.0625F);
/* 58:65 */     addColoredPoint(placement, 1, 1, 0.0F);
/* 59:66 */     addColoredPoint(placement, 1, 0, 0.0F);
/* 60:67 */     tes.func_78381_a();
/* 61:   */     
/* 62:69 */     tes.func_78382_b();
/* 63:70 */     addColoredPoint(placement, 1, 1, 0.0625F);
/* 64:71 */     addColoredPoint(placement, 0, 1, 0.0625F);
/* 65:72 */     addColoredPoint(placement, 0, 1, 0.0F);
/* 66:73 */     addColoredPoint(placement, 1, 1, 0.0F);
/* 67:74 */     tes.func_78381_a();
/* 68:   */     
/* 69:76 */     tes.func_78382_b();
/* 70:77 */     addColoredPoint(placement, 0, 1, 0.0625F);
/* 71:78 */     addColoredPoint(placement, 0, 0, 0.0625F);
/* 72:79 */     addColoredPoint(placement, 0, 0, 0.0F);
/* 73:80 */     addColoredPoint(placement, 0, 1, 0.0F);
/* 74:81 */     tes.func_78381_a();
/* 75:   */   }
/* 76:   */   
/* 77:   */   private void addColoredPoint(PaintingPlacement pp, int x, int y, float d)
/* 78:   */   {
/* 79:85 */     float[] pos = pp.painting2blockWithShift(x, y, 0.0F);
/* 80:86 */     Tessellator.field_78398_a.func_78378_d(16777215);
/* 81:87 */     Tessellator.field_78398_a.func_78377_a(pos[0], pos[1], pos[2]);
/* 82:   */   }
/* 83:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.painting.PaintingRenderer
 * JD-Core Version:    0.7.0.1
 */