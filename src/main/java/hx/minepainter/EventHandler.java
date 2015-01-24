/*  1:   */ package hx.minepainter;
/*  2:   */ 
/*  3:   */ import cpw.mods.fml.common.eventhandler.SubscribeEvent;
/*  4:   */ import cpw.mods.fml.relauncher.Side;
/*  5:   */ import cpw.mods.fml.relauncher.SideOnly;
/*  6:   */ import hx.minepainter.item.ChiselItem;
/*  7:   */ import hx.minepainter.painting.PaintTool;
/*  8:   */ import hx.minepainter.painting.PaintingBlock;
/*  9:   */ import hx.minepainter.painting.PaintingPlacement;
/* 10:   */ import hx.minepainter.sculpture.Operations;
/* 11:   */ import hx.minepainter.sculpture.SculptureRender;
/* 12:   */ import hx.utils.BlockLoader;
/* 13:   */ import hx.utils.Utils;
/* 14:   */ import net.minecraft.block.Block;
/* 15:   */ import net.minecraft.client.renderer.RenderGlobal;
/* 16:   */ import net.minecraft.client.renderer.WorldRenderer;
/* 17:   */ import net.minecraft.entity.player.EntityPlayer;
/* 18:   */ import net.minecraft.item.ItemStack;
/* 19:   */ import net.minecraft.util.MovingObjectPosition;
/* 20:   */ import net.minecraft.util.Vec3;
/* 21:   */ import net.minecraft.world.World;
/* 22:   */ import net.minecraftforge.client.event.DrawBlockHighlightEvent;
/* 23:   */ import net.minecraftforge.client.event.RenderWorldEvent.Pre;
/* 24:   */ 
/* 25:   */ public class EventHandler
/* 26:   */ {
/* 27:   */   @SideOnly(Side.CLIENT)
/* 28:   */   @SubscribeEvent
/* 29:   */   public void onPreRenderWorld(RenderWorldEvent.Pre e)
/* 30:   */   {
/* 31:34 */     SculptureRender.setCurrentChunkPos(e.renderer.field_78923_c, e.renderer.field_78920_d, e.renderer.field_78921_e);
/* 32:   */   }
/* 33:   */   
/* 34:   */   @SideOnly(Side.CLIENT)
/* 35:   */   @SubscribeEvent
/* 36:   */   public void onDrawBlockhightlight(DrawBlockHighlightEvent event)
/* 37:   */   {
/* 38:40 */     ItemStack is = event.player.func_71045_bC();
/* 39:41 */     if ((is == null) || (!(is.func_77973_b() instanceof ChiselItem))) {
/* 40:41 */       return;
/* 41:   */     }
/* 42:43 */     int x = event.target.field_72311_b;int y = event.target.field_72312_c;int z = event.target.field_72309_d;
/* 43:44 */     Block sculpture = event.player.field_70170_p.func_147439_a(x, y, z);
/* 44:   */     
/* 45:46 */     int[] pos = Operations.raytrace(x, y, z, event.player);
/* 46:47 */     if (pos[0] == -1) {
/* 47:47 */       return;
/* 48:   */     }
/* 49:49 */     ChiselItem ci = (ChiselItem)Utils.getItem(is);
/* 50:50 */     int flags = ci.getChiselFlags(event.player);
/* 51:51 */     if (!Operations.validOperation(event.player.field_70170_p, x, y, z, pos, flags)) {
/* 52:51 */       return;
/* 53:   */     }
/* 54:53 */     Operations.setBlockBoundsFromRaytrace(pos, sculpture, flags);
/* 55:54 */     event.context.func_72731_b(event.player, event.target, 0, event.partialTicks);
/* 56:55 */     sculpture.func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/* 57:   */   }
/* 58:   */   
/* 59:   */   @SideOnly(Side.CLIENT)
/* 60:   */   @SubscribeEvent
/* 61:   */   public void onDrawPaintingPixel(DrawBlockHighlightEvent event)
/* 62:   */   {
/* 63:63 */     ItemStack is = event.player.func_71045_bC();
/* 64:64 */     if ((is == null) || (!(is.func_77973_b() instanceof PaintTool))) {
/* 65:64 */       return;
/* 66:   */     }
/* 67:66 */     int x = event.target.field_72311_b;int y = event.target.field_72312_c;int z = event.target.field_72309_d;
/* 68:67 */     World w = event.player.field_70170_p;
/* 69:68 */     if (w.func_147439_a(x, y, z) != ModMinePainter.painting.block) {
/* 70:68 */       return;
/* 71:   */     }
/* 72:69 */     PaintingBlock painting = (PaintingBlock)w.func_147439_a(x, y, z);
/* 73:70 */     PaintingPlacement pp = PaintingPlacement.of(w.func_72805_g(x, y, z));
/* 74:   */     
/* 75:72 */     Vec3 pos = event.player.func_70666_h(1.0F);
/* 76:73 */     Vec3 look = event.player.func_70040_Z();
/* 77:74 */     look = pos.func_72441_c(look.field_72450_a * 5.0D, look.field_72448_b * 5.0D, look.field_72449_c * 5.0D);
/* 78:   */     
/* 79:76 */     MovingObjectPosition mop = painting.func_149731_a(w, x, y, z, pos, look);
/* 80:77 */     if (mop == null) {
/* 81:77 */       return;
/* 82:   */     }
/* 83:78 */     float[] point = pp.block2painting((float)(mop.field_72307_f.field_72450_a - mop.field_72311_b), (float)(mop.field_72307_f.field_72448_b - mop.field_72312_c), (float)(mop.field_72307_f.field_72449_c - mop.field_72309_d));
/* 84:   */     
/* 85:   */ 
/* 86:   */ 
/* 87:82 */     point[0] = ((int)(point[0] * 16.0F) / 16.0F);
/* 88:83 */     point[1] = ((int)(point[1] * 16.0F) / 16.0F);
/* 89:   */     
/* 90:85 */     float[] bound1 = pp.painting2blockWithShift(point[0], point[1], 0.002F);
/* 91:86 */     float[] bound2 = pp.painting2blockWithShift(point[0] + 0.0625F, point[1] + 0.0625F, 0.002F);
/* 92:   */     
/* 93:88 */     painting.func_149676_a(Math.min(bound1[0], bound2[0]), Math.min(bound1[1], bound2[1]), Math.min(bound1[2], bound2[2]), Math.max(bound1[0], bound2[0]), Math.max(bound1[1], bound2[1]), Math.max(bound1[2], bound2[2]));
/* 94:   */     
/* 95:   */ 
/* 96:   */ 
/* 97:   */ 
/* 98:   */ 
/* 99:94 */     painting.ignore_bounds_on_state = true;
/* :0:95 */     event.context.func_72731_b(event.player, event.target, 0, event.partialTicks);
/* :1:96 */     painting.ignore_bounds_on_state = false;
/* :2:   */   }
/* :3:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.EventHandler
 * JD-Core Version:    0.7.0.1
 */