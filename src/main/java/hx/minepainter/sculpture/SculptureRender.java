/*  1:   */ package hx.minepainter.sculpture;
/*  2:   */ 
/*  3:   */ import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
/*  4:   */ import cpw.mods.fml.relauncher.Side;
/*  5:   */ import cpw.mods.fml.relauncher.SideOnly;
/*  6:   */ import hx.minepainter.ModMinePainter;
/*  7:   */ import hx.utils.BlockLoader;
/*  8:   */ import net.minecraft.block.Block;
/*  9:   */ import net.minecraft.client.renderer.RenderBlocks;
/* 10:   */ import net.minecraft.world.IBlockAccess;
/* 11:   */ 
/* 12:   */ @SideOnly(Side.CLIENT)
/* 13:   */ public class SculptureRender
/* 14:   */   implements ISimpleBlockRenderingHandler
/* 15:   */ {
/* 16:   */   private static int chunk_x;
/* 17:   */   private static int chunk_y;
/* 18:   */   private static int chunk_z;
/* 19:   */   
/* 20:   */   public static void setCurrentChunkPos(int x, int y, int z)
/* 21:   */   {
/* 22:21 */     chunk_x = x;chunk_y = y;chunk_z = z;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {}
/* 26:   */   
/* 27:   */   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
/* 28:   */   {
/* 29:33 */     if (world.func_147439_a(x, y, z) != ModMinePainter.sculpture.block) {
/* 30:33 */       return false;
/* 31:   */     }
/* 32:35 */     SculptureEntity se = (SculptureEntity)world.func_147438_o(x, y, z);
/* 33:   */     
/* 34:37 */     se.getRender().updateLight(block.func_149677_c(world, x, y, z));
/* 35:38 */     se.getRender().updateAO(world, x, y, z);
/* 36:   */     
/* 37:   */ 
/* 38:   */ 
/* 39:   */ 
/* 40:   */ 
/* 41:   */ 
/* 42:   */ 
/* 43:46 */     return false;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public boolean shouldRender3DInInventory(int modelId)
/* 47:   */   {
/* 48:51 */     return false;
/* 49:   */   }
/* 50:   */   
/* 51:   */   public int getRenderId()
/* 52:   */   {
/* 53:56 */     return ModMinePainter.sculpture.renderID;
/* 54:   */   }
/* 55:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.sculpture.SculptureRender
 * JD-Core Version:    0.7.0.1
 */