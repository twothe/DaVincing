/*  1:   */ package hx.minepainter.item;
/*  2:   */ 
/*  3:   */ import hx.minepainter.ModMinePainter;
/*  4:   */ import hx.minepainter.sculpture.Rotation;
/*  5:   */ import hx.minepainter.sculpture.Sculpture;
/*  6:   */ import hx.minepainter.sculpture.SculptureEntity;
/*  7:   */ import hx.utils.BlockLoader;
/*  8:   */ import hx.utils.Utils;
/*  9:   */ import net.minecraft.entity.player.EntityPlayer;
/* 10:   */ import net.minecraft.item.Item;
/* 11:   */ import net.minecraft.item.ItemStack;
/* 12:   */ import net.minecraft.world.World;
/* 13:   */ 
/* 14:   */ public class WrenchItem
/* 15:   */   extends Item
/* 16:   */ {
/* 17:   */   public WrenchItem()
/* 18:   */   {
/* 19:14 */     func_77637_a(ModMinePainter.tabMinePainter);
/* 20:15 */     func_77655_b("wrench");
/* 21:16 */     func_111206_d("minepainter:wrench");
/* 22:17 */     func_77625_d(1);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public boolean func_77648_a(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs)
/* 26:   */   {
/* 27:22 */     if (w.func_147439_a(x, y, z) != ModMinePainter.sculpture.block) {
/* 28:22 */       return false;
/* 29:   */     }
/* 30:24 */     SculptureEntity se = (SculptureEntity)Utils.getTE(w, x, y, z);
/* 31:26 */     if (ep.func_70093_af()) {
/* 32:26 */       se.sculpture().getRotation().rotate(face);
/* 33:   */     } else {
/* 34:27 */       se.sculpture().getRotation().rotate(face ^ 0x1);
/* 35:   */     }
/* 36:29 */     if (w.field_72995_K) {
/* 37:29 */       se.getRender().changed = true;
/* 38:   */     } else {
/* 39:30 */       w.func_147471_g(x, y, z);
/* 40:   */     }
/* 41:32 */     w.func_72908_a(x + 0.5D, y + 0.5D, z + 0.5D, "tile.piston.out", 0.5F, 0.5F);
/* 42:   */     
/* 43:34 */     return true;
/* 44:   */   }
/* 45:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.item.WrenchItem
 * JD-Core Version:    0.7.0.1
 */