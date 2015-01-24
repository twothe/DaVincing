/*  1:   */ package hx.minepainter.sculpture;
/*  2:   */ 
/*  3:   */ public class Rotation
/*  4:   */ {
/*  5: 8 */   byte[] r = new byte[9];
/*  6:   */   int x;
/*  7:   */   int y;
/*  8:   */   int z;
/*  9:   */   
/* 10:   */   public void apply(int x, int y, int z)
/* 11:   */   {
/* 12:12 */     this.x = (x * this.r[0] + y * this.r[3] + z * this.r[6]);
/* 13:13 */     this.y = (x * this.r[1] + y * this.r[4] + z * this.r[7]);
/* 14:14 */     this.z = (x * this.r[2] + y * this.r[5] + z * this.r[8]);
/* 15:16 */     if (this.r[0] + this.r[3] + this.r[6] < 0) {
/* 16:16 */       this.x = (7 + this.x);
/* 17:   */     }
/* 18:17 */     if (this.r[1] + this.r[4] + this.r[7] < 0) {
/* 19:17 */       this.y = (7 + this.y);
/* 20:   */     }
/* 21:18 */     if (this.r[2] + this.r[5] + this.r[8] < 0) {
/* 22:18 */       this.z = (7 + this.z);
/* 23:   */     }
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void multiply(Rotation r)
/* 27:   */   {
/* 28:22 */     byte[] result = new byte[9];
/* 29:23 */     for (int i = 0; i < 9; i++)
/* 30:   */     {
/* 31:24 */       int x = i / 3;
/* 32:25 */       int y = i % 3;
/* 33:26 */       for (int j = 0; j < 3; j++)
/* 34:   */       {
/* 35:27 */         int tmp34_33 = i; byte[] tmp34_32 = result;tmp34_32[tmp34_33] = ((byte)(tmp34_32[tmp34_33] + r.r[(x * 3 + j)] * this.r[(j * 3 + y)]));
/* 36:   */       }
/* 37:   */     }
/* 38:29 */     this.r = result;
/* 39:   */   }
/* 40:   */   
/* 41:   */   public void rotate(int face)
/* 42:   */   {
/* 43:33 */     if (face == 0) {
/* 44:33 */       multiply(new Rotation(2, 0));
/* 45:34 */     } else if (face == 1) {
/* 46:34 */       multiply(new Rotation(0, 2));
/* 47:35 */     } else if (face == 2) {
/* 48:35 */       multiply(new Rotation(0, 1));
/* 49:36 */     } else if (face == 3) {
/* 50:36 */       multiply(new Rotation(1, 0));
/* 51:37 */     } else if (face == 4) {
/* 52:37 */       multiply(new Rotation(1, 2));
/* 53:38 */     } else if (face == 5) {
/* 54:38 */       multiply(new Rotation(2, 1));
/* 55:   */     }
/* 56:   */   }
/* 57:   */   
/* 58:   */   private Rotation(int axis1, int axis2)
/* 59:   */   {
/* 60:46 */     int to1 = axis1 * 3 + axis2;
/* 61:47 */     int to2 = axis2 * 3 + axis1;
/* 62:   */     
/* 63:49 */     this.r[to1] = 1;
/* 64:50 */     this.r[to2] = -1;
/* 65:51 */     for (int i = 0; i < 3; i++) {
/* 66:52 */       if ((i != axis1) && (i != axis2)) {
/* 67:53 */         this.r[(i * 3 + i)] = 1;
/* 68:   */       }
/* 69:   */     }
/* 70:   */   }
/* 71:   */   
/* 72:   */   public Rotation()
/* 73:   */   {
/* 74:58 */     this.r[0] = 1;
/* 75:59 */     this.r[4] = 1;
/* 76:60 */     this.r[8] = 1;
/* 77:   */   }
/* 78:   */   
/* 79:   */   private int normalize(int x)
/* 80:   */   {
/* 81:64 */     if (x < 0) {
/* 82:64 */       x += x / 8 * 8 + 8;
/* 83:   */     }
/* 84:65 */     x %= 8;
/* 85:66 */     return x;
/* 86:   */   }
/* 87:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.sculpture.Rotation
 * JD-Core Version:    0.7.0.1
 */