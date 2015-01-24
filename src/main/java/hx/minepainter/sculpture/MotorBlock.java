/*  1:   */ package hx.minepainter.sculpture;
/*  2:   */ 
/*  3:   */ public class MotorBlock
/*  4:   */ {
/*  5:12 */   private static final int[][] rotation_table = new int[3][4];
/*  6:   */   
/*  7:   */   static
/*  8:   */   {
/*  9:14 */     for (int i = 0; i < 3; i++)
/* 10:   */     {
/* 11:15 */       int y = (i + 1) % 3;
/* 12:16 */       int z = (i + 2) % 3;
/* 13:   */       
/* 14:18 */       rotation_table[i][0] = (z * 2);
/* 15:19 */       rotation_table[i][1] = (y * 2);
/* 16:20 */       rotation_table[i][2] = (z * 2 + 1);
/* 17:21 */       rotation_table[i][3] = (y * 2 + 1);
/* 18:   */     }
/* 19:   */   }
/* 20:   */   
/* 21:   */   private int getShiftAxis(int face, int meta)
/* 22:   */   {
/* 23:27 */     int shift = face % 2 ^ meta >> 3 ^ 0x1;
/* 24:   */     
/* 25:29 */     int y = (face / 2 + 1) % 3;
/* 26:30 */     int z = (face / 2 + 2) % 3;
/* 27:31 */     int corner = (meta >> y & 0x1) * 2 + (meta >> z & 0x1);
/* 28:   */     
/* 29:   */ 
/* 30:34 */     corner = (corner + shift) % 4;
/* 31:   */     
/* 32:36 */     return rotation_table[(face / 2)][corner];
/* 33:   */   }
/* 34:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.sculpture.MotorBlock
 * JD-Core Version:    0.7.0.1
 */