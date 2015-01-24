/*  1:   */ package hx.minepainter.painting;
/*  2:   */ 
/*  3:   */ import java.awt.image.BufferedImage;
/*  4:   */ import java.io.ByteArrayInputStream;
/*  5:   */ import java.io.IOException;
/*  6:   */ import java.util.LinkedList;
/*  7:   */ import javax.imageio.ImageIO;
/*  8:   */ import net.minecraft.item.ItemStack;
/*  9:   */ import net.minecraft.nbt.NBTTagCompound;
/* 10:   */ 
/* 11:   */ public class PaintingCache
/* 12:   */ {
/* 13:   */   public static final int res = 256;
/* 14:15 */   private static LinkedList<PaintingSheet> sheets = new LinkedList();
/* 15:16 */   private static ExpirablePool<ItemStack, PaintingIcon> item_pool = new ExpirablePool(12)
/* 16:   */   {
/* 17:   */     public void release(PaintingIcon v)
/* 18:   */     {
/* 19:19 */       v.release();
/* 20:   */     }
/* 21:   */     
/* 22:   */     public PaintingIcon get()
/* 23:   */     {
/* 24:23 */       return PaintingCache.get();
/* 25:   */     }
/* 26:   */   };
/* 27:   */   
/* 28:   */   public static PaintingIcon get()
/* 29:   */   {
/* 30:29 */     for (PaintingSheet sheet : sheets) {
/* 31:30 */       if (!sheet.isEmpty()) {
/* 32:31 */         return sheet.get();
/* 33:   */       }
/* 34:   */     }
/* 35:33 */     PaintingSheet sheet = new PaintingSheet(256);
/* 36:34 */     sheets.add(sheet);
/* 37:35 */     return sheet.get();
/* 38:   */   }
/* 39:   */   
/* 40:   */   public static PaintingIcon get(ItemStack is)
/* 41:   */   {
/* 42:39 */     boolean upload = !item_pool.contains(is);
/* 43:40 */     PaintingIcon pi = (PaintingIcon)item_pool.get(is);
/* 44:41 */     if (!item_pool.running) {
/* 45:41 */       item_pool.start();
/* 46:   */     }
/* 47:42 */     if (upload) {
/* 48:   */       try
/* 49:   */       {
/* 50:44 */         byte[] data = is.func_77978_p().func_74770_j("image_data");
/* 51:45 */         ByteArrayInputStream bais = new ByteArrayInputStream(data);
/* 52:46 */         BufferedImage img = ImageIO.read(bais);
/* 53:47 */         pi.fill(img);
/* 54:   */       }
/* 55:   */       catch (IOException e) {}
/* 56:   */     }
/* 57:51 */     return pi;
/* 58:   */   }
/* 59:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.painting.PaintingCache
 * JD-Core Version:    0.7.0.1
 */