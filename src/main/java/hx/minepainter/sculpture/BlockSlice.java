/*   1:    */ package hx.minepainter.sculpture;
/*   2:    */ 
/*   3:    */ import cpw.mods.fml.relauncher.Side;
/*   4:    */ import cpw.mods.fml.relauncher.SideOnly;
/*   5:    */ import net.minecraft.block.Block;
/*   6:    */ import net.minecraft.init.Blocks;
/*   7:    */ import net.minecraft.tileentity.TileEntity;
/*   8:    */ import net.minecraft.util.Vec3Pool;
/*   9:    */ import net.minecraft.world.IBlockAccess;
/*  10:    */ import net.minecraft.world.biome.BiomeGenBase;
/*  11:    */ import net.minecraftforge.common.util.ForgeDirection;
/*  12:    */ 
/*  13:    */ @SideOnly(Side.CLIENT)
/*  14:    */ public class BlockSlice
/*  15:    */   implements IBlockAccess
/*  16:    */ {
/*  17:    */   IBlockAccess iba;
/*  18:    */   int x;
/*  19:    */   int y;
/*  20:    */   int z;
/*  21:    */   Sculpture sculpture;
/*  22:    */   int brightness;
/*  23: 23 */   private static BlockSlice instance = new BlockSlice();
/*  24:    */   
/*  25:    */   public static BlockSlice at(IBlockAccess iba, int x, int y, int z)
/*  26:    */   {
/*  27: 27 */     instance.iba = iba;
/*  28: 28 */     instance.x = x;
/*  29: 29 */     instance.y = y;
/*  30: 30 */     instance.z = z;
/*  31:    */     
/*  32: 32 */     TileEntity te = iba.func_147438_o(x, y, z);
/*  33: 33 */     if ((te != null) && ((te instanceof SculptureEntity))) {
/*  34: 34 */       instance.sculpture = ((SculptureEntity)te).sculpture;
/*  35:    */     } else {
/*  36: 36 */       instance.sculpture = null;
/*  37:    */     }
/*  38: 38 */     return instance;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public static BlockSlice of(Sculpture sculpture, int brightness)
/*  42:    */   {
/*  43: 42 */     instance.iba = null;
/*  44: 43 */     instance.sculpture = sculpture;
/*  45: 44 */     instance.brightness = brightness;
/*  46: 45 */     return instance;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public static void clear()
/*  50:    */   {
/*  51: 49 */     instance.iba = null;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public Block func_147439_a(int x, int y, int z)
/*  55:    */   {
/*  56: 54 */     if ((this.sculpture != null) && (Sculpture.contains(x, y, z))) {
/*  57: 55 */       return this.sculpture.getBlockAt(x, y, z, this);
/*  58:    */     }
/*  59: 56 */     if (this.iba == null) {
/*  60: 56 */       return Blocks.field_150350_a;
/*  61:    */     }
/*  62: 57 */     return this.iba.func_147439_a(this.x + cap(x), this.y + cap(y), this.z + cap(z));
/*  63:    */   }
/*  64:    */   
/*  65:    */   public TileEntity func_147438_o(int x, int y, int z)
/*  66:    */   {
/*  67: 62 */     if (this.iba == null) {
/*  68: 62 */       return null;
/*  69:    */     }
/*  70: 63 */     return this.iba.func_147438_o(this.x + cap(x), this.y + cap(y), this.z + cap(z));
/*  71:    */   }
/*  72:    */   
/*  73:    */   @SideOnly(Side.CLIENT)
/*  74:    */   public int func_72802_i(int x, int y, int z, int var4)
/*  75:    */   {
/*  76: 69 */     if (this.iba == null) {
/*  77: 69 */       return this.brightness;
/*  78:    */     }
/*  79: 70 */     return this.iba.func_72802_i(this.x + cap(x), this.y + cap(y), this.z + cap(z), var4);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public int func_72805_g(int x, int y, int z)
/*  83:    */   {
/*  84: 75 */     if ((this.sculpture != null) && (Sculpture.contains(x, y, z))) {
/*  85: 76 */       return this.sculpture.getMetaAt(x, y, z, this);
/*  86:    */     }
/*  87: 77 */     if (this.iba == null) {
/*  88: 77 */       return 0;
/*  89:    */     }
/*  90: 78 */     return this.iba.func_72805_g(this.x + cap(x), this.y + cap(y), this.z + cap(z));
/*  91:    */   }
/*  92:    */   
/*  93:    */   public boolean func_147437_c(int x, int y, int z)
/*  94:    */   {
/*  95: 83 */     if ((this.sculpture != null) && (Sculpture.contains(x, y, z))) {
/*  96: 84 */       return this.sculpture.getBlockAt(x, y, z, this) == Blocks.field_150350_a;
/*  97:    */     }
/*  98: 85 */     if (this.iba == null) {
/*  99: 85 */       return true;
/* 100:    */     }
/* 101: 86 */     return this.iba.func_147437_c(this.x + cap(x), this.y + cap(y), this.z + cap(z));
/* 102:    */   }
/* 103:    */   
/* 104:    */   @SideOnly(Side.CLIENT)
/* 105:    */   public BiomeGenBase func_72807_a(int var1, int var2)
/* 106:    */   {
/* 107: 92 */     return null;
/* 108:    */   }
/* 109:    */   
/* 110:    */   @SideOnly(Side.CLIENT)
/* 111:    */   public int func_72800_K()
/* 112:    */   {
/* 113: 98 */     if (this.iba == null) {
/* 114: 98 */       return 256;
/* 115:    */     }
/* 116: 99 */     return this.iba.func_72800_K();
/* 117:    */   }
/* 118:    */   
/* 119:    */   @SideOnly(Side.CLIENT)
/* 120:    */   public boolean func_72806_N()
/* 121:    */   {
/* 122:105 */     if (this.iba == null) {
/* 123:105 */       return false;
/* 124:    */     }
/* 125:106 */     return this.iba.func_72806_N();
/* 126:    */   }
/* 127:    */   
/* 128:    */   public Vec3Pool func_82732_R()
/* 129:    */   {
/* 130:111 */     if (this.iba == null) {
/* 131:111 */       return null;
/* 132:    */     }
/* 133:112 */     return this.iba.func_82732_R();
/* 134:    */   }
/* 135:    */   
/* 136:    */   public int func_72879_k(int x, int y, int z, int var4)
/* 137:    */   {
/* 138:117 */     if (this.iba == null) {
/* 139:117 */       return 0;
/* 140:    */     }
/* 141:118 */     return this.iba.func_72879_k(this.x + cap(x), this.y + cap(y), this.z + cap(z), var4);
/* 142:    */   }
/* 143:    */   
/* 144:    */   public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default)
/* 145:    */   {
/* 146:124 */     if (this.iba == null) {
/* 147:124 */       return false;
/* 148:    */     }
/* 149:125 */     return this.iba.isSideSolid(this.x + cap(x), this.y + cap(y), this.z + cap(z), side, _default);
/* 150:    */   }
/* 151:    */   
/* 152:    */   private static int cap(int original)
/* 153:    */   {
/* 154:128 */     return original >= 0 ? 0 : original > 7 ? original - 7 : original;
/* 155:    */   }
/* 156:    */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.sculpture.BlockSlice
 * JD-Core Version:    0.7.0.1
 */