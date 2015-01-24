/*  1:   */ package hx.utils;
/*  2:   */ 
/*  3:   */ import cpw.mods.fml.client.registry.ClientRegistry;
/*  4:   */ import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
/*  5:   */ import cpw.mods.fml.client.registry.RenderingRegistry;
/*  6:   */ import cpw.mods.fml.common.registry.GameRegistry;
/*  7:   */ import cpw.mods.fml.relauncher.Side;
/*  8:   */ import cpw.mods.fml.relauncher.SideOnly;
/*  9:   */ import net.minecraft.block.Block;
/* 10:   */ import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
/* 11:   */ import net.minecraft.tileentity.TileEntity;
/* 12:   */ 
/* 13:   */ public class BlockLoader<T extends Block>
/* 14:   */ {
/* 15:   */   public final T block;
/* 16:   */   public final Class<? extends TileEntity> tileEntityClass;
/* 17:   */   @SideOnly(Side.CLIENT)
/* 18:   */   public int renderID;
/* 19:   */   
/* 20:   */   public BlockLoader(T block, Class<? extends TileEntity> clazz)
/* 21:   */   {
/* 22:23 */     this.block = block;
/* 23:24 */     this.tileEntityClass = clazz;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void load()
/* 27:   */   {
/* 28:28 */     GameRegistry.registerBlock(this.block, this.block.getClass().getSimpleName());
/* 29:29 */     if (this.tileEntityClass != null) {
/* 30:29 */       GameRegistry.registerTileEntity(this.tileEntityClass, this.tileEntityClass.getSimpleName());
/* 31:   */     }
/* 32:   */   }
/* 33:   */   
/* 34:   */   @SideOnly(Side.CLIENT)
/* 35:   */   public void registerRendering(ISimpleBlockRenderingHandler blockRenderer, TileEntitySpecialRenderer tileRenderer)
/* 36:   */   {
/* 37:34 */     if (blockRenderer != null)
/* 38:   */     {
/* 39:35 */       this.renderID = RenderingRegistry.getNextAvailableRenderId();
/* 40:36 */       RenderingRegistry.registerBlockHandler(this.renderID, blockRenderer);
/* 41:   */     }
/* 42:38 */     if (tileRenderer != null) {
/* 43:39 */       ClientRegistry.bindTileEntitySpecialRenderer(this.tileEntityClass, tileRenderer);
/* 44:   */     }
/* 45:   */   }
/* 46:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.utils.BlockLoader
 * JD-Core Version:    0.7.0.1
 */