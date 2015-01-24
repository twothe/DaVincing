/*   1:    */ package hx.minepainter.sculpture;
/*   2:    */ 
/*   3:    */ import cpw.mods.fml.relauncher.Side;
/*   4:    */ import cpw.mods.fml.relauncher.SideOnly;
/*   5:    */ import net.minecraft.block.Block;
/*   6:    */ import net.minecraft.client.renderer.RenderBlocks;
/*   7:    */ import net.minecraft.init.Blocks;
/*   8:    */ import net.minecraft.util.IIcon;
/*   9:    */ import net.minecraftforge.common.util.ForgeDirection;
/*  10:    */ 
/*  11:    */ @SideOnly(Side.CLIENT)
/*  12:    */ public class SculptureRenderBlocks
/*  13:    */   extends RenderBlocks
/*  14:    */ {
/*  15: 15 */   private double[] overrideBounds = new double[6];
/*  16: 16 */   public boolean[] drawFace = new boolean[6];
/*  17:    */   
/*  18:    */   public SculptureRenderBlocks()
/*  19:    */   {
/*  20: 20 */     for (int i = 0; i < 6; i++) {
/*  21: 20 */       this.drawFace[i] = true;
/*  22:    */     }
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void cull(Sculpture sculpture, int x, int y, int z)
/*  26:    */   {
/*  27: 24 */     Block ours = sculpture.getBlockAt(x, y, z, null);
/*  28: 25 */     for (int i = 0; i < 6; i++)
/*  29:    */     {
/*  30: 26 */       ForgeDirection dir = ForgeDirection.getOrientation(i);
/*  31: 27 */       int _x = x + dir.offsetX;
/*  32: 28 */       int _y = y + dir.offsetY;
/*  33: 29 */       int _z = z + dir.offsetZ;
/*  34: 31 */       if (!Sculpture.contains(_x, _y, _z))
/*  35:    */       {
/*  36: 32 */         this.drawFace[i] = true;
/*  37:    */       }
/*  38:    */       else
/*  39:    */       {
/*  40: 34 */         Block theirs = sculpture.getBlockAt(_x, _y, _z, null);
/*  41: 35 */         if (theirs == ours) {
/*  42: 36 */           this.drawFace[i] = false;
/*  43: 37 */         } else if ((theirs == Blocks.field_150350_a) || (!theirs.func_149662_c())) {
/*  44: 38 */           this.drawFace[i] = true;
/*  45:    */         } else {
/*  46: 40 */           this.drawFace[i] = false;
/*  47:    */         }
/*  48:    */       }
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void func_147775_a(Block p_147775_1_)
/*  53:    */   {
/*  54: 46 */     super.func_147775_a(p_147775_1_);
/*  55: 47 */     render2override();
/*  56: 48 */     renderFull();
/*  57:    */   }
/*  58:    */   
/*  59:    */   private void override2render()
/*  60:    */   {
/*  61: 52 */     this.field_147859_h = this.overrideBounds[0];
/*  62: 53 */     this.field_147855_j = this.overrideBounds[1];
/*  63: 54 */     this.field_147851_l = this.overrideBounds[2];
/*  64: 55 */     this.field_147861_i = this.overrideBounds[3];
/*  65: 56 */     this.field_147857_k = this.overrideBounds[4];
/*  66: 57 */     this.field_147853_m = this.overrideBounds[5];
/*  67:    */   }
/*  68:    */   
/*  69:    */   private void render2override()
/*  70:    */   {
/*  71: 61 */     this.overrideBounds[0] = this.field_147859_h;
/*  72: 62 */     this.overrideBounds[1] = this.field_147855_j;
/*  73: 63 */     this.overrideBounds[2] = this.field_147851_l;
/*  74: 64 */     this.overrideBounds[3] = this.field_147861_i;
/*  75: 65 */     this.overrideBounds[4] = this.field_147857_k;
/*  76: 66 */     this.overrideBounds[5] = this.field_147853_m;
/*  77:    */   }
/*  78:    */   
/*  79:    */   private void renderFull()
/*  80:    */   {
/*  81: 70 */     this.field_147859_h = 0.0D;
/*  82: 71 */     this.field_147855_j = 0.0D;
/*  83: 72 */     this.field_147851_l = 0.0D;
/*  84: 73 */     this.field_147861_i = 1.0D;
/*  85: 74 */     this.field_147857_k = 1.0D;
/*  86: 75 */     this.field_147853_m = 1.0D;
/*  87:    */   }
/*  88:    */   
/*  89:    */   public void func_147768_a(Block p_147768_1_, double p_147768_2_, double p_147768_4_, double p_147768_6_, IIcon p_147768_8_)
/*  90:    */   {
/*  91: 79 */     if (this.drawFace[0] == 0) {
/*  92: 79 */       return;
/*  93:    */     }
/*  94: 80 */     override2render();
/*  95: 81 */     super.func_147768_a(p_147768_1_, p_147768_2_, p_147768_4_, p_147768_6_, p_147768_8_);
/*  96: 82 */     renderFull();
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void func_147806_b(Block p_147806_1_, double p_147806_2_, double p_147806_4_, double p_147806_6_, IIcon p_147806_8_)
/* 100:    */   {
/* 101: 86 */     if (this.drawFace[1] == 0) {
/* 102: 86 */       return;
/* 103:    */     }
/* 104: 87 */     override2render();
/* 105: 88 */     super.func_147806_b(p_147806_1_, p_147806_2_, p_147806_4_, p_147806_6_, p_147806_8_);
/* 106: 89 */     renderFull();
/* 107:    */   }
/* 108:    */   
/* 109:    */   public void func_147761_c(Block p_147761_1_, double p_147761_2_, double p_147761_4_, double p_147761_6_, IIcon p_147761_8_)
/* 110:    */   {
/* 111: 93 */     if (this.drawFace[2] == 0) {
/* 112: 93 */       return;
/* 113:    */     }
/* 114: 94 */     override2render();
/* 115: 95 */     super.func_147761_c(p_147761_1_, p_147761_2_, p_147761_4_, p_147761_6_, p_147761_8_);
/* 116: 96 */     renderFull();
/* 117:    */   }
/* 118:    */   
/* 119:    */   public void func_147734_d(Block p_147734_1_, double p_147734_2_, double p_147734_4_, double p_147734_6_, IIcon p_147734_8_)
/* 120:    */   {
/* 121:100 */     if (this.drawFace[3] == 0) {
/* 122:100 */       return;
/* 123:    */     }
/* 124:101 */     override2render();
/* 125:102 */     super.func_147734_d(p_147734_1_, p_147734_2_, p_147734_4_, p_147734_6_, p_147734_8_);
/* 126:103 */     renderFull();
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void func_147798_e(Block p_147798_1_, double p_147798_2_, double p_147798_4_, double p_147798_6_, IIcon p_147798_8_)
/* 130:    */   {
/* 131:107 */     if (this.drawFace[4] == 0) {
/* 132:107 */       return;
/* 133:    */     }
/* 134:108 */     override2render();
/* 135:109 */     super.func_147798_e(p_147798_1_, p_147798_2_, p_147798_4_, p_147798_6_, p_147798_8_);
/* 136:110 */     renderFull();
/* 137:    */   }
/* 138:    */   
/* 139:    */   public void func_147764_f(Block p_147764_1_, double p_147764_2_, double p_147764_4_, double p_147764_6_, IIcon p_147764_8_)
/* 140:    */   {
/* 141:114 */     if (this.drawFace[5] == 0) {
/* 142:114 */       return;
/* 143:    */     }
/* 144:115 */     override2render();
/* 145:116 */     super.func_147764_f(p_147764_1_, p_147764_2_, p_147764_4_, p_147764_6_, p_147764_8_);
/* 146:117 */     renderFull();
/* 147:    */   }
/* 148:    */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.sculpture.SculptureRenderBlocks
 * JD-Core Version:    0.7.0.1
 */