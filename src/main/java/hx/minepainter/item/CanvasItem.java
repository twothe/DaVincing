/*  1:   */ package hx.minepainter.item;
/*  2:   */ 
/*  3:   */ import hx.minepainter.ModMinePainter;
/*  4:   */ import hx.minepainter.painting.PaintingEntity;
/*  5:   */ import hx.minepainter.painting.PaintingPlacement;
/*  6:   */ import hx.utils.BlockLoader;
/*  7:   */ import hx.utils.Utils;
/*  8:   */ import net.minecraft.block.Block;
/*  9:   */ import net.minecraft.block.material.Material;
/* 10:   */ import net.minecraft.entity.player.EntityPlayer;
/* 11:   */ import net.minecraft.entity.player.PlayerCapabilities;
/* 12:   */ import net.minecraft.item.Item;
/* 13:   */ import net.minecraft.item.ItemStack;
/* 14:   */ import net.minecraft.world.World;
/* 15:   */ import net.minecraftforge.common.util.ForgeDirection;
/* 16:   */ 
/* 17:   */ public class CanvasItem
/* 18:   */   extends Item
/* 19:   */ {
/* 20:   */   public CanvasItem()
/* 21:   */   {
/* 22:21 */     func_77637_a(ModMinePainter.tabMinePainter);
/* 23:22 */     func_77664_n();
/* 24:23 */     func_77655_b("canvas");
/* 25:24 */     func_111206_d("minepainter:canvas");
/* 26:   */   }
/* 27:   */   
/* 28:   */   public boolean func_77651_p()
/* 29:   */   {
/* 30:28 */     return true;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public boolean func_77648_a(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs)
/* 34:   */   {
/* 35:34 */     if (!w.func_147439_a(x, y, z).func_149688_o().func_76220_a()) {
/* 36:34 */       return false;
/* 37:   */     }
/* 38:36 */     ForgeDirection dir = ForgeDirection.getOrientation(face);
/* 39:37 */     int _x = x + dir.offsetX;
/* 40:38 */     int _y = y + dir.offsetY;
/* 41:39 */     int _z = z + dir.offsetZ;
/* 42:41 */     if (!w.func_147437_c(_x, _y, _z)) {
/* 43:41 */       return false;
/* 44:   */     }
/* 45:42 */     if (!ep.func_82247_a(x, y, z, face, is)) {
/* 46:42 */       return false;
/* 47:   */     }
/* 48:44 */     PaintingPlacement pp = PaintingPlacement.of(ep.func_70040_Z(), face);
/* 49:45 */     w.func_147465_d(_x, _y, _z, ModMinePainter.painting.block, pp.ordinal(), 3);
/* 50:46 */     PaintingEntity pe = (PaintingEntity)Utils.getTE(w, _x, _y, _z);
/* 51:47 */     pe.readFromNBTToImage(is.func_77978_p());
/* 52:49 */     if (!ep.field_71075_bZ.field_75098_d) {
/* 53:50 */       is.field_77994_a -= 1;
/* 54:   */     }
/* 55:52 */     return true;
/* 56:   */   }
/* 57:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.item.CanvasItem
 * JD-Core Version:    0.7.0.1
 */