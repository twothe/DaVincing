/*  1:   */ package hx.minepainter.painting;
/*  2:   */ 
/*  3:   */ import java.util.HashSet;
/*  4:   */ import java.util.Iterator;
/*  5:   */ import net.minecraft.client.renderer.texture.TextureUtil;
/*  6:   */ 
/*  7:   */ public class PaintingSheet
/*  8:   */ {
/*  9:   */   final int resolution;
/* 10:27 */   int glTexId = -1;
/* 11:29 */   HashSet<PaintingIcon> icons = new HashSet();
/* 12:   */   
/* 13:   */   public PaintingSheet(int res)
/* 14:   */   {
/* 15:32 */     this.glTexId = TextureUtil.func_110996_a();
/* 16:33 */     this.resolution = res;
/* 17:   */     
/* 18:35 */     int total = this.resolution * this.resolution / 256;
/* 19:36 */     for (int i = 0; i < total; i++) {
/* 20:36 */       this.icons.add(new PaintingIcon(this, i));
/* 21:   */     }
/* 22:38 */     TextureUtil.func_110991_a(this.glTexId, this.resolution, this.resolution);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public boolean isEmpty()
/* 26:   */   {
/* 27:42 */     return this.icons.isEmpty();
/* 28:   */   }
/* 29:   */   
/* 30:   */   public PaintingIcon get()
/* 31:   */   {
/* 32:46 */     Iterator i$ = this.icons.iterator();
/* 33:46 */     if (i$.hasNext())
/* 34:   */     {
/* 35:46 */       PaintingIcon pi = (PaintingIcon)i$.next();
/* 36:47 */       this.icons.remove(pi);
/* 37:48 */       return pi;
/* 38:   */     }
/* 39:50 */     throw new IllegalStateException("painting slots depleted!");
/* 40:   */   }
/* 41:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.painting.PaintingSheet
 * JD-Core Version:    0.7.0.1
 */