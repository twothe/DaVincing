/*   1:    */ package hx.minepainter.sculpture;
/*   2:    */ 
/*   3:    */ import hx.minepainter.ModMinePainter;
/*   4:    */ import hx.utils.BlockLoader;
/*   5:    */ import hx.utils.ItemLoader;
/*   6:    */ import hx.utils.Utils;
/*   7:    */ import java.util.LinkedList;
/*   8:    */ import java.util.List;
/*   9:    */ import net.minecraft.block.Block;
/*  10:    */ import net.minecraft.entity.player.EntityPlayer;
/*  11:    */ import net.minecraft.init.Blocks;
/*  12:    */ import net.minecraft.item.ItemStack;
/*  13:    */ import net.minecraft.util.Vec3;
/*  14:    */ import net.minecraft.world.World;
/*  15:    */ import net.minecraftforge.common.util.ForgeDirection;
/*  16:    */ 
/*  17:    */ public class Operations
/*  18:    */ {
/*  19:    */   static double length;
/*  20:    */   
/*  21:    */   public static int editSubBlock(World w, int[] minmax, int x, int y, int z, Block block, byte meta)
/*  22:    */   {
/*  23: 26 */     int s = 0;
/*  24:    */     
/*  25: 28 */     LinkedList<int[]> droplist = new LinkedList();
/*  26: 30 */     for (int _x = minmax[0]; _x < minmax[3]; _x++) {
/*  27: 31 */       for (int _y = minmax[1]; _y < minmax[4]; _y++) {
/*  28: 32 */         for (int _z = minmax[2]; _z < minmax[5]; _z++)
/*  29:    */         {
/*  30: 34 */           int tx = x;int ty = y;int tz = z;
/*  31: 36 */           for (; _x > 7; tx++) {
/*  32: 36 */             _x -= 8;
/*  33:    */           }
/*  34: 37 */           for (; _y > 7; ty++) {
/*  35: 37 */             _y -= 8;
/*  36:    */           }
/*  37: 38 */           for (; _z > 7; tz++) {
/*  38: 38 */             _z -= 8;
/*  39:    */           }
/*  40: 39 */           for (; _x < 0; tx--) {
/*  41: 39 */             _x += 8;
/*  42:    */           }
/*  43: 40 */           for (; _y < 0; ty--) {
/*  44: 40 */             _y += 8;
/*  45:    */           }
/*  46: 41 */           for (; _z < 0; tz--) {
/*  47: 41 */             _z += 8;
/*  48:    */           }
/*  49: 43 */           Block tgt_block = w.func_147439_a(tx, ty, tz);
/*  50: 44 */           int tgt_meta = w.func_72805_g(tx, ty, tz);
/*  51: 46 */           if ((tgt_block == Blocks.field_150350_a) && (block != Blocks.field_150350_a)) {
/*  52: 47 */             w.func_147449_b(tx, ty, tz, ModMinePainter.sculpture.block);
/*  53: 48 */           } else if (sculptable(tgt_block, tgt_meta)) {
/*  54: 49 */             convertToFullSculpture(w, tx, ty, tz);
/*  55:    */           }
/*  56: 51 */           if (w.func_147439_a(tx, ty, tz) == ModMinePainter.sculpture.block)
/*  57:    */           {
/*  58: 54 */             SculptureEntity se = (SculptureEntity)w.func_147438_o(tx, ty, tz);
/*  59: 55 */             Block former = se.sculpture.getBlockAt(_x, _y, _z, null);
/*  60: 56 */             int metaFormer = se.sculpture.getMetaAt(_x, _y, _z, null);
/*  61: 57 */             addDrop(droplist, former, metaFormer);
/*  62: 58 */             se.sculpture.setBlockAt(_x, _y, _z, block, meta);
/*  63: 59 */             if (se.sculpture.isEmpty()) {
/*  64: 59 */               w.func_147449_b(x, y, z, Blocks.field_150350_a);
/*  65:    */             }
/*  66: 60 */             if (w.field_72995_K) {
/*  67: 60 */               se.getRender().changed = true;
/*  68:    */             } else {
/*  69: 61 */               w.func_147471_g(tx, ty, tz);
/*  70:    */             }
/*  71: 62 */             s++;
/*  72:    */           }
/*  73:    */         }
/*  74:    */       }
/*  75:    */     }
/*  76: 64 */     for (int[] drop : droplist) {
/*  77: 65 */       if (drop[0] != 0) {
/*  78: 66 */         dropScrap(w, x, y, z, Block.func_149729_e(drop[0]), (byte)drop[1], drop[2]);
/*  79:    */       }
/*  80:    */     }
/*  81: 69 */     return s;
/*  82:    */   }
/*  83:    */   
/*  84:    */   private static void addDrop(List<int[]> drops, Block block, int meta)
/*  85:    */   {
/*  86: 73 */     int id = Block.func_149682_b(block);
/*  87: 74 */     for (int[] drop : drops) {
/*  88: 75 */       if ((drop[0] == id) && (drop[1] == meta))
/*  89:    */       {
/*  90: 76 */         drop[2] += 1;
/*  91: 77 */         return;
/*  92:    */       }
/*  93:    */     }
/*  94: 80 */     drops.add(new int[] { id, meta, 1 });
/*  95:    */   }
/*  96:    */   
/*  97:    */   public static void dropScrap(World w, int x, int y, int z, Block block, byte meta, int amount)
/*  98:    */   {
/*  99: 85 */     if (block == Blocks.field_150350_a) {
/* 100: 85 */       return;
/* 101:    */     }
/* 102: 87 */     int covers = amount / 64;
/* 103: 88 */     amount %= 64;
/* 104: 89 */     int bars = amount / 8;
/* 105: 90 */     amount %= 8;
/* 106: 92 */     if (covers > 0)
/* 107:    */     {
/* 108: 93 */       ItemStack is = new ItemStack(ModMinePainter.cover.item);
/* 109: 94 */       is.field_77994_a = covers;
/* 110: 95 */       is.func_77964_b((Block.func_149682_b(block) << 4) + meta);
/* 111: 96 */       ((SculptureBlock)ModMinePainter.sculpture.block).dropScrap(w, x, y, z, is);
/* 112:    */     }
/* 113: 99 */     if (bars > 0)
/* 114:    */     {
/* 115:100 */       ItemStack is = new ItemStack(ModMinePainter.bar.item);
/* 116:101 */       is.field_77994_a = bars;
/* 117:102 */       is.func_77964_b((Block.func_149682_b(block) << 4) + meta);
/* 118:103 */       ((SculptureBlock)ModMinePainter.sculpture.block).dropScrap(w, x, y, z, is);
/* 119:    */     }
/* 120:106 */     if (amount > 0)
/* 121:    */     {
/* 122:107 */       ItemStack is = new ItemStack(ModMinePainter.piece.item);
/* 123:108 */       is.field_77994_a = amount;
/* 124:109 */       is.func_77964_b((Block.func_149682_b(block) << 4) + meta);
/* 125:110 */       ((SculptureBlock)ModMinePainter.sculpture.block).dropScrap(w, x, y, z, is);
/* 126:    */     }
/* 127:    */   }
/* 128:    */   
/* 129:    */   public static boolean sculptable(Block b, int blockMeta)
/* 130:    */   {
/* 131:116 */     if (b == null) {
/* 132:116 */       return false;
/* 133:    */     }
/* 134:118 */     if (b == Blocks.field_150349_c) {
/* 135:118 */       return false;
/* 136:    */     }
/* 137:119 */     if (b == Blocks.field_150357_h) {
/* 138:119 */       return false;
/* 139:    */     }
/* 140:120 */     if (b == Blocks.field_150434_aF) {
/* 141:120 */       return false;
/* 142:    */     }
/* 143:121 */     if (b == Blocks.field_150359_w) {
/* 144:121 */       return true;
/* 145:    */     }
/* 146:122 */     if (b == Blocks.field_150399_cn) {
/* 147:122 */       return true;
/* 148:    */     }
/* 149:123 */     if (b == Blocks.field_150362_t) {
/* 150:123 */       return false;
/* 151:    */     }
/* 152:125 */     if (b.hasTileEntity(blockMeta)) {
/* 153:125 */       return false;
/* 154:    */     }
/* 155:126 */     if (!b.func_149686_d()) {
/* 156:126 */       return false;
/* 157:    */     }
/* 158:128 */     if (b.func_149753_y() != 1.0D) {
/* 159:128 */       return false;
/* 160:    */     }
/* 161:129 */     if (b.func_149669_A() != 1.0D) {
/* 162:129 */       return false;
/* 163:    */     }
/* 164:130 */     if (b.func_149693_C() != 1.0D) {
/* 165:130 */       return false;
/* 166:    */     }
/* 167:131 */     if (b.func_149704_x() != 0.0D) {
/* 168:131 */       return false;
/* 169:    */     }
/* 170:132 */     if (b.func_149665_z() != 0.0D) {
/* 171:132 */       return false;
/* 172:    */     }
/* 173:133 */     if (b.func_149706_B() != 0.0D) {
/* 174:133 */       return false;
/* 175:    */     }
/* 176:136 */     return true;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public static void convertToFullSculpture(World w, int x, int y, int z)
/* 180:    */   {
/* 181:140 */     Block was = w.func_147439_a(x, y, z);
/* 182:141 */     int meta = w.func_72805_g(x, y, z);
/* 183:142 */     w.func_147449_b(x, y, z, ModMinePainter.sculpture.block);
/* 184:143 */     SculptureEntity se = (SculptureEntity)w.func_147438_o(x, y, z);
/* 185:144 */     for (int i = 0; i < 512; i++) {
/* 186:145 */       se.sculpture.setBlockAt(i >> 6 & 0x7, i >> 3 & 0x7, i >> 0 & 0x7, was, (byte)meta);
/* 187:    */     }
/* 188:    */   }
/* 189:    */   
/* 190:150 */   static int[] xyzf = { -1, -1, -1, -1 };
/* 191:    */   public static final int PLACE = 1;
/* 192:    */   public static final int ALLX = 2;
/* 193:    */   public static final int ALLY = 4;
/* 194:    */   public static final int ALLZ = 8;
/* 195:    */   public static final int DAMAGE = 16;
/* 196:    */   public static final int CONSUME = 32;
/* 197:    */   
/* 198:    */   public static int[] raytrace(int x, int y, int z, EntityPlayer ep)
/* 199:    */   {
/* 200:153 */     Block sculpture = ep.field_70170_p.func_147439_a(x, y, z);
/* 201:154 */     Sculpture the_sculpture = null;
/* 202:155 */     if (sculpture == ModMinePainter.sculpture.block)
/* 203:    */     {
/* 204:156 */       SculptureEntity se = (SculptureEntity)Utils.getTE(ep.field_70170_p, x, y, z);
/* 205:157 */       the_sculpture = se.sculpture();
/* 206:    */     }
/* 207:160 */     Vec3 from = ep.func_70666_h(1.0F);
/* 208:161 */     from = from.func_72441_c(-x, -y, -z);
/* 209:162 */     Vec3 look = ep.func_70040_Z();
/* 210:    */     
/* 211:164 */     return raytrace(the_sculpture, from, from.func_72441_c(look.field_72450_a * 5.0D, look.field_72448_b * 5.0D, look.field_72449_c * 5.0D));
/* 212:    */   }
/* 213:    */   
/* 214:    */   public static int[] raytrace(Sculpture sculpture, Vec3 start, Vec3 end)
/* 215:    */   {
/* 216:168 */     byte tmp21_20 = (xyzf[2] = xyzf[3] = -1);xyzf[1] = tmp21_20;xyzf[0] = tmp21_20;
/* 217:169 */     length = 1.7976931348623157E+308D;
/* 218:171 */     for (int x = 0; x <= 8; x++)
/* 219:    */     {
/* 220:173 */       Vec3 hit = start.func_72429_b(end, x / 8.0F);
/* 221:174 */       if (hit != null) {
/* 222:176 */         if ((hit.field_72448_b >= 0.0D) && 
/* 223:177 */           (hit.field_72449_c >= 0.0D))
/* 224:    */         {
/* 225:178 */           int y = (int)(hit.field_72448_b * 8.0D);
/* 226:179 */           int z = (int)(hit.field_72449_c * 8.0D);
/* 227:181 */           if (end.field_72450_a > start.field_72450_a) {
/* 228:181 */             updateRaytraceResult(sculpture, x, y, z, ForgeDirection.WEST.ordinal(), hit.func_72444_a(start).func_72433_c());
/* 229:    */           } else {
/* 230:183 */             updateRaytraceResult(sculpture, x - 1, y, z, ForgeDirection.EAST.ordinal(), hit.func_72444_a(start).func_72433_c());
/* 231:    */           }
/* 232:    */         }
/* 233:    */       }
/* 234:    */     }
/* 235:187 */     for (int y = 0; y <= 8; y++)
/* 236:    */     {
/* 237:189 */       Vec3 hit = start.func_72435_c(end, y / 8.0F);
/* 238:190 */       if (hit != null) {
/* 239:192 */         if ((hit.field_72450_a >= 0.0D) && 
/* 240:193 */           (hit.field_72449_c >= 0.0D))
/* 241:    */         {
/* 242:194 */           int x = (int)(hit.field_72450_a * 8.0D);
/* 243:195 */           int z = (int)(hit.field_72449_c * 8.0D);
/* 244:197 */           if (end.field_72448_b > start.field_72448_b) {
/* 245:197 */             updateRaytraceResult(sculpture, x, y, z, ForgeDirection.DOWN.ordinal(), hit.func_72444_a(start).func_72433_c());
/* 246:    */           } else {
/* 247:199 */             updateRaytraceResult(sculpture, x, y - 1, z, ForgeDirection.UP.ordinal(), hit.func_72444_a(start).func_72433_c());
/* 248:    */           }
/* 249:    */         }
/* 250:    */       }
/* 251:    */     }
/* 252:203 */     for (int z = 0; z <= 8; z++)
/* 253:    */     {
/* 254:205 */       Vec3 hit = start.func_72434_d(end, z / 8.0F);
/* 255:206 */       if (hit != null) {
/* 256:208 */         if ((hit.field_72450_a >= 0.0D) && 
/* 257:209 */           (hit.field_72448_b >= 0.0D))
/* 258:    */         {
/* 259:210 */           int x = (int)(hit.field_72450_a * 8.0D);
/* 260:211 */           int y = (int)(hit.field_72448_b * 8.0D);
/* 261:213 */           if (end.field_72449_c > start.field_72449_c) {
/* 262:213 */             updateRaytraceResult(sculpture, x, y, z, ForgeDirection.NORTH.ordinal(), hit.func_72444_a(start).func_72433_c());
/* 263:    */           } else {
/* 264:215 */             updateRaytraceResult(sculpture, x, y, z - 1, ForgeDirection.SOUTH.ordinal(), hit.func_72444_a(start).func_72433_c());
/* 265:    */           }
/* 266:    */         }
/* 267:    */       }
/* 268:    */     }
/* 269:219 */     return xyzf;
/* 270:    */   }
/* 271:    */   
/* 272:    */   private static void updateRaytraceResult(Sculpture sculpture, int x, int y, int z, int f, double len)
/* 273:    */   {
/* 274:223 */     if (!Sculpture.contains(x, y, z)) {
/* 275:223 */       return;
/* 276:    */     }
/* 277:224 */     if ((sculpture != null) && 
/* 278:225 */       (sculpture.getBlockAt(x, y, z, null) == Blocks.field_150350_a)) {
/* 279:225 */       return;
/* 280:    */     }
/* 281:227 */     if (len >= length) {
/* 282:227 */       return;
/* 283:    */     }
/* 284:229 */     length = len;
/* 285:230 */     xyzf[0] = x;xyzf[1] = y;xyzf[2] = z;xyzf[3] = f;
/* 286:    */   }
/* 287:    */   
/* 288:    */   public static void setBlockBoundsFromRaytrace(int[] pos, Block block, int type)
/* 289:    */   {
/* 290:241 */     pos = (int[])pos.clone();
/* 291:242 */     if (hasFlag(type, 1))
/* 292:    */     {
/* 293:243 */       ForgeDirection dir = ForgeDirection.getOrientation(pos[3]);
/* 294:244 */       pos[0] += dir.offsetX;
/* 295:245 */       pos[1] += dir.offsetY;
/* 296:246 */       pos[2] += dir.offsetZ;
/* 297:    */     }
/* 298:249 */     int x = 0;int y = 0;int z = 0;
/* 299:251 */     for (; pos[0] < 0; x--) {
/* 300:251 */       pos[0] += 8;
/* 301:    */     }
/* 302:252 */     for (; pos[0] > 7; x++) {
/* 303:252 */       pos[0] -= 8;
/* 304:    */     }
/* 305:253 */     for (; pos[1] < 0; y--) {
/* 306:253 */       pos[1] += 8;
/* 307:    */     }
/* 308:254 */     for (; pos[1] > 7; y++) {
/* 309:254 */       pos[1] -= 8;
/* 310:    */     }
/* 311:255 */     for (; pos[2] < 0; z--) {
/* 312:255 */       pos[2] += 8;
/* 313:    */     }
/* 314:256 */     for (; pos[2] > 7; z++) {
/* 315:256 */       pos[2] -= 8;
/* 316:    */     }
/* 317:258 */     boolean allx = (type & 0x2) > 0;
/* 318:259 */     boolean ally = (type & 0x4) > 0;
/* 319:260 */     boolean allz = (type & 0x8) > 0;
/* 320:261 */     block.func_149676_a(allx ? x + 0 : x + pos[0] / 8.0F, ally ? y + 0 : y + pos[1] / 8.0F, allz ? z + 0 : z + pos[2] / 8.0F, allx ? x + 1 : x + (pos[0] + 1) / 8.0F, ally ? y + 1 : y + (pos[1] + 1) / 8.0F, allz ? z + 1 : z + (pos[2] + 1) / 8.0F);
/* 321:    */   }
/* 322:    */   
/* 323:    */   public static boolean validOperation(World worldObj, int x, int y, int z, int[] pos, int chiselFlags)
/* 324:    */   {
/* 325:272 */     pos = (int[])pos.clone();
/* 326:273 */     if (hasFlag(chiselFlags, 1))
/* 327:    */     {
/* 328:274 */       ForgeDirection dir = ForgeDirection.getOrientation(pos[3]);
/* 329:275 */       pos[0] += dir.offsetX;
/* 330:276 */       pos[1] += dir.offsetY;
/* 331:277 */       pos[2] += dir.offsetZ;
/* 332:    */     }
/* 333:280 */     for (; pos[0] < 0; x--) {
/* 334:280 */       pos[0] += 8;
/* 335:    */     }
/* 336:281 */     for (; pos[0] > 7; x++) {
/* 337:281 */       pos[0] -= 8;
/* 338:    */     }
/* 339:282 */     for (; pos[1] < 0; y--) {
/* 340:282 */       pos[1] += 8;
/* 341:    */     }
/* 342:283 */     for (; pos[1] > 7; y++) {
/* 343:283 */       pos[1] -= 8;
/* 344:    */     }
/* 345:284 */     for (; pos[2] < 0; z--) {
/* 346:284 */       pos[2] += 8;
/* 347:    */     }
/* 348:285 */     for (; pos[2] > 7; z++) {
/* 349:285 */       pos[2] -= 8;
/* 350:    */     }
/* 351:287 */     Block b = worldObj.func_147439_a(x, y, z);
/* 352:288 */     if (hasFlag(chiselFlags, 1))
/* 353:    */     {
/* 354:289 */       if (b == Blocks.field_150350_a) {
/* 355:289 */         return true;
/* 356:    */       }
/* 357:290 */       if (b == ModMinePainter.sculpture.block) {
/* 358:290 */         return true;
/* 359:    */       }
/* 360:291 */       return false;
/* 361:    */     }
/* 362:293 */     int meta = worldObj.func_72805_g(x, y, z);
/* 363:294 */     if (b == Blocks.field_150350_a) {
/* 364:295 */       return false;
/* 365:    */     }
/* 366:297 */     if (b == ModMinePainter.sculpture.block) {
/* 367:297 */       return true;
/* 368:    */     }
/* 369:298 */     if (sculptable(b, meta)) {
/* 370:298 */       return true;
/* 371:    */     }
/* 372:299 */     return false;
/* 373:    */   }
/* 374:    */   
/* 375:    */   private static boolean hasFlag(int flags, int mask)
/* 376:    */   {
/* 377:304 */     return (flags & mask) > 0;
/* 378:    */   }
/* 379:    */   
/* 380:    */   public static boolean applyOperation(World w, int x, int y, int z, int[] pos, int flags, Block editBlock, int editMeta)
/* 381:    */   {
/* 382:310 */     pos = (int[])pos.clone();
/* 383:311 */     if (hasFlag(flags, 1))
/* 384:    */     {
/* 385:312 */       ForgeDirection dir = ForgeDirection.getOrientation(pos[3]);
/* 386:313 */       pos[0] += dir.offsetX;
/* 387:314 */       pos[1] += dir.offsetY;
/* 388:315 */       pos[2] += dir.offsetZ;
/* 389:    */     }
/* 390:318 */     for (; pos[0] < 0; x--) {
/* 391:318 */       pos[0] += 8;
/* 392:    */     }
/* 393:319 */     for (; pos[0] > 7; x++) {
/* 394:319 */       pos[0] -= 8;
/* 395:    */     }
/* 396:320 */     for (; pos[1] < 0; y--) {
/* 397:320 */       pos[1] += 8;
/* 398:    */     }
/* 399:321 */     for (; pos[1] > 7; y++) {
/* 400:321 */       pos[1] -= 8;
/* 401:    */     }
/* 402:322 */     for (; pos[2] < 0; z--) {
/* 403:322 */       pos[2] += 8;
/* 404:    */     }
/* 405:323 */     for (; pos[2] > 7; z++) {
/* 406:323 */       pos[2] -= 8;
/* 407:    */     }
/* 408:325 */     int[] minmax = new int[6];
/* 409:326 */     boolean allx = hasFlag(flags, 2);
/* 410:327 */     boolean ally = hasFlag(flags, 4);
/* 411:328 */     boolean allz = hasFlag(flags, 8);
/* 412:329 */     minmax[0] = (allx ? 0 : pos[0]);
/* 413:330 */     minmax[1] = (ally ? 0 : pos[1]);
/* 414:331 */     minmax[2] = (allz ? 0 : pos[2]);
/* 415:332 */     minmax[3] = (allx ? 8 : pos[0] + 1);
/* 416:333 */     minmax[4] = (ally ? 8 : pos[1] + 1);
/* 417:334 */     minmax[5] = (allz ? 8 : pos[2] + 1);
/* 418:    */     
/* 419:336 */     int blocks = editSubBlock(w, minmax, x, y, z, editBlock, (byte)editMeta);
/* 420:    */     
/* 421:338 */     return blocks > 0;
/* 422:    */   }
/* 423:    */   
/* 424:    */   public static int getLookingAxis(EntityPlayer ep)
/* 425:    */   {
/* 426:342 */     Vec3 vec = ep.func_70040_Z();
/* 427:343 */     double x = Math.abs(vec.field_72450_a);
/* 428:344 */     double y = Math.abs(vec.field_72448_b);
/* 429:345 */     double z = Math.abs(vec.field_72449_c);
/* 430:346 */     if ((x >= y) && (x >= z)) {
/* 431:346 */       return 0;
/* 432:    */     }
/* 433:347 */     if ((y >= x) && (y >= z)) {
/* 434:347 */       return 1;
/* 435:    */     }
/* 436:348 */     if ((z >= x) && (z >= y)) {
/* 437:348 */       return 2;
/* 438:    */     }
/* 439:349 */     return 0;
/* 440:    */   }
/* 441:    */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.sculpture.Operations
 * JD-Core Version:    0.7.0.1
 */