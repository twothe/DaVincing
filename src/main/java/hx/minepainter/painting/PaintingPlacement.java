/*  1:   */ package hx.minepainter.painting;
/*  2:   */ 
/*  3:   */ import net.minecraft.block.Block;
/*  4:   */ import net.minecraft.util.Vec3;
/*  5:   */ import net.minecraftforge.common.util.ForgeDirection;
/*  6:   */ 
/*  7:   */ public enum PaintingPlacement
/*  8:   */ {
/*  9:12 */   UPXNEG(ForgeDirection.UP, ForgeDirection.WEST),  UPXPOS(ForgeDirection.UP, ForgeDirection.EAST),  UPZNEG(ForgeDirection.UP, ForgeDirection.NORTH),  UPZPOS(ForgeDirection.UP, ForgeDirection.SOUTH),  XNEG(ForgeDirection.WEST, ForgeDirection.DOWN),  XPOS(ForgeDirection.EAST, ForgeDirection.DOWN),  ZNEG(ForgeDirection.NORTH, ForgeDirection.DOWN),  ZPOS(ForgeDirection.SOUTH, ForgeDirection.DOWN),  DOWNXNEG(ForgeDirection.DOWN, ForgeDirection.WEST),  DOWNXPOS(ForgeDirection.DOWN, ForgeDirection.EAST),  DOWNZNEG(ForgeDirection.DOWN, ForgeDirection.NORTH),  DOWNZPOS(ForgeDirection.DOWN, ForgeDirection.SOUTH);
/* 10:   */   
/* 11:   */   ForgeDirection normal;
/* 12:   */   ForgeDirection ypos;
/* 13:   */   ForgeDirection xpos;
/* 14:   */   
/* 15:   */   public static PaintingPlacement of(int id)
/* 16:   */   {
/* 17:28 */     return values()[(id % values().length)];
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static PaintingPlacement of(Vec3 vec, int face)
/* 21:   */   {
/* 22:33 */     ForgeDirection dir = ForgeDirection.getOrientation(face);
/* 23:34 */     switch (1.$SwitchMap$net$minecraftforge$common$util$ForgeDirection[dir.ordinal()])
/* 24:   */     {
/* 25:   */     case 1: 
/* 26:35 */       return ZPOS;
/* 27:   */     case 2: 
/* 28:36 */       return ZNEG;
/* 29:   */     case 3: 
/* 30:37 */       return XNEG;
/* 31:   */     case 4: 
/* 32:38 */       return XPOS;
/* 33:   */     case 5: 
/* 34:40 */       if (Math.abs(vec.field_72450_a) > Math.abs(vec.field_72449_c)) {
/* 35:41 */         return vec.field_72450_a > 0.0D ? DOWNXNEG : DOWNXPOS;
/* 36:   */       }
/* 37:42 */       return vec.field_72449_c > 0.0D ? DOWNZNEG : DOWNZPOS;
/* 38:   */     case 6: 
/* 39:44 */       if (Math.abs(vec.field_72450_a) > Math.abs(vec.field_72449_c)) {
/* 40:45 */         return vec.field_72450_a > 0.0D ? UPXNEG : UPXPOS;
/* 41:   */       }
/* 42:46 */       return vec.field_72449_c > 0.0D ? UPZNEG : UPZPOS;
/* 43:   */     }
/* 44:48 */     return null;
/* 45:   */   }
/* 46:   */   
/* 47:   */   private PaintingPlacement(ForgeDirection normal, ForgeDirection ypos)
/* 48:   */   {
/* 49:55 */     this.normal = normal;
/* 50:56 */     this.ypos = ypos;
/* 51:57 */     this.xpos = normal.getRotation(ypos);
/* 52:   */   }
/* 53:   */   
/* 54:   */   public float[] painting2blockWithShift(float x, float y, float shift)
/* 55:   */   {
/* 56:61 */     float[] point = new float[3];
/* 57:62 */     point[0] = ((1 - (this.xpos.offsetX + this.ypos.offsetX + this.normal.offsetX)) / 2);
/* 58:63 */     point[1] = ((1 - (this.xpos.offsetY + this.ypos.offsetY + this.normal.offsetY)) / 2);
/* 59:64 */     point[2] = ((1 - (this.xpos.offsetZ + this.ypos.offsetZ + this.normal.offsetZ)) / 2);
/* 60:65 */     point[0] += this.xpos.offsetX * x + this.ypos.offsetX * y + this.normal.offsetX * shift;
/* 61:66 */     point[1] += this.xpos.offsetY * x + this.ypos.offsetY * y + this.normal.offsetY * shift;
/* 62:67 */     point[2] += this.xpos.offsetZ * x + this.ypos.offsetZ * y + this.normal.offsetZ * shift;
/* 63:68 */     return point;
/* 64:   */   }
/* 65:   */   
/* 66:   */   public float[] painting2block(float x, float y)
/* 67:   */   {
/* 68:72 */     return painting2blockWithShift(x, y, 0.0625F);
/* 69:   */   }
/* 70:   */   
/* 71:   */   public float[] block2painting(float x, float y, float z)
/* 72:   */   {
/* 73:76 */     float[] point = new float[2];
/* 74:77 */     point[0] = ((1 - this.xpos.offsetX - this.xpos.offsetY - this.xpos.offsetZ) / 2);
/* 75:78 */     point[1] = ((1 - this.ypos.offsetX - this.ypos.offsetY - this.ypos.offsetZ) / 2);
/* 76:79 */     point[0] += this.xpos.offsetX * x + this.xpos.offsetY * y + this.xpos.offsetZ * z;
/* 77:80 */     point[1] += this.ypos.offsetX * x + this.ypos.offsetY * y + this.ypos.offsetZ * z;
/* 78:81 */     return point;
/* 79:   */   }
/* 80:   */   
/* 81:   */   public void setBlockBounds(Block b)
/* 82:   */   {
/* 83:85 */     b.func_149676_a(0 + (1 - this.normal.offsetX) / 2, 0 + (1 - this.normal.offsetY) / 2, 0 + (1 - this.normal.offsetZ) / 2, 1 - (1 + this.normal.offsetX) / 2, 1 - (1 + this.normal.offsetY) / 2, 1 - (1 + this.normal.offsetZ) / 2);
/* 84:   */   }
/* 85:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.painting.PaintingPlacement
 * JD-Core Version:    0.7.0.1
 */