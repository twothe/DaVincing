/*   1:    */ package hx.minepainter.item;
/*   2:    */ 
/*   3:    */ import cpw.mods.fml.relauncher.Side;
/*   4:    */ import cpw.mods.fml.relauncher.SideOnly;
/*   5:    */ import hx.minepainter.ModMinePainter;
/*   6:    */ import hx.minepainter.painting.ExpirablePool;
/*   7:    */ import hx.minepainter.sculpture.Sculpture;
/*   8:    */ import hx.minepainter.sculpture.SculptureBlock;
/*   9:    */ import hx.minepainter.sculpture.SculptureRenderBlocks;
/*  10:    */ import hx.utils.BlockLoader;
/*  11:    */ import net.minecraft.client.Minecraft;
/*  12:    */ import net.minecraft.client.renderer.GLAllocation;
/*  13:    */ import net.minecraft.client.renderer.OpenGlHelper;
/*  14:    */ import net.minecraft.client.renderer.RenderHelper;
/*  15:    */ import net.minecraft.client.renderer.entity.RenderItem;
/*  16:    */ import net.minecraft.client.renderer.texture.TextureManager;
/*  17:    */ import net.minecraft.client.renderer.texture.TextureMap;
/*  18:    */ import net.minecraft.init.Blocks;
/*  19:    */ import net.minecraft.item.ItemStack;
/*  20:    */ import net.minecraft.nbt.NBTTagCompound;
/*  21:    */ import net.minecraftforge.client.IItemRenderer;
/*  22:    */ import net.minecraftforge.client.IItemRenderer.ItemRenderType;
/*  23:    */ import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
/*  24:    */ import org.lwjgl.opengl.GL11;
/*  25:    */ 
/*  26:    */ @SideOnly(Side.CLIENT)
/*  27:    */ public class DroppedSculptureRenderer
/*  28:    */   implements IItemRenderer
/*  29:    */ {
/*  30:    */   SculptureRenderBlocks rb;
/*  31:    */   RenderItem renderItem;
/*  32:    */   ItemStack is;
/*  33:    */   ExpirablePool<ItemStack, CompiledRender> renders;
/*  34:    */   
/*  35:    */   public DroppedSculptureRenderer()
/*  36:    */   {
/*  37: 32 */     this.rb = new SculptureRenderBlocks();
/*  38: 33 */     this.renderItem = new RenderItem();
/*  39:    */     
/*  40:    */ 
/*  41: 36 */     this.renders = new ExpirablePool(12)
/*  42:    */     {
/*  43:    */       public void release(DroppedSculptureRenderer.CompiledRender v)
/*  44:    */       {
/*  45: 40 */         v.clear();
/*  46:    */       }
/*  47:    */       
/*  48:    */       public DroppedSculptureRenderer.CompiledRender get()
/*  49:    */       {
/*  50: 45 */         return new DroppedSculptureRenderer.CompiledRender(DroppedSculptureRenderer.this, null);
/*  51:    */       }
/*  52:    */     };
/*  53:    */   }
/*  54:    */   
/*  55:    */   public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type)
/*  56:    */   {
/*  57: 54 */     return (type == IItemRenderer.ItemRenderType.INVENTORY) || (type == IItemRenderer.ItemRenderType.ENTITY) || (type == IItemRenderer.ItemRenderType.EQUIPPED) || (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper)
/*  61:    */   {
/*  62: 63 */     return type == IItemRenderer.ItemRenderType.ENTITY;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data)
/*  66:    */   {
/*  67: 68 */     CompiledRender cr = (CompiledRender)this.renders.get(item);
/*  68: 69 */     if (!cr.compiled(type)) {
/*  69: 69 */       cr.compile(item.func_77978_p(), type, data);
/*  70:    */     }
/*  71: 70 */     TextureManager tm = Minecraft.func_71410_x().field_71446_o;
/*  72: 71 */     tm.func_110577_a(TextureMap.field_110575_b);
/*  73: 72 */     GL11.glCallList(cr.glDispList);
/*  74:    */   }
/*  75:    */   
/*  76:    */   private class CompiledRender
/*  77:    */   {
/*  78: 77 */     int glDispList = -1;
/*  79: 78 */     IItemRenderer.ItemRenderType type = null;
/*  80: 79 */     Sculpture sculpture = new Sculpture();
/*  81:    */     
/*  82:    */     private CompiledRender() {}
/*  83:    */     
/*  84:    */     public boolean compiled(IItemRenderer.ItemRenderType type)
/*  85:    */     {
/*  86: 82 */       return (this.glDispList >= 0) && (this.type == type);
/*  87:    */     }
/*  88:    */     
/*  89:    */     public void clear()
/*  90:    */     {
/*  91: 86 */       if (this.glDispList >= 0) {
/*  92: 87 */         GLAllocation.func_74523_b(this.glDispList);
/*  93:    */       }
/*  94:    */     }
/*  95:    */     
/*  96:    */     public void compile(NBTTagCompound nbt, IItemRenderer.ItemRenderType type, Object... data)
/*  97:    */     {
/*  98: 91 */       this.type = type;
/*  99: 92 */       this.sculpture.read(nbt);
/* 100: 94 */       if (this.glDispList < 0) {
/* 101: 94 */         this.glDispList = GLAllocation.func_74526_a(1);
/* 102:    */       }
/* 103: 95 */       if (DroppedSculptureRenderer.this.is == null) {
/* 104: 95 */         DroppedSculptureRenderer.this.is = new ItemStack(ModMinePainter.sculpture.block);
/* 105:    */       }
/* 106: 97 */       GL11.glNewList(this.glDispList, 4864);
/* 107: 98 */       TextureManager tm = Minecraft.func_71410_x().field_71446_o;
/* 108: 99 */       tm.func_110577_a(TextureMap.field_110575_b);
/* 109:100 */       SculptureBlock sb = (SculptureBlock)ModMinePainter.sculpture.block;
/* 110:102 */       if (type == IItemRenderer.ItemRenderType.INVENTORY) {
/* 111:103 */         RenderHelper.func_74520_c();
/* 112:    */       }
/* 113:106 */       for (int i = 0; i < 512; i++)
/* 114:    */       {
/* 115:107 */         int x = i >> 6 & 0x7;
/* 116:108 */         int y = i >> 3 & 0x7;
/* 117:109 */         int z = i >> 0 & 0x7;
/* 118:111 */         if (this.sculpture.getBlockAt(x, y, z, null) != Blocks.field_150350_a)
/* 119:    */         {
/* 120:113 */           sb.setCurrentBlock(this.sculpture.getBlockAt(x, y, z, null), this.sculpture.getMetaAt(x, y, z, null));
/* 121:114 */           sb.func_149676_a(x / 8.0F, y / 8.0F, z / 8.0F, (x + 1) / 8.0F, (y + 1) / 8.0F, (z + 1) / 8.0F);
/* 122:116 */           if (type == IItemRenderer.ItemRenderType.INVENTORY)
/* 123:    */           {
/* 124:120 */             GL11.glPushMatrix();
/* 125:121 */             GL11.glEnable(3042);
/* 126:122 */             OpenGlHelper.func_148821_a(770, 771, 1, 0);
/* 127:123 */             GL11.glTranslatef(-2.0F, 3.0F, 47.0F);
/* 128:124 */             GL11.glScalef(10.0F, 10.0F, 10.0F);
/* 129:125 */             GL11.glTranslatef(1.0F, 0.5F, 1.0F);
/* 130:126 */             GL11.glScalef(1.0F, 1.0F, -1.0F);
/* 131:127 */             GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
/* 132:128 */             GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
/* 133:129 */             GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
/* 134:130 */             DroppedSculptureRenderer.this.rb.cull(this.sculpture, x, y, z);
/* 135:131 */             DroppedSculptureRenderer.this.rb.func_147800_a(sb, 0, 1.0F);
/* 136:132 */             GL11.glEnable(2884);
/* 137:133 */             GL11.glPopMatrix();
/* 138:    */           }
/* 139:    */           else
/* 140:    */           {
/* 141:135 */             GL11.glPushMatrix();
/* 142:136 */             DroppedSculptureRenderer.this.rb.cull(this.sculpture, x, y, z);
/* 143:137 */             DroppedSculptureRenderer.this.rb.func_147800_a(sb, 0, 1.0F);
/* 144:138 */             GL11.glPopMatrix();
/* 145:    */           }
/* 146:    */         }
/* 147:    */       }
/* 148:142 */       GL11.glEndList();
/* 149:    */       
/* 150:144 */       sb.setCurrentBlock(null, 0);
/* 151:145 */       sb.func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/* 152:    */     }
/* 153:    */   }
/* 154:    */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.item.DroppedSculptureRenderer
 * JD-Core Version:    0.7.0.1
 */