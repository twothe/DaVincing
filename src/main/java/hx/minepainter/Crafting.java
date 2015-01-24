/*   1:    */ package hx.minepainter;
/*   2:    */ 
/*   3:    */ import cpw.mods.fml.common.registry.GameRegistry;
/*   4:    */ import hx.minepainter.item.PieceItem;
/*   5:    */ import hx.minepainter.painting.PaintTool.Bucket;
/*   6:    */ import hx.utils.ItemLoader;
/*   7:    */ import hx.utils.Utils;
/*   8:    */ import net.minecraft.block.Block;
/*   9:    */ import net.minecraft.init.Blocks;
/*  10:    */ import net.minecraft.init.Items;
/*  11:    */ import net.minecraft.inventory.InventoryCrafting;
/*  12:    */ import net.minecraft.item.ItemDye;
/*  13:    */ import net.minecraft.item.ItemStack;
/*  14:    */ import net.minecraft.item.crafting.IRecipe;
/*  15:    */ import net.minecraft.world.World;
/*  16:    */ 
/*  17:    */ public class Crafting
/*  18:    */ {
/*  19:    */   public void registerRecipes()
/*  20:    */   {
/*  21: 21 */     GameRegistry.addRecipe(new ItemStack(ModMinePainter.minibrush.item), new Object[] { "X  ", " Y ", "  Z", Character.valueOf('X'), new ItemStack(Blocks.field_150325_L), Character.valueOf('Y'), new ItemStack(Items.field_151055_y), Character.valueOf('Z'), new ItemStack(Items.field_151100_aR, 1, 1) });
/*  22:    */     
/*  23:    */ 
/*  24:    */ 
/*  25:    */ 
/*  26:    */ 
/*  27: 27 */     GameRegistry.addRecipe(new ItemStack(ModMinePainter.mixerbrush.item), new Object[] { "XX ", "XY ", "  Z", Character.valueOf('X'), new ItemStack(Blocks.field_150325_L), Character.valueOf('Y'), new ItemStack(Items.field_151055_y), Character.valueOf('Z'), new ItemStack(Items.field_151100_aR, 1, 1) });
/*  28:    */     
/*  29:    */ 
/*  30:    */ 
/*  31:    */ 
/*  32:    */ 
/*  33: 33 */     GameRegistry.addRecipe(new ItemStack(ModMinePainter.canvas.item), new Object[] { "XXX", "XXX", Character.valueOf('X'), new ItemStack(Blocks.field_150325_L) });
/*  34:    */     
/*  35:    */ 
/*  36:    */ 
/*  37: 37 */     GameRegistry.addRecipe(new ItemStack(ModMinePainter.chisel.item), new Object[] { "X ", " Y", Character.valueOf('X'), new ItemStack(Blocks.field_150347_e), Character.valueOf('Y'), new ItemStack(Items.field_151055_y) });
/*  38:    */     
/*  39:    */ 
/*  40:    */ 
/*  41:    */ 
/*  42: 42 */     GameRegistry.addRecipe(new ItemStack(ModMinePainter.barcutter.item), new Object[] { "X ", " Y", Character.valueOf('X'), new ItemStack(Items.field_151042_j), Character.valueOf('Y'), new ItemStack(ModMinePainter.chisel.item) });
/*  43:    */     
/*  44:    */ 
/*  45:    */ 
/*  46:    */ 
/*  47: 47 */     GameRegistry.addRecipe(new ItemStack(ModMinePainter.saw.item), new Object[] { "X ", " Y", Character.valueOf('X'), new ItemStack(Items.field_151045_i), Character.valueOf('Y'), new ItemStack(ModMinePainter.barcutter.item) });
/*  48:    */     
/*  49:    */ 
/*  50:    */ 
/*  51:    */ 
/*  52: 52 */     GameRegistry.addRecipe(new ItemStack(ModMinePainter.palette.item), new Object[] { "X", "Y", Character.valueOf('X'), new ItemStack(Blocks.field_150344_f), Character.valueOf('Y'), new ItemStack(ModMinePainter.chisel.item) });
/*  53:    */     
/*  54:    */ 
/*  55:    */ 
/*  56:    */ 
/*  57: 57 */     GameRegistry.addRecipe(new ItemStack(ModMinePainter.eraser.item), new Object[] { "XX ", "YY ", "ZZ ", Character.valueOf('X'), new ItemStack(Items.field_151123_aH), Character.valueOf('Y'), new ItemStack(Items.field_151121_aF), Character.valueOf('Z'), new ItemStack(Items.field_151100_aR, 1, 4) });
/*  58:    */     
/*  59:    */ 
/*  60:    */ 
/*  61:    */ 
/*  62:    */ 
/*  63: 63 */     GameRegistry.addRecipe(new ItemStack(ModMinePainter.wrench.item), new Object[] { "XX ", "YX ", " X ", Character.valueOf('X'), new ItemStack(Items.field_151042_j), Character.valueOf('Y'), new ItemStack(Items.field_151100_aR, 1, 1) });
/*  64:    */     
/*  65:    */ 
/*  66:    */ 
/*  67:    */ 
/*  68: 68 */     GameRegistry.addRecipe(this.scrap);
/*  69:    */     
/*  70: 70 */     GameRegistry.addRecipe(this.fillBucket);
/*  71:    */   }
/*  72:    */   
/*  73: 73 */   private IRecipe scrap = new IRecipe()
/*  74:    */   {
/*  75:    */     public boolean func_77569_a(InventoryCrafting ic, World w)
/*  76:    */     {
/*  77: 77 */       Block block = null;
/*  78: 78 */       int meta = 0;
/*  79: 79 */       int count = 0;
/*  80:    */       
/*  81: 81 */       int size = ic.func_70302_i_();
/*  82: 82 */       for (int i = 0; i < size; i++)
/*  83:    */       {
/*  84: 83 */         ItemStack is = ic.func_70301_a(i);
/*  85: 84 */         if ((is != null) && 
/*  86: 85 */           ((is.func_77973_b() instanceof PieceItem)))
/*  87:    */         {
/*  88: 86 */           PieceItem pi = (PieceItem)Utils.getItem(is);
/*  89: 87 */           if (block == null)
/*  90:    */           {
/*  91: 88 */             block = pi.getEditBlock(is);
/*  92: 89 */             meta = pi.getEditMeta(is);
/*  93:    */           }
/*  94: 91 */           if (block != pi.getEditBlock(is)) {
/*  95: 91 */             return false;
/*  96:    */           }
/*  97: 92 */           if (meta != pi.getEditMeta(is)) {
/*  98: 92 */             return false;
/*  99:    */           }
/* 100: 94 */           count += pi.getWorthPiece();
/* 101:    */         }
/* 102:    */       }
/* 103: 98 */       if (count == 0) {
/* 104: 98 */         return false;
/* 105:    */       }
/* 106: 99 */       if ((count % 512 == 0) && (count / 512 <= 64)) {
/* 107: 99 */         return true;
/* 108:    */       }
/* 109:100 */       if ((count % 64 == 0) && (count / 64 <= 64)) {
/* 110:100 */         return true;
/* 111:    */       }
/* 112:101 */       if ((count % 8 == 0) && (count / 8 <= 64)) {
/* 113:101 */         return true;
/* 114:    */       }
/* 115:102 */       if (count <= 64) {
/* 116:102 */         return true;
/* 117:    */       }
/* 118:104 */       return false;
/* 119:    */     }
/* 120:    */     
/* 121:    */     public ItemStack func_77572_b(InventoryCrafting ic)
/* 122:    */     {
/* 123:109 */       Block block = null;
/* 124:110 */       int meta = 0;
/* 125:111 */       int count = 0;
/* 126:    */       
/* 127:113 */       int size = ic.func_70302_i_();
/* 128:114 */       for (int i = 0; i < size; i++)
/* 129:    */       {
/* 130:115 */         ItemStack is = ic.func_70301_a(i);
/* 131:116 */         if ((is != null) && 
/* 132:117 */           ((is.func_77973_b() instanceof PieceItem)))
/* 133:    */         {
/* 134:118 */           PieceItem pi = (PieceItem)Utils.getItem(is);
/* 135:119 */           if (block == null)
/* 136:    */           {
/* 137:120 */             block = pi.getEditBlock(is);
/* 138:121 */             meta = pi.getEditMeta(is);
/* 139:    */           }
/* 140:124 */           count += pi.getWorthPiece();
/* 141:    */         }
/* 142:    */       }
/* 143:128 */       if ((count % 512 == 0) && (count / 512 <= 64)) {
/* 144:129 */         return new ItemStack(block, count / 512, meta);
/* 145:    */       }
/* 146:131 */       if ((count % 64 == 0) && (count / 64 <= 64)) {
/* 147:132 */         return new ItemStack(ModMinePainter.cover.item, count / 64, (Block.func_149682_b(block) << 4) + meta);
/* 148:    */       }
/* 149:134 */       if ((count % 8 == 0) && (count / 8 <= 64)) {
/* 150:135 */         return new ItemStack(ModMinePainter.bar.item, count / 8, (Block.func_149682_b(block) << 4) + meta);
/* 151:    */       }
/* 152:137 */       return new ItemStack(ModMinePainter.piece.item, count, (Block.func_149682_b(block) << 4) + meta);
/* 153:    */     }
/* 154:    */     
/* 155:    */     public int func_77570_a()
/* 156:    */     {
/* 157:142 */       return 0;
/* 158:    */     }
/* 159:    */     
/* 160:    */     public ItemStack func_77571_b()
/* 161:    */     {
/* 162:147 */       return null;
/* 163:    */     }
/* 164:    */   };
/* 165:151 */   private IRecipe fillBucket = new IRecipe()
/* 166:    */   {
/* 167:    */     public int func_77570_a()
/* 168:    */     {
/* 169:154 */       return 0;
/* 170:    */     }
/* 171:    */     
/* 172:    */     public ItemStack func_77571_b()
/* 173:    */     {
/* 174:159 */       return null;
/* 175:    */     }
/* 176:    */     
/* 177:    */     public boolean func_77569_a(InventoryCrafting ic, World w)
/* 178:    */     {
/* 179:164 */       ItemStack bucket = null;
/* 180:165 */       ItemStack dye = null;
/* 181:    */       
/* 182:167 */       int size = ic.func_70302_i_();
/* 183:168 */       for (int i = 0; i < size; i++)
/* 184:    */       {
/* 185:169 */         ItemStack is = ic.func_70301_a(i);
/* 186:170 */         if (is != null) {
/* 187:171 */           if (((is.func_77973_b() instanceof PaintTool.Bucket)) || (is.func_77973_b() == Items.field_151131_as))
/* 188:    */           {
/* 189:172 */             if (bucket != null) {
/* 190:172 */               return false;
/* 191:    */             }
/* 192:173 */             bucket = is;
/* 193:    */           }
/* 194:176 */           else if (((is.func_77973_b() instanceof ItemDye)) || (is.func_77973_b() == Items.field_151123_aH))
/* 195:    */           {
/* 196:177 */             if (dye != null) {
/* 197:177 */               return false;
/* 198:    */             }
/* 199:178 */             dye = is;
/* 200:    */           }
/* 201:    */           else
/* 202:    */           {
/* 203:181 */             return false;
/* 204:    */           }
/* 205:    */         }
/* 206:    */       }
/* 207:183 */       return (bucket != null) && (dye != null);
/* 208:    */     }
/* 209:    */     
/* 210:    */     public ItemStack func_77572_b(InventoryCrafting ic)
/* 211:    */     {
/* 212:188 */       ItemStack bucket = null;
/* 213:189 */       ItemStack dye = null;
/* 214:    */       
/* 215:191 */       int size = ic.func_70302_i_();
/* 216:192 */       for (int i = 0; i < size; i++)
/* 217:    */       {
/* 218:193 */         ItemStack is = ic.func_70301_a(i);
/* 219:194 */         if (is != null) {
/* 220:195 */           if (((is.func_77973_b() instanceof PaintTool.Bucket)) || (is.func_77973_b() == Items.field_151131_as))
/* 221:    */           {
/* 222:196 */             if (bucket != null) {
/* 223:196 */               return null;
/* 224:    */             }
/* 225:197 */             bucket = is;
/* 226:    */           }
/* 227:200 */           else if (((is.func_77973_b() instanceof ItemDye)) || (is.func_77973_b() == Items.field_151123_aH))
/* 228:    */           {
/* 229:201 */             if (dye != null) {
/* 230:201 */               return null;
/* 231:    */             }
/* 232:202 */             dye = is;
/* 233:    */           }
/* 234:    */         }
/* 235:    */       }
/* 236:206 */       ItemStack newbucket = new ItemStack(ModMinePainter.bucket.item);
/* 237:207 */       newbucket.func_77964_b(dye.func_77973_b() == Items.field_151123_aH ? 16 : dye.func_77960_j());
/* 238:208 */       return newbucket;
/* 239:    */     }
/* 240:    */   };
/* 241:    */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.Crafting
 * JD-Core Version:    0.7.0.1
 */