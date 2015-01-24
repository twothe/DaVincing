/*   1:    */ package hx.minepainter.painting;
/*   2:    */ 
/*   3:    */ import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
/*   4:    */ import cpw.mods.fml.relauncher.Side;
/*   5:    */ import cpw.mods.fml.relauncher.SideOnly;
/*   6:    */ import hx.minepainter.ModMinePainter;
/*   7:    */ import hx.minepainter.item.Palette;
/*   8:    */ import hx.utils.BlockLoader;
/*   9:    */ import hx.utils.Utils;
/*  10:    */ import java.awt.image.BufferedImage;
/*  11:    */ import net.minecraft.block.Block;
/*  12:    */ import net.minecraft.block.material.Material;
/*  13:    */ import net.minecraft.client.renderer.texture.IIconRegister;
/*  14:    */ import net.minecraft.entity.player.EntityPlayer;
/*  15:    */ import net.minecraft.entity.player.InventoryPlayer;
/*  16:    */ import net.minecraft.init.Items;
/*  17:    */ import net.minecraft.item.Item;
/*  18:    */ import net.minecraft.item.ItemStack;
/*  19:    */ import net.minecraft.util.IIcon;
/*  20:    */ import net.minecraft.util.MovingObjectPosition;
/*  21:    */ import net.minecraft.world.World;
/*  22:    */ import net.minecraftforge.common.util.ForgeDirection;
/*  23:    */ 
/*  24:    */ public class PaintTool
/*  25:    */   extends Item
/*  26:    */ {
/*  27:    */   public PaintTool()
/*  28:    */   {
/*  29: 26 */     func_77637_a(ModMinePainter.tabMinePainter);
/*  30: 27 */     func_77625_d(1);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public boolean func_77648_a(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs)
/*  34:    */   {
/*  35: 32 */     if (!w.field_72995_K) {
/*  36: 32 */       return false;
/*  37:    */     }
/*  38: 34 */     boolean changed = paintAt(w, x, y, z, xs, ys, zs, getColor(ep, is));
/*  39: 36 */     if (changed) {
/*  40: 36 */       ModMinePainter.network.sendToServer(new PaintingOperationMessage(this, x, y, z, xs, ys, zs, getColor(ep, is)));
/*  41:    */     }
/*  42: 38 */     return changed;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public int getColor(EntityPlayer ep, ItemStack is)
/*  46:    */   {
/*  47: 42 */     int size = ep.field_71071_by.func_70302_i_();
/*  48: 43 */     for (int i = 0; i < size; i++)
/*  49:    */     {
/*  50: 44 */       ItemStack slot = ep.field_71071_by.func_70301_a(i);
/*  51: 45 */       if ((slot != null) && 
/*  52: 46 */         ((slot.func_77973_b() instanceof Palette))) {
/*  53: 48 */         return Palette.getColors(slot)[0];
/*  54:    */       }
/*  55:    */     }
/*  56: 50 */     return 0;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public boolean apply(BufferedImage img, float[] point, int color)
/*  60:    */   {
/*  61: 54 */     return false;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public boolean inBounds(int x, int y)
/*  65:    */   {
/*  66: 58 */     return (x >= 0) && (x < 16) && (y >= 0) && (y < 16);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public boolean paintAt(World w, int x, int y, int z, float xs, float ys, float zs, int color)
/*  70:    */   {
/*  71: 62 */     if (w.func_147439_a(x, y, z) != ModMinePainter.painting.block) {
/*  72: 62 */       return false;
/*  73:    */     }
/*  74: 63 */     PaintingEntity pe = (PaintingEntity)Utils.getTE(w, x, y, z);
/*  75: 64 */     if (pe == null) {
/*  76: 64 */       return false;
/*  77:    */     }
/*  78: 65 */     PaintingPlacement place = PaintingPlacement.of(w.func_72805_g(x, y, z));
/*  79: 66 */     float[] point = place.block2painting(xs, ys, zs);
/*  80:    */     
/*  81: 68 */     boolean changed = false;
/*  82: 69 */     for (int i = -1; i <= 1; i++) {
/*  83: 70 */       for (int j = -1; j <= 1; j++)
/*  84:    */       {
/*  85: 71 */         int _x = x + place.xpos.offsetX * i + place.ypos.offsetX * j;
/*  86: 72 */         int _y = y + place.xpos.offsetY * i + place.ypos.offsetY * j;
/*  87: 73 */         int _z = z + place.xpos.offsetZ * i + place.ypos.offsetZ * j;
/*  88: 75 */         if ((w.func_147439_a(_x, _y, _z) == ModMinePainter.painting.block) && 
/*  89: 76 */           (w.func_72805_g(_x, _y, _z) == place.ordinal()))
/*  90:    */         {
/*  91: 78 */           PaintingEntity painting = (PaintingEntity)Utils.getTE(w, _x, _y, _z);
/*  92:    */           
/*  93: 80 */           point[0] -= i;
/*  94: 81 */           point[1] -= j;
/*  95: 82 */           boolean _changed = apply(painting.image, point, color);
/*  96: 83 */           point[0] += i;
/*  97: 84 */           point[1] += j;
/*  98: 86 */           if (_changed)
/*  99:    */           {
/* 100: 87 */             if (w.field_72995_K) {
/* 101: 87 */               pe.getIcon().fill(pe.image);
/* 102:    */             } else {
/* 103: 88 */               w.func_147471_g(_x, _y, _z);
/* 104:    */             }
/* 105: 89 */             changed = true;
/* 106:    */           }
/* 107:    */         }
/* 108:    */       }
/* 109:    */     }
/* 110: 92 */     return changed;
/* 111:    */   }
/* 112:    */   
/* 113:    */   public static class Mini
/* 114:    */     extends PaintTool
/* 115:    */   {
/* 116:    */     public Mini()
/* 117:    */     {
/* 118: 99 */       func_77655_b("mini_brush");
/* 119:100 */       func_111206_d("minepainter:brush_small");
/* 120:    */     }
/* 121:    */     
/* 122:    */     public boolean apply(BufferedImage img, float[] point, int color)
/* 123:    */     {
/* 124:105 */       int x = (int)(point[0] * 16.0F + 16.0F) - 16;
/* 125:106 */       int y = (int)(point[1] * 16.0F + 16.0F) - 16;
/* 126:108 */       if (!inBounds(x, y)) {
/* 127:108 */         return false;
/* 128:    */       }
/* 129:110 */       img.setRGB(x, y, color);
/* 130:111 */       return true;
/* 131:    */     }
/* 132:    */   }
/* 133:    */   
/* 134:    */   public static class Bucket
/* 135:    */     extends PaintTool
/* 136:    */   {
/* 137:    */     IIcon fill;
/* 138:    */     
/* 139:    */     public Bucket()
/* 140:    */     {
/* 141:119 */       func_77655_b("paint_bucket");
/* 142:120 */       func_111206_d("minepainter:bucket");
/* 143:121 */       func_77627_a(true);
/* 144:    */     }
/* 145:    */     
/* 146:    */     public ItemStack func_77659_a(ItemStack is, World w, EntityPlayer ep)
/* 147:    */     {
/* 148:125 */       MovingObjectPosition mop = func_77621_a(w, ep, true);
/* 149:126 */       if (mop == null) {
/* 150:126 */         return is;
/* 151:    */       }
/* 152:127 */       Material m = w.func_147439_a(mop.field_72311_b, mop.field_72312_c, mop.field_72309_d).func_149688_o();
/* 153:128 */       if (m.func_76224_d()) {
/* 154:128 */         return new ItemStack(Items.field_151133_ar);
/* 155:    */       }
/* 156:129 */       return is;
/* 157:    */     }
/* 158:    */     
/* 159:    */     public boolean func_77623_v()
/* 160:    */     {
/* 161:133 */       return true;
/* 162:    */     }
/* 163:    */     
/* 164:    */     public int getRenderPasses(int metadata)
/* 165:    */     {
/* 166:137 */       return 2;
/* 167:    */     }
/* 168:    */     
/* 169:    */     public IIcon getIcon(ItemStack is, int renderPass)
/* 170:    */     {
/* 171:141 */       if (renderPass == 0) {
/* 172:141 */         return this.field_77791_bV;
/* 173:    */       }
/* 174:142 */       return this.fill;
/* 175:    */     }
/* 176:    */     
/* 177:    */     @SideOnly(Side.CLIENT)
/* 178:    */     public void func_94581_a(IIconRegister par1IconRegister)
/* 179:    */     {
/* 180:146 */       super.func_94581_a(par1IconRegister);
/* 181:147 */       this.fill = par1IconRegister.func_94245_a("minepainter:bucket_fill");
/* 182:    */     }
/* 183:    */     
/* 184:    */     public int func_82790_a(ItemStack par1ItemStack, int par2)
/* 185:    */     {
/* 186:151 */       if (par2 == 1) {
/* 187:151 */         return getColor(par1ItemStack.func_77960_j(), 0);
/* 188:    */       }
/* 189:152 */       return super.func_82790_a(par1ItemStack, par2);
/* 190:    */     }
/* 191:    */     
/* 192:    */     public int getColor(EntityPlayer ep, ItemStack is)
/* 193:    */     {
/* 194:156 */       return getColor(is.func_77960_j(), -16777216);
/* 195:    */     }
/* 196:    */     
/* 197:    */     private int getColor(int dmg, int mask)
/* 198:    */     {
/* 199:160 */       if (dmg < 16) {
/* 200:160 */         return net.minecraft.item.ItemDye.field_150922_c[dmg] | mask;
/* 201:    */       }
/* 202:161 */       return 16777215;
/* 203:    */     }
/* 204:    */     
/* 205:    */     public boolean apply(BufferedImage img, float[] point, int color)
/* 206:    */     {
/* 207:166 */       int x = (int)(point[0] * 16.0F + 16.0F) - 16;
/* 208:167 */       int y = (int)(point[1] * 16.0F + 16.0F) - 16;
/* 209:169 */       if (!inBounds(x, y)) {
/* 210:169 */         return false;
/* 211:    */       }
/* 212:171 */       int from_color = img.getRGB(x, y);
/* 213:173 */       for (int i = 0; i < 256; i++)
/* 214:    */       {
/* 215:174 */         x = i / 16;
/* 216:175 */         y = i % 16;
/* 217:176 */         img.setRGB(x, y, color);
/* 218:    */       }
/* 219:179 */       return true;
/* 220:    */     }
/* 221:    */   }
/* 222:    */   
/* 223:    */   public static class Mixer
/* 224:    */     extends PaintTool
/* 225:    */   {
/* 226:    */     public Mixer()
/* 227:    */     {
/* 228:187 */       func_77655_b("mixer_brush").func_111206_d("minepainter:brush");
/* 229:    */     }
/* 230:    */     
/* 231:    */     public boolean apply(BufferedImage img, float[] point, int color)
/* 232:    */     {
/* 233:192 */       int x = (int)(point[0] * 16.0F + 16.0F) - 16;
/* 234:193 */       int y = (int)(point[1] * 16.0F + 16.0F) - 16;
/* 235:    */       
/* 236:195 */       int a75 = (int)((color >> 24 & 0xFF) * 0.75F) << 24;
/* 237:196 */       a75 += (color & 0xFFFFFF);
/* 238:197 */       int a50 = (int)((color >> 24 & 0xFF) * 0.5F) << 24;
/* 239:198 */       a50 += (color & 0xFFFFFF);
/* 240:    */       
/* 241:200 */       boolean changed = false;
/* 242:201 */       for (int i = -1; i <= 1; i++) {
/* 243:202 */         for (int j = -1; j <= 1; j++) {
/* 244:203 */           if (inBounds(x + i, y + j))
/* 245:    */           {
/* 246:204 */             changed = true;
/* 247:    */             
/* 248:206 */             int to_blend = Math.abs(i) + Math.abs(j);
/* 249:207 */             if (to_blend == 0) {
/* 250:207 */               to_blend = color;
/* 251:208 */             } else if (to_blend == 1) {
/* 252:208 */               to_blend = a75;
/* 253:    */             } else {
/* 254:209 */               to_blend = a50;
/* 255:    */             }
/* 256:211 */             img.setRGB(x + i, y + j, mix(to_blend, img.getRGB(x + i, y + j)));
/* 257:    */           }
/* 258:    */         }
/* 259:    */       }
/* 260:213 */       return changed;
/* 261:    */     }
/* 262:    */     
/* 263:    */     private int mix(int color, int original)
/* 264:    */     {
/* 265:217 */       float a_alpha = (color >> 24 & 0xFF) / 255.0F;
/* 266:218 */       float b_alpha = (original >> 24 & 0xFF) / 255.0F;
/* 267:219 */       float c_alpha = a_alpha + b_alpha * (1.0F - a_alpha);
/* 268:220 */       int result = 0;
/* 269:222 */       for (int b = 0; b < 24; b += 8)
/* 270:    */       {
/* 271:224 */         int ca = color >> b & 0xFF;
/* 272:225 */         ca = (int)(ca * a_alpha);
/* 273:226 */         int cb = original >> b & 0xFF;
/* 274:227 */         cb = (int)(cb * b_alpha * (1.0F - a_alpha));
/* 275:228 */         result += ((int)((ca + cb) / c_alpha) << b);
/* 276:    */       }
/* 277:231 */       result += ((int)(255.0F * c_alpha) << 24);
/* 278:232 */       return result;
/* 279:    */     }
/* 280:    */   }
/* 281:    */   
/* 282:    */   public static class Eraser
/* 283:    */     extends PaintTool.Mixer
/* 284:    */   {
/* 285:    */     public Eraser()
/* 286:    */     {
/* 287:240 */       func_77655_b("eraser").func_111206_d("minepainter:eraser");
/* 288:    */     }
/* 289:    */     
/* 290:    */     public boolean apply(BufferedImage img, float[] point, int color)
/* 291:    */     {
/* 292:245 */       int x = (int)(point[0] * 16.0F + 16.0F) - 16;
/* 293:246 */       int y = (int)(point[1] * 16.0F + 16.0F) - 16;
/* 294:    */       
/* 295:248 */       boolean changed = false;
/* 296:249 */       for (int i = -1; i <= 1; i++) {
/* 297:250 */         for (int j = -1; j <= 1; j++) {
/* 298:251 */           if (inBounds(x + i, y + j))
/* 299:    */           {
/* 300:252 */             changed = true;
/* 301:    */             
/* 302:254 */             color = img.getRGB(x + i, y + j);
/* 303:255 */             int a75 = (int)((color >> 24 & 0xFF) * 0.75F) << 24;
/* 304:256 */             a75 += (color & 0xFFFFFF);
/* 305:257 */             int a50 = (int)((color >> 24 & 0xFF) * 0.5F) << 24;
/* 306:258 */             a50 += (color & 0xFFFFFF);
/* 307:    */             
/* 308:260 */             int to_blend = Math.abs(i) + Math.abs(j);
/* 309:261 */             if (to_blend == 0) {
/* 310:261 */               to_blend = 0;
/* 311:262 */             } else if (to_blend == 1) {
/* 312:262 */               to_blend = a50;
/* 313:    */             } else {
/* 314:263 */               to_blend = a75;
/* 315:    */             }
/* 316:265 */             img.setRGB(x + i, y + j, to_blend);
/* 317:    */           }
/* 318:    */         }
/* 319:    */       }
/* 320:267 */       return changed;
/* 321:    */     }
/* 322:    */   }
/* 323:    */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.painting.PaintTool
 * JD-Core Version:    0.7.0.1
 */