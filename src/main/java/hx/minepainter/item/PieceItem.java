/*  1:   */ package hx.minepainter.item;
/*  2:   */ 
/*  3:   */ import hx.minepainter.sculpture.Operations;
/*  4:   */ import net.minecraft.block.Block;
/*  5:   */ import net.minecraft.client.renderer.texture.IIconRegister;
/*  6:   */ import net.minecraft.entity.player.EntityPlayer;
/*  7:   */ import net.minecraft.item.ItemStack;
/*  8:   */ 
/*  9:   */ public class PieceItem
/* 10:   */   extends ChiselItem
/* 11:   */ {
/* 12:   */   public PieceItem()
/* 13:   */   {
/* 14:15 */     func_77637_a(null);
/* 15:16 */     func_77655_b("sculpture_piece");
/* 16:17 */     func_111206_d("");
/* 17:18 */     func_77627_a(true);
/* 18:19 */     func_77625_d(64);
/* 19:20 */     func_77656_e(0);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void func_94581_a(IIconRegister r) {}
/* 23:   */   
/* 24:   */   public Block getEditBlock(ItemStack is)
/* 25:   */   {
/* 26:27 */     return Block.func_149729_e(is.func_77960_j() >> 4 & 0xFFF);
/* 27:   */   }
/* 28:   */   
/* 29:   */   public int getEditMeta(ItemStack is)
/* 30:   */   {
/* 31:32 */     return is.func_77960_j() & 0xF;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public int getChiselFlags(EntityPlayer ep)
/* 35:   */   {
/* 36:37 */     return 33;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public int getWorthPiece()
/* 40:   */   {
/* 41:41 */     return 1;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public static class Bar
/* 45:   */     extends PieceItem
/* 46:   */   {
/* 47:   */     public int getChiselFlags(EntityPlayer ep)
/* 48:   */     {
/* 49:47 */       int axis = Operations.getLookingAxis(ep);
/* 50:48 */       switch (axis)
/* 51:   */       {
/* 52:   */       case 0: 
/* 53:49 */         return 35;
/* 54:   */       case 1: 
/* 55:50 */         return 37;
/* 56:   */       case 2: 
/* 57:51 */         return 41;
/* 58:   */       }
/* 59:53 */       return 1;
/* 60:   */     }
/* 61:   */     
/* 62:   */     public int getWorthPiece()
/* 63:   */     {
/* 64:57 */       return 8;
/* 65:   */     }
/* 66:   */   }
/* 67:   */   
/* 68:   */   public static class Cover
/* 69:   */     extends PieceItem
/* 70:   */   {
/* 71:   */     public int getChiselFlags(EntityPlayer ep)
/* 72:   */     {
/* 73:64 */       int axis = Operations.getLookingAxis(ep);
/* 74:65 */       switch (axis)
/* 75:   */       {
/* 76:   */       case 0: 
/* 77:66 */         return 45;
/* 78:   */       case 1: 
/* 79:67 */         return 43;
/* 80:   */       case 2: 
/* 81:68 */         return 39;
/* 82:   */       }
/* 83:70 */       return 1;
/* 84:   */     }
/* 85:   */     
/* 86:   */     public int getWorthPiece()
/* 87:   */     {
/* 88:74 */       return 64;
/* 89:   */     }
/* 90:   */   }
/* 91:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.item.PieceItem
 * JD-Core Version:    0.7.0.1
 */