/*  1:   */ package hx.minepainter.item;
/*  2:   */ 
/*  3:   */ import cpw.mods.fml.relauncher.Side;
/*  4:   */ import cpw.mods.fml.relauncher.SideOnly;
/*  5:   */ import hx.minepainter.ModMinePainter;
/*  6:   */ import hx.minepainter.sculpture.Sculpture;
/*  7:   */ import hx.minepainter.sculpture.SculptureEntity;
/*  8:   */ import hx.utils.BlockLoader;
/*  9:   */ import hx.utils.Utils;
/* 10:   */ import net.minecraft.block.Block;
/* 11:   */ import net.minecraft.block.material.Material;
/* 12:   */ import net.minecraft.client.renderer.texture.IIconRegister;
/* 13:   */ import net.minecraft.entity.player.EntityPlayer;
/* 14:   */ import net.minecraft.entity.player.PlayerCapabilities;
/* 15:   */ import net.minecraft.item.Item;
/* 16:   */ import net.minecraft.item.ItemStack;
/* 17:   */ import net.minecraft.world.World;
/* 18:   */ import net.minecraftforge.common.util.ForgeDirection;
/* 19:   */ 
/* 20:   */ public class DroppedSculptureItem
/* 21:   */   extends Item
/* 22:   */ {
/* 23:   */   public DroppedSculptureItem()
/* 24:   */   {
/* 25:20 */     func_77655_b("dropped_sculpture");
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void readTo(ItemStack is, Sculpture sculpture)
/* 29:   */   {
/* 30:24 */     if (is.func_77942_o()) {
/* 31:25 */       sculpture.read(is.func_77978_p());
/* 32:   */     }
/* 33:   */   }
/* 34:   */   
/* 35:   */   @SideOnly(Side.CLIENT)
/* 36:   */   public void func_94581_a(IIconRegister r) {}
/* 37:   */   
/* 38:   */   public boolean func_77651_p()
/* 39:   */   {
/* 40:32 */     return true;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public boolean func_77648_a(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs)
/* 44:   */   {
/* 45:37 */     if (!w.func_147439_a(x, y, z).func_149688_o().func_76220_a()) {
/* 46:37 */       return false;
/* 47:   */     }
/* 48:39 */     ForgeDirection dir = ForgeDirection.getOrientation(face);
/* 49:40 */     int _x = x + dir.offsetX;
/* 50:41 */     int _y = y + dir.offsetY;
/* 51:42 */     int _z = z + dir.offsetZ;
/* 52:44 */     if (!w.func_147437_c(_x, _y, _z)) {
/* 53:44 */       return false;
/* 54:   */     }
/* 55:45 */     if (!ep.func_82247_a(x, y, z, face, is)) {
/* 56:45 */       return false;
/* 57:   */     }
/* 58:47 */     w.func_147449_b(_x, _y, _z, ModMinePainter.sculpture.block);
/* 59:48 */     SculptureEntity se = (SculptureEntity)Utils.getTE(w, _x, _y, _z);
/* 60:49 */     se.sculpture().read(is.func_77978_p());
/* 61:51 */     if (!ep.field_71075_bZ.field_75098_d) {
/* 62:52 */       is.field_77994_a -= 1;
/* 63:   */     }
/* 64:54 */     return true;
/* 65:   */   }
/* 66:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.item.DroppedSculptureItem
 * JD-Core Version:    0.7.0.1
 */