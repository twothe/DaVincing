/*  1:   */ package hx.utils;
/*  2:   */ 
/*  3:   */ import net.minecraft.inventory.IInventory;
/*  4:   */ import net.minecraft.item.Item;
/*  5:   */ import net.minecraft.item.ItemStack;
/*  6:   */ import net.minecraft.tileentity.TileEntity;
/*  7:   */ import net.minecraft.world.IBlockAccess;
/*  8:   */ 
/*  9:   */ public class Utils
/* 10:   */ {
/* 11:   */   public static <T extends TileEntity> T getTE(IBlockAccess iba, int x, int y, int z)
/* 12:   */   {
/* 13:13 */     return iba.func_147438_o(x, y, z);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public static <T extends Item> T getItem(ItemStack is)
/* 17:   */   {
/* 18:17 */     return is.func_77973_b();
/* 19:   */   }
/* 20:   */   
/* 21:   */   public static void forEachInv(IInventory inv, IInvTraversal traversal)
/* 22:   */   {
/* 23:21 */     int size = inv.func_70302_i_();
/* 24:22 */     for (int i = 0; i < size; i++)
/* 25:   */     {
/* 26:23 */       ItemStack is = inv.func_70301_a(i);
/* 27:24 */       if (traversal.visit(i, is)) {
/* 28:24 */         return;
/* 29:   */       }
/* 30:   */     }
/* 31:   */   }
/* 32:   */   
/* 33:   */   public static abstract interface IInvTraversal
/* 34:   */   {
/* 35:   */     public abstract boolean visit(int paramInt, ItemStack paramItemStack);
/* 36:   */   }
/* 37:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.utils.Utils
 * JD-Core Version:    0.7.0.1
 */