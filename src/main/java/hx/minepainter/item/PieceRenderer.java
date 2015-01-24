/*  1:   */ package hx.minepainter.item;
/*  2:   */ 
/*  3:   */ import cpw.mods.fml.relauncher.Side;
/*  4:   */ import cpw.mods.fml.relauncher.SideOnly;
/*  5:   */ import hx.minepainter.ModMinePainter;
/*  6:   */ import hx.minepainter.sculpture.SculptureBlock;
/*  7:   */ import hx.utils.BlockLoader;
/*  8:   */ import hx.utils.Utils;
/*  9:   */ import net.minecraft.client.Minecraft;
/* 10:   */ import net.minecraft.client.renderer.EntityRenderer;
/* 11:   */ import net.minecraft.client.renderer.ItemRenderer;
/* 12:   */ import net.minecraft.client.renderer.RenderBlocks;
/* 13:   */ import net.minecraft.client.renderer.RenderHelper;
/* 14:   */ import net.minecraft.client.renderer.entity.RenderItem;
/* 15:   */ import net.minecraft.client.renderer.texture.TextureManager;
/* 16:   */ import net.minecraft.client.renderer.texture.TextureMap;
/* 17:   */ import net.minecraft.entity.EntityLivingBase;
/* 18:   */ import net.minecraft.entity.item.EntityItem;
/* 19:   */ import net.minecraft.item.ItemStack;
/* 20:   */ import net.minecraftforge.client.IItemRenderer;
/* 21:   */ import net.minecraftforge.client.IItemRenderer.ItemRenderType;
/* 22:   */ import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
/* 23:   */ import org.lwjgl.opengl.GL11;
/* 24:   */ 
/* 25:   */ @SideOnly(Side.CLIENT)
/* 26:   */ public class PieceRenderer
/* 27:   */   implements IItemRenderer
/* 28:   */ {
/* 29:   */   private RenderItem renderItem;
/* 30:   */   private ItemStack is;
/* 31:   */   private Minecraft mc;
/* 32:   */   
/* 33:   */   public PieceRenderer()
/* 34:   */   {
/* 35:24 */     this.renderItem = new RenderItem();
/* 36:   */     
/* 37:26 */     this.mc = Minecraft.func_71410_x();
/* 38:   */   }
/* 39:   */   
/* 40:   */   public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type)
/* 41:   */   {
/* 42:30 */     return (type == IItemRenderer.ItemRenderType.INVENTORY) || (type == IItemRenderer.ItemRenderType.ENTITY) || (type == IItemRenderer.ItemRenderType.EQUIPPED) || (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON);
/* 43:   */   }
/* 44:   */   
/* 45:   */   public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper)
/* 46:   */   {
/* 47:39 */     return (type == IItemRenderer.ItemRenderType.ENTITY) || (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON);
/* 48:   */   }
/* 49:   */   
/* 50:   */   public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data)
/* 51:   */   {
/* 52:45 */     if (this.is == null) {
/* 53:45 */       this.is = new ItemStack(ModMinePainter.sculpture.block);
/* 54:   */     }
/* 55:46 */     SculptureBlock sculpture = (SculptureBlock)ModMinePainter.sculpture.block;
/* 56:47 */     PieceItem piece = (PieceItem)Utils.getItem(item);
/* 57:48 */     sculpture.setCurrentBlock(piece.getEditBlock(item), piece.getEditMeta(item));
/* 58:49 */     setBounds(sculpture);
/* 59:51 */     if (type == IItemRenderer.ItemRenderType.INVENTORY)
/* 60:   */     {
/* 61:53 */       RenderHelper.func_74520_c();
/* 62:54 */       this.renderItem.func_77015_a(Minecraft.func_71410_x().field_71466_p, Minecraft.func_71410_x().field_71446_o, this.is, 0, 0);
/* 63:   */     }
/* 64:57 */     else if (type == IItemRenderer.ItemRenderType.ENTITY)
/* 65:   */     {
/* 66:59 */       EntityItem eis = (EntityItem)data[1];
/* 67:60 */       GL11.glScalef(0.5F, 0.5F, 0.5F);
/* 68:61 */       this.mc.field_71446_o.func_110577_a(TextureMap.field_110575_b);
/* 69:62 */       RenderBlocks rb = (RenderBlocks)data[0];
/* 70:63 */       rb.func_147800_a(sculpture, 0, 1.0F);
/* 71:   */     }
/* 72:   */     else
/* 73:   */     {
/* 74:67 */       Minecraft.func_71410_x().field_71460_t.field_78516_c.renderItem((EntityLivingBase)data[1], this.is, 0, type);
/* 75:   */     }
/* 76:71 */     sculpture.setCurrentBlock(null, 0);
/* 77:72 */     sculpture.func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/* 78:   */   }
/* 79:   */   
/* 80:   */   protected void setBounds(SculptureBlock sculpture)
/* 81:   */   {
/* 82:76 */     sculpture.func_149676_a(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);
/* 83:   */   }
/* 84:   */   
/* 85:   */   public static class Bar
/* 86:   */     extends PieceRenderer
/* 87:   */   {
/* 88:   */     protected void setBounds(SculptureBlock sculpture)
/* 89:   */     {
/* 90:81 */       sculpture.func_149676_a(0.3F, 0.0F, 0.3F, 0.7F, 1.0F, 0.7F);
/* 91:   */     }
/* 92:   */   }
/* 93:   */   
/* 94:   */   public static class Cover
/* 95:   */     extends PieceRenderer
/* 96:   */   {
/* 97:   */     protected void setBounds(SculptureBlock sculpture)
/* 98:   */     {
/* 99:87 */       sculpture.func_149676_a(0.3F, 0.0F, 0.0F, 0.7F, 1.0F, 1.0F);
/* :0:   */     }
/* :1:   */   }
/* :2:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.item.PieceRenderer
 * JD-Core Version:    0.7.0.1
 */