/*  1:   */ package hx.minepainter.painting;
/*  2:   */ 
/*  3:   */ import cpw.mods.fml.relauncher.Side;
/*  4:   */ import cpw.mods.fml.relauncher.SideOnly;
/*  5:   */ import java.awt.image.BufferedImage;
/*  6:   */ import java.util.HashSet;
/*  7:   */ import net.minecraft.client.renderer.texture.TextureUtil;
/*  8:   */ import net.minecraft.util.IIcon;
/*  9:   */ 
/* 10:   */ public class PaintingIcon
/* 11:   */   implements IIcon
/* 12:   */ {
/* 13:   */   final PaintingSheet sheet;
/* 14:   */   int index;
/* 15:   */   float umax;
/* 16:   */   float umin;
/* 17:   */   float vmax;
/* 18:   */   float vmin;
/* 19:   */   
/* 20:   */   public PaintingIcon(PaintingSheet sheet, int index)
/* 21:   */   {
/* 22:17 */     this.index = index;
/* 23:18 */     this.sheet = sheet;
/* 24:19 */     int slots = sheet.resolution / 16;
/* 25:20 */     int xind = index / slots;
/* 26:21 */     int yind = index % slots;
/* 27:22 */     float unit = 1.0F / slots;
/* 28:   */     
/* 29:24 */     this.umin = (1.0F * xind / slots);
/* 30:25 */     this.vmin = (1.0F * yind / slots);
/* 31:26 */     this.umax = (this.umin + unit);
/* 32:27 */     this.vmax = (this.vmin + unit);
/* 33:   */   }
/* 34:   */   
/* 35:   */   @SideOnly(Side.CLIENT)
/* 36:   */   public int func_94211_a()
/* 37:   */   {
/* 38:31 */     return 16;
/* 39:   */   }
/* 40:   */   
/* 41:   */   @SideOnly(Side.CLIENT)
/* 42:   */   public int func_94216_b()
/* 43:   */   {
/* 44:35 */     return 16;
/* 45:   */   }
/* 46:   */   
/* 47:   */   @SideOnly(Side.CLIENT)
/* 48:   */   public float func_94209_e()
/* 49:   */   {
/* 50:39 */     return this.umin;
/* 51:   */   }
/* 52:   */   
/* 53:   */   @SideOnly(Side.CLIENT)
/* 54:   */   public float func_94212_f()
/* 55:   */   {
/* 56:43 */     return this.umax;
/* 57:   */   }
/* 58:   */   
/* 59:   */   @SideOnly(Side.CLIENT)
/* 60:   */   public float func_94214_a(double var1)
/* 61:   */   {
/* 62:47 */     return (float)(this.umin + (this.umax - this.umin) * var1 / 16.0D);
/* 63:   */   }
/* 64:   */   
/* 65:   */   @SideOnly(Side.CLIENT)
/* 66:   */   public float func_94206_g()
/* 67:   */   {
/* 68:51 */     return this.vmin;
/* 69:   */   }
/* 70:   */   
/* 71:   */   @SideOnly(Side.CLIENT)
/* 72:   */   public float func_94210_h()
/* 73:   */   {
/* 74:55 */     return this.vmax;
/* 75:   */   }
/* 76:   */   
/* 77:   */   @SideOnly(Side.CLIENT)
/* 78:   */   public float func_94207_b(double var1)
/* 79:   */   {
/* 80:59 */     return (float)(this.vmin + (this.vmax - this.vmin) * var1 / 16.0D);
/* 81:   */   }
/* 82:   */   
/* 83:   */   @SideOnly(Side.CLIENT)
/* 84:   */   public String func_94215_i()
/* 85:   */   {
/* 86:63 */     return "painting";
/* 87:   */   }
/* 88:   */   
/* 89:   */   public void fill(BufferedImage img)
/* 90:   */   {
/* 91:67 */     TextureUtil.func_110995_a(this.sheet.glTexId, img, (int)(this.umin * this.sheet.resolution), (int)(this.vmin * this.sheet.resolution), false, false);
/* 92:   */   }
/* 93:   */   
/* 94:   */   public void release()
/* 95:   */   {
/* 96:72 */     this.sheet.icons.add(this);
/* 97:   */   }
/* 98:   */   
/* 99:   */   public int glTexId()
/* :0:   */   {
/* :1:76 */     return this.sheet.glTexId;
/* :2:   */   }
/* :3:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.painting.PaintingIcon
 * JD-Core Version:    0.7.0.1
 */