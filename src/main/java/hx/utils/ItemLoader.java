/*  1:   */ package hx.utils;
/*  2:   */ 
/*  3:   */ import cpw.mods.fml.common.registry.GameRegistry;
/*  4:   */ import cpw.mods.fml.relauncher.Side;
/*  5:   */ import cpw.mods.fml.relauncher.SideOnly;
/*  6:   */ import net.minecraft.item.Item;
/*  7:   */ import net.minecraftforge.client.IItemRenderer;
/*  8:   */ import net.minecraftforge.client.MinecraftForgeClient;
/*  9:   */ 
/* 10:   */ public class ItemLoader<T extends Item>
/* 11:   */ {
/* 12:   */   public T item;
/* 13:   */   
/* 14:   */   public ItemLoader(T item)
/* 15:   */   {
/* 16:14 */     this.item = item;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void load(String name)
/* 20:   */   {
/* 21:18 */     GameRegistry.registerItem(this.item, name);
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void load()
/* 25:   */   {
/* 26:22 */     load(this.item.getClass().getSimpleName().replace("$", "_"));
/* 27:   */   }
/* 28:   */   
/* 29:   */   @SideOnly(Side.CLIENT)
/* 30:   */   public void registerRendering(IItemRenderer renderer)
/* 31:   */   {
/* 32:27 */     MinecraftForgeClient.registerItemRenderer(this.item, renderer);
/* 33:   */   }
/* 34:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.utils.ItemLoader
 * JD-Core Version:    0.7.0.1
 */