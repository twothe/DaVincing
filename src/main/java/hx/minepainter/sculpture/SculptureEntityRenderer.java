/*  1:   */ package hx.minepainter.sculpture;
/*  2:   */ 
/*  3:   */ import cpw.mods.fml.relauncher.Side;
/*  4:   */ import cpw.mods.fml.relauncher.SideOnly;
/*  5:   */ import net.minecraft.client.renderer.OpenGlHelper;
/*  6:   */ import net.minecraft.client.renderer.RenderHelper;
/*  7:   */ import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
/*  8:   */ import net.minecraft.tileentity.TileEntity;
/*  9:   */ import org.lwjgl.opengl.GL11;
/* 10:   */ 
/* 11:   */ @SideOnly(Side.CLIENT)
/* 12:   */ public class SculptureEntityRenderer
/* 13:   */   extends TileEntitySpecialRenderer
/* 14:   */ {
/* 15:   */   public void func_147500_a(TileEntity var1, double xd, double yd, double zd, float partial)
/* 16:   */   {
/* 17:25 */     SculptureEntity se = (SculptureEntity)var1;
/* 18:   */     
/* 19:27 */     RenderHelper.func_74518_a();
/* 20:28 */     GL11.glShadeModel(7425);
/* 21:29 */     GL11.glEnable(3042);
/* 22:30 */     OpenGlHelper.func_148821_a(770, 771, 1, 0);
/* 23:32 */     if ((!se.getRender().ready()) && (!se.getRender().hasContext()))
/* 24:   */     {
/* 25:34 */       int lightX = (int)OpenGlHelper.lastBrightnessX;
/* 26:35 */       int lightY = (int)OpenGlHelper.lastBrightnessY;
/* 27:36 */       int light = lightY * 65536 + lightX;
/* 28:   */       
/* 29:38 */       se.getRender().initFromSculptureAndLight(se.sculpture(), light);
/* 30:   */     }
/* 31:   */     else
/* 32:   */     {
/* 33:41 */       se.updateRender();
/* 34:   */     }
/* 35:45 */     GL11.glPushMatrix();
/* 36:46 */     GL11.glTranslated(xd, yd, zd);
/* 37:47 */     GL11.glCallList(se.getRender().glDisplayList);
/* 38:48 */     GL11.glPopMatrix();
/* 39:   */     
/* 40:50 */     GL11.glDisable(3042);
/* 41:51 */     GL11.glShadeModel(7424);
/* 42:52 */     RenderHelper.func_74519_b();
/* 43:   */   }
/* 44:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.sculpture.SculptureEntityRenderer
 * JD-Core Version:    0.7.0.1
 */