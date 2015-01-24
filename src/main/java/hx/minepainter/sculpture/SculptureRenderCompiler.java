/*   1:    */ package hx.minepainter.sculpture;
/*   2:    */ 
/*   3:    */ import cpw.mods.fml.common.ObfuscationReflectionHelper;
/*   4:    */ import cpw.mods.fml.relauncher.Side;
/*   5:    */ import cpw.mods.fml.relauncher.SideOnly;
/*   6:    */ import hx.minepainter.ModMinePainter;
/*   7:    */ import hx.utils.BlockLoader;
/*   8:    */ import java.lang.reflect.Field;
/*   9:    */ import net.minecraft.block.Block;
/*  10:    */ import net.minecraft.client.Minecraft;
/*  11:    */ import net.minecraft.client.renderer.GLAllocation;
/*  12:    */ import net.minecraft.client.renderer.RenderBlocks;
/*  13:    */ import net.minecraft.client.renderer.Tessellator;
/*  14:    */ import net.minecraft.client.renderer.texture.TextureManager;
/*  15:    */ import net.minecraft.client.renderer.texture.TextureMap;
/*  16:    */ import net.minecraft.init.Blocks;
/*  17:    */ import net.minecraft.world.IBlockAccess;
/*  18:    */ import org.lwjgl.opengl.GL11;
/*  19:    */ 
/*  20:    */ @SideOnly(Side.CLIENT)
/*  21:    */ public class SculptureRenderCompiler
/*  22:    */ {
/*  23: 26 */   public static RenderBlocks rb = new SculptureRenderBlocks();
/*  24: 28 */   int glDisplayList = -1;
/*  25:    */   int light;
/*  26: 30 */   public boolean changed = true;
/*  27: 31 */   boolean context = false;
/*  28: 32 */   float[][][] neighborAO = new float[3][3][3];
/*  29:    */   
/*  30:    */   public void updateAO(IBlockAccess w, int x, int y, int z)
/*  31:    */   {
/*  32: 35 */     for (int i = 0; i < 27; i++)
/*  33:    */     {
/*  34: 36 */       int dx = i % 3;
/*  35: 37 */       int dy = i / 3 % 3;
/*  36: 38 */       int dz = i / 9 % 3;
/*  37:    */       
/*  38: 40 */       float ao = w.func_147439_a(x + dx - 1, y + dy - 1, z + dz - 1).func_149685_I();
/*  39: 41 */       if (ao != this.neighborAO[dx][dy][dz])
/*  40:    */       {
/*  41: 42 */         this.changed = true;
/*  42: 43 */         this.neighborAO[dx][dy][dz] = ao;
/*  43:    */       }
/*  44:    */     }
/*  45: 46 */     this.context = true;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void updateLight(int light)
/*  49:    */   {
/*  50: 50 */     if (light != this.light) {
/*  51: 51 */       this.changed = true;
/*  52:    */     }
/*  53: 52 */     this.light = light;
/*  54: 53 */     this.context = true;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public boolean hasContext()
/*  58:    */   {
/*  59: 57 */     return this.context;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public boolean update(BlockSlice slice)
/*  63:    */   {
/*  64: 61 */     if ((this.glDisplayList != -1) && (!this.changed)) {
/*  65: 61 */       return false;
/*  66:    */     }
/*  67: 63 */     if (this.glDisplayList < 0) {
/*  68: 63 */       this.glDisplayList = GLAllocation.func_74526_a(1);
/*  69:    */     }
/*  70: 64 */     GL11.glPushMatrix();
/*  71: 65 */     GL11.glNewList(this.glDisplayList, 4864);
/*  72: 66 */     build(slice);
/*  73: 67 */     GL11.glEndList();
/*  74: 68 */     GL11.glPopMatrix();
/*  75:    */     
/*  76: 70 */     this.changed = false;
/*  77: 71 */     return true;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void build(BlockSlice slice)
/*  81:    */   {
/*  82: 75 */     rb.field_147845_a = slice;
/*  83: 76 */     rb.field_147837_f = false;
/*  84: 77 */     SculptureBlock sculpture = (SculptureBlock)ModMinePainter.sculpture.block;
/*  85:    */     
/*  86: 79 */     TextureManager tm = Minecraft.func_71410_x().field_71446_o;
/*  87: 80 */     tm.func_110577_a(TextureMap.field_110575_b);
/*  88:    */     
/*  89: 82 */     Tessellator tes = Tessellator.field_78398_a;
/*  90: 83 */     double[] offs = getTesOffsets();
/*  91: 84 */     tes.func_78373_b(0.0D, 0.0D, 0.0D);
/*  92: 85 */     tes.func_78382_b();
/*  93: 88 */     for (int i = 0; i < 512; i++)
/*  94:    */     {
/*  95: 89 */       int x = i >> 6 & 0x7;
/*  96: 90 */       int y = i >> 3 & 0x7;
/*  97: 91 */       int z = i >> 0 & 0x7;
/*  98:    */       
/*  99: 93 */       Block b = slice.func_147439_a(x, y, z);
/* 100: 94 */       if (b != Blocks.field_150350_a)
/* 101:    */       {
/* 102: 95 */         int meta = slice.func_72805_g(x, y, z);
/* 103: 96 */         sculpture.setCurrentBlock(b, meta);
/* 104:    */         
/* 105: 98 */         tes.func_78373_b(-x, -y, -z);
/* 106:    */         
/* 107:100 */         sculpture.func_149676_a(x / 8.0F, y / 8.0F, z / 8.0F, (x + 1) / 8.0F, (y + 1) / 8.0F, (z + 1) / 8.0F);
/* 108:101 */         rb.func_147805_b(sculpture, x, y, z);
/* 109:    */       }
/* 110:    */     }
/* 111:104 */     ((SculptureBlock)ModMinePainter.sculpture.block).setCurrentBlock(null, 0);
/* 112:105 */     sculpture.func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/* 113:106 */     rb.field_147845_a = null;
/* 114:107 */     tes.func_78381_a();
/* 115:108 */     tes.func_78373_b(offs[0], offs[1], offs[2]);
/* 116:    */   }
/* 117:    */   
/* 118:    */   public void clear()
/* 119:    */   {
/* 120:112 */     if (this.glDisplayList >= 0) {
/* 121:113 */       GL11.glDeleteLists(this.glDisplayList, 1);
/* 122:    */     }
/* 123:    */   }
/* 124:    */   
/* 125:    */   public boolean ready()
/* 126:    */   {
/* 127:118 */     return this.glDisplayList >= 0;
/* 128:    */   }
/* 129:    */   
/* 130:    */   private double[] getTesOffsets()
/* 131:    */   {
/* 132:122 */     double[] off = new double[3];
/* 133:    */     
/* 134:124 */     int count = 0;
/* 135:125 */     int xoff = 0;
/* 136:126 */     Field[] fields = Tessellator.class.getDeclaredFields();
/* 137:127 */     for (int i = 0; i < fields.length; i++) {
/* 138:128 */       if (fields[i].getType() == Double.TYPE)
/* 139:    */       {
/* 140:129 */         count++;
/* 141:130 */         if (count == 3) {
/* 142:130 */           xoff = i - 2;
/* 143:    */         }
/* 144:    */       }
/* 145:    */       else
/* 146:    */       {
/* 147:132 */         count = 0;
/* 148:    */       }
/* 149:    */     }
/* 150:134 */     off[0] = ((Double)ObfuscationReflectionHelper.getPrivateValue(Tessellator.class, Tessellator.field_78398_a, xoff)).doubleValue();
/* 151:135 */     off[1] = ((Double)ObfuscationReflectionHelper.getPrivateValue(Tessellator.class, Tessellator.field_78398_a, xoff + 1)).doubleValue();
/* 152:136 */     off[2] = ((Double)ObfuscationReflectionHelper.getPrivateValue(Tessellator.class, Tessellator.field_78398_a, xoff + 2)).doubleValue();
/* 153:    */     
/* 154:138 */     return off;
/* 155:    */   }
/* 156:    */   
/* 157:    */   public void initFromSculptureAndLight(Sculpture sculpture, int light)
/* 158:    */   {
/* 159:142 */     update(BlockSlice.of(sculpture, light));
/* 160:    */   }
/* 161:    */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.sculpture.SculptureRenderCompiler
 * JD-Core Version:    0.7.0.1
 */