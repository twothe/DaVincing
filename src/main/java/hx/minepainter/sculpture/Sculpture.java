/*   1:    */ package hx.minepainter.sculpture;
/*   2:    */ 
/*   3:    */ import hx.utils.Debug;
/*   4:    */ import net.minecraft.block.Block;
/*   5:    */ import net.minecraft.nbt.NBTTagCompound;
/*   6:    */ 
/*   7:    */ public class Sculpture
/*   8:    */ {
/*   9:    */   byte[][] layers;
/*  10:    */   int[] block_ids;
/*  11:    */   byte[] block_metas;
/*  12:    */   int[] usage_count;
/*  13: 24 */   Rotation r = new Rotation();
/*  14:    */   
/*  15:    */   public Sculpture()
/*  16:    */   {
/*  17: 27 */     normalize();
/*  18:    */   }
/*  19:    */   
/*  20:    */   public Rotation getRotation()
/*  21:    */   {
/*  22: 31 */     return this.r;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void write(NBTTagCompound nbt)
/*  26:    */   {
/*  27: 35 */     nbt.func_74783_a("block_ids", this.block_ids);
/*  28: 36 */     nbt.func_74773_a("block_metas", this.block_metas);
/*  29: 37 */     for (int i = 0; i < this.layers.length; i++) {
/*  30: 38 */       nbt.func_74773_a("layer" + i, this.layers[i]);
/*  31:    */     }
/*  32: 39 */     nbt.func_74773_a("rotation", this.r.r);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void read(NBTTagCompound nbt)
/*  36:    */   {
/*  37: 43 */     this.block_ids = nbt.func_74759_k("block_ids");
/*  38: 44 */     this.block_metas = nbt.func_74770_j("block_metas");
/*  39: 45 */     this.r.r = nbt.func_74770_j("rotation");
/*  40: 46 */     this.layers = new byte[log(this.block_ids.length)][];
/*  41: 47 */     for (int i = 0; i < this.layers.length; i++) {
/*  42: 48 */       this.layers[i] = nbt.func_74770_j("layer" + i);
/*  43:    */     }
/*  44: 50 */     normalize();
/*  45:    */   }
/*  46:    */   
/*  47:    */   public Block getBlockAt(int x, int y, int z, BlockSlice slice)
/*  48:    */   {
/*  49: 54 */     if (!contains(x, y, z)) {
/*  50: 55 */       return slice.func_147439_a(x, y, z);
/*  51:    */     }
/*  52: 57 */     int index = getIndex(x, y, z);
/*  53:    */     
/*  54: 59 */     return Block.func_149729_e(this.block_ids[index]);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public int getMetaAt(int x, int y, int z, BlockSlice slice)
/*  58:    */   {
/*  59: 63 */     if (!contains(x, y, z)) {
/*  60: 64 */       return slice.func_72805_g(x, y, z);
/*  61:    */     }
/*  62: 66 */     int index = getIndex(x, y, z);
/*  63:    */     
/*  64: 68 */     return this.block_metas[index];
/*  65:    */   }
/*  66:    */   
/*  67:    */   public boolean setBlockAt(int x, int y, int z, Block block, byte meta)
/*  68:    */   {
/*  69: 72 */     if (!contains(x, y, z)) {
/*  70: 72 */       return false;
/*  71:    */     }
/*  72: 74 */     int index = findIndexForBlock(Block.func_149682_b(block), meta);
/*  73: 75 */     if (index < 0)
/*  74:    */     {
/*  75: 76 */       grow();
/*  76: 77 */       index = this.block_ids.length / 2;
/*  77: 78 */       this.block_ids[index] = Block.func_149682_b(block);
/*  78: 79 */       this.block_metas[index] = meta;
/*  79:    */     }
/*  80: 82 */     setIndex(x, y, z, index);
/*  81: 83 */     return true;
/*  82:    */   }
/*  83:    */   
/*  84:    */   public boolean isEmpty()
/*  85:    */   {
/*  86: 87 */     int s = 0;
/*  87: 88 */     for (int i = 0; i < this.block_ids.length; i++) {
/*  88: 89 */       if (this.block_ids[i] == 0) {
/*  89: 89 */         s += this.usage_count[i];
/*  90:    */       }
/*  91:    */     }
/*  92: 90 */     return s == 512;
/*  93:    */   }
/*  94:    */   
/*  95:    */   public boolean isFull()
/*  96:    */   {
/*  97: 94 */     int s = 0;
/*  98: 95 */     for (int i = 0; i < this.block_ids.length; i++) {
/*  99: 96 */       if (this.block_ids[i] == 0) {
/* 100: 96 */         s += this.usage_count[i];
/* 101:    */       }
/* 102:    */     }
/* 103: 97 */     return s == 0;
/* 104:    */   }
/* 105:    */   
/* 106:    */   private int findIndexForBlock(int blockID, byte meta)
/* 107:    */   {
/* 108:101 */     for (int i = 0; i < this.block_ids.length; i++)
/* 109:    */     {
/* 110:102 */       if ((this.block_ids[i] == blockID) && (this.block_metas[i] == meta)) {
/* 111:103 */         return i;
/* 112:    */       }
/* 113:105 */       if (this.usage_count[i] == 0)
/* 114:    */       {
/* 115:106 */         this.block_ids[i] = blockID;
/* 116:107 */         this.block_metas[i] = meta;
/* 117:108 */         return i;
/* 118:    */       }
/* 119:    */     }
/* 120:111 */     return -1;
/* 121:    */   }
/* 122:    */   
/* 123:    */   int getIndex(int x, int y, int z)
/* 124:    */   {
/* 125:115 */     this.r.apply(x, y, z);
/* 126:116 */     int index = 0;
/* 127:117 */     for (int l = 0; l < this.layers.length; l++) {
/* 128:118 */       if ((this.layers[l][(this.r.x * 8 + this.r.y)] & 1 << this.r.z) > 0) {
/* 129:118 */         index |= 1 << l;
/* 130:    */       }
/* 131:    */     }
/* 132:119 */     return index;
/* 133:    */   }
/* 134:    */   
/* 135:    */   void setIndex(int x, int y, int z, int index)
/* 136:    */   {
/* 137:124 */     int prev = getIndex(x, y, z);
/* 138:125 */     this.usage_count[prev] -= 1;
/* 139:126 */     this.usage_count[index] += 1;
/* 140:    */     
/* 141:128 */     this.r.apply(x, y, z);
/* 142:129 */     for (int l = 0; l < this.layers.length; l++) {
/* 143:130 */       if ((index & 1 << l) > 0)
/* 144:    */       {
/* 145:131 */         int tmp89_88 = (this.r.x * 8 + this.r.y); byte[] tmp89_70 = this.layers[l];tmp89_70[tmp89_88] = ((byte)(tmp89_70[tmp89_88] | 1 << this.r.z));
/* 146:    */       }
/* 147:    */       else
/* 148:    */       {
/* 149:132 */         int tmp131_130 = (this.r.x * 8 + this.r.y); byte[] tmp131_112 = this.layers[l];tmp131_112[tmp131_130] = ((byte)(tmp131_112[tmp131_130] & (1 << this.r.z ^ 0xFFFFFFFF)));
/* 150:    */       }
/* 151:    */     }
/* 152:    */   }
/* 153:    */   
/* 154:    */   public static boolean contains(int x, int y, int z)
/* 155:    */   {
/* 156:136 */     return (x >= 0) && (y >= 0) && (z >= 0) && (x < 8) && (y < 8) && (z < 8);
/* 157:    */   }
/* 158:    */   
/* 159:    */   private boolean check()
/* 160:    */   {
/* 161:140 */     if (this.block_ids == null) {
/* 162:140 */       return false;
/* 163:    */     }
/* 164:141 */     if (this.block_metas == null) {
/* 165:141 */       return false;
/* 166:    */     }
/* 167:142 */     if (this.layers == null) {
/* 168:142 */       return false;
/* 169:    */     }
/* 170:143 */     if (this.r.r == null) {
/* 171:143 */       return false;
/* 172:    */     }
/* 173:144 */     for (int i = 0; i < this.layers.length; i++)
/* 174:    */     {
/* 175:145 */       if (this.layers[i] == null)
/* 176:    */       {
/* 177:146 */         Debug.log(new String[] { "layer " + i + " is null!" });
/* 178:147 */         return false;
/* 179:    */       }
/* 180:149 */       if (this.layers[i].length != 64)
/* 181:    */       {
/* 182:150 */         Debug.log(new String[] { "layer " + i + " is " + this.layers[i].length + " long!" });
/* 183:151 */         return false;
/* 184:    */       }
/* 185:    */     }
/* 186:154 */     if (this.block_ids.length != 1 << this.layers.length) {
/* 187:154 */       return false;
/* 188:    */     }
/* 189:155 */     if (this.block_ids.length != this.block_metas.length) {
/* 190:155 */       return false;
/* 191:    */     }
/* 192:157 */     if (this.usage_count.length != this.block_ids.length) {
/* 193:157 */       this.usage_count = new int[this.block_ids.length];
/* 194:    */     }
/* 195:158 */     return true;
/* 196:    */   }
/* 197:    */   
/* 198:    */   private void grow()
/* 199:    */   {
/* 200:162 */     byte[][] nlayers = new byte[this.layers.length + 1][];
/* 201:163 */     for (int i = 0; i < this.layers.length; i++) {
/* 202:164 */       nlayers[i] = this.layers[i];
/* 203:    */     }
/* 204:165 */     nlayers[this.layers.length] = new byte[64];
/* 205:166 */     this.layers = nlayers;
/* 206:    */     
/* 207:168 */     int[] ids = new int[this.block_ids.length * 2];
/* 208:169 */     for (int i = 0; i < this.block_ids.length; i++) {
/* 209:169 */       ids[i] = this.block_ids[i];
/* 210:    */     }
/* 211:170 */     this.block_ids = ids;
/* 212:    */     
/* 213:172 */     byte[] metas = new byte[this.block_metas.length * 2];
/* 214:173 */     for (int i = 0; i < this.block_metas.length; i++) {
/* 215:173 */       metas[i] = this.block_metas[i];
/* 216:    */     }
/* 217:174 */     this.block_metas = metas;
/* 218:    */     
/* 219:176 */     int[] usage = new int[this.usage_count.length * 2];
/* 220:177 */     for (int i = 0; i < this.usage_count.length; i++) {
/* 221:177 */       usage[i] = this.usage_count[i];
/* 222:    */     }
/* 223:178 */     this.usage_count = usage;
/* 224:    */   }
/* 225:    */   
/* 226:    */   private void normalize()
/* 227:    */   {
/* 228:182 */     if (!check())
/* 229:    */     {
/* 230:183 */       this.layers = new byte[1][64];
/* 231:184 */       this.block_ids = new int[2];
/* 232:185 */       this.block_metas = new byte[2];
/* 233:186 */       this.usage_count = new int[2];
/* 234:    */     }
/* 235:188 */     for (int i = 0; i < this.usage_count.length; i++) {
/* 236:188 */       this.usage_count[i] = 0;
/* 237:    */     }
/* 238:189 */     for (int i = 0; i < 512; i++)
/* 239:    */     {
/* 240:190 */       int index = getIndex(i >> 6, i >> 3 & 0x7, i & 0x7);
/* 241:191 */       this.usage_count[index] += 1;
/* 242:    */     }
/* 243:    */   }
/* 244:    */   
/* 245:    */   private int log(int num)
/* 246:    */   {
/* 247:196 */     int i = 0;
/* 248:197 */     while (num > 1)
/* 249:    */     {
/* 250:198 */       num >>= 1;
/* 251:199 */       i++;
/* 252:    */     }
/* 253:201 */     return i;
/* 254:    */   }
/* 255:    */   
/* 256:    */   public int getLight()
/* 257:    */   {
/* 258:206 */     int light = 0;
/* 259:207 */     int current = 0;
/* 260:208 */     for (int i = 0; i < this.usage_count.length; i++)
/* 261:    */     {
/* 262:209 */       current = Block.func_149729_e(this.block_ids[i]).func_149750_m();
/* 263:210 */       if (current > light) {
/* 264:210 */         light = current;
/* 265:    */       }
/* 266:    */     }
/* 267:212 */     return light;
/* 268:    */   }
/* 269:    */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.sculpture.Sculpture
 * JD-Core Version:    0.7.0.1
 */