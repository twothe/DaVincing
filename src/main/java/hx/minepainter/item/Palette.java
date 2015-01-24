/*   1:    */ package hx.minepainter.item;
/*   2:    */ 
/*   3:    */ import cpw.mods.fml.relauncher.Side;
/*   4:    */ import cpw.mods.fml.relauncher.SideOnly;
/*   5:    */ import hx.minepainter.ModMinePainter;
/*   6:    */ import hx.minepainter.painting.PaintingEntity;
/*   7:    */ import hx.minepainter.painting.PaintingPlacement;
/*   8:    */ import hx.utils.BlockLoader;
/*   9:    */ import hx.utils.Utils;
/*  10:    */ import java.awt.image.BufferedImage;
/*  11:    */ import java.util.List;
/*  12:    */ import net.minecraft.client.renderer.texture.IIconRegister;
/*  13:    */ import net.minecraft.entity.player.EntityPlayer;
/*  14:    */ import net.minecraft.item.Item;
/*  15:    */ import net.minecraft.item.ItemStack;
/*  16:    */ import net.minecraft.nbt.NBTTagCompound;
/*  17:    */ import net.minecraft.util.IIcon;
/*  18:    */ import net.minecraft.world.World;
/*  19:    */ 
/*  20:    */ public class Palette
/*  21:    */   extends Item
/*  22:    */ {
/*  23: 24 */   private IIcon[] colors = new IIcon[6];
/*  24:    */   
/*  25:    */   public Palette()
/*  26:    */   {
/*  27: 28 */     func_77637_a(ModMinePainter.tabMinePainter);
/*  28: 29 */     func_77625_d(1);
/*  29: 30 */     func_111206_d("minepainter:palette");
/*  30: 31 */     func_77655_b("palette");
/*  31:    */   }
/*  32:    */   
/*  33:    */   public boolean func_77623_v()
/*  34:    */   {
/*  35: 37 */     return true;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public int getRenderPasses(int metadata)
/*  39:    */   {
/*  40: 43 */     return 7;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public IIcon getIcon(ItemStack is, int renderPass)
/*  44:    */   {
/*  45: 49 */     if (renderPass == 0) {
/*  46: 49 */       return this.field_77791_bV;
/*  47:    */     }
/*  48: 50 */     return this.colors[(renderPass - 1)];
/*  49:    */   }
/*  50:    */   
/*  51:    */   @SideOnly(Side.CLIENT)
/*  52:    */   public void func_94581_a(IIconRegister par1IconRegister)
/*  53:    */   {
/*  54: 57 */     super.func_94581_a(par1IconRegister);
/*  55: 58 */     for (int i = 0; i < 6; i++) {
/*  56: 58 */       this.colors[i] = par1IconRegister.func_94245_a(func_111208_A() + i);
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   public int func_82790_a(ItemStack par1ItemStack, int par2)
/*  61:    */   {
/*  62: 64 */     int[] colors = getColors(par1ItemStack);
/*  63: 66 */     if (par2 == 0) {
/*  64: 66 */       return super.func_82790_a(par1ItemStack, par2);
/*  65:    */     }
/*  66: 67 */     return colors[(par2 - 1)];
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static int[] getColors(ItemStack is)
/*  70:    */   {
/*  71: 72 */     NBTTagCompound nbt = is.func_77978_p();
/*  72: 73 */     if (nbt == null) {
/*  73: 73 */       is.func_77982_d(nbt = new NBTTagCompound());
/*  74:    */     }
/*  75: 75 */     NBTTagCompound palette = nbt.func_74775_l("palette");
/*  76: 76 */     int[] colors = palette.func_74759_k("colors");
/*  77: 77 */     if (colors.length == 0) {
/*  78: 77 */       colors = new int[] { -1, -1, -1, -1, -1, -1 };
/*  79:    */     }
/*  80: 79 */     palette.func_74783_a("colors", colors);
/*  81: 80 */     nbt.func_74782_a("palette", palette);
/*  82:    */     
/*  83: 82 */     return colors;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public boolean func_77648_a(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int par7, float _x, float _y, float _z)
/*  87:    */   {
/*  88: 88 */     if (w.func_147439_a(x, y, z) == ModMinePainter.painting.block)
/*  89:    */     {
/*  90: 90 */       int face = w.func_72805_g(x, y, z);
/*  91: 91 */       PaintingEntity pe = (PaintingEntity)Utils.getTE(w, x, y, z);
/*  92: 92 */       PaintingPlacement pp = PaintingPlacement.of(face);
/*  93: 93 */       float[] point = pp.block2painting(_x, _y, _z);
/*  94:    */       
/*  95: 95 */       int[] colors = getColors(is);
/*  96: 96 */       colors[0] = pe.getImg().getRGB((int)(point[0] * 16.0F), (int)(point[1] * 16.0F));
/*  97: 97 */       setColors(is, colors);
/*  98: 98 */       return true;
/*  99:    */     }
/* 100:101 */     return false;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public ItemStack func_77659_a(ItemStack is, World w, EntityPlayer ep)
/* 104:    */   {
/* 105:108 */     setColors(is, shift(getColors(is)));
/* 106:109 */     return is;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public static int[] shift(int[] colors)
/* 110:    */   {
/* 111:114 */     int t = colors[0];
/* 112:115 */     for (int i = 1; i < colors.length; i++) {
/* 113:116 */       colors[(i - 1)] = colors[i];
/* 114:    */     }
/* 115:117 */     colors[(colors.length - 1)] = t;
/* 116:    */     
/* 117:119 */     return colors;
/* 118:    */   }
/* 119:    */   
/* 120:    */   public static void setColors(ItemStack is, int[] colors)
/* 121:    */   {
/* 122:124 */     NBTTagCompound nbt = is.func_77978_p();
/* 123:125 */     if (nbt == null) {
/* 124:125 */       is.func_77982_d(nbt = new NBTTagCompound());
/* 125:    */     }
/* 126:127 */     NBTTagCompound palette = nbt.func_74775_l("palette");
/* 127:128 */     palette.func_74783_a("colors", colors);
/* 128:129 */     nbt.func_74782_a("palette", palette);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void func_77624_a(ItemStack is, EntityPlayer ep, List list, boolean help)
/* 132:    */   {
/* 133:135 */     int color = getColors(is)[0];
/* 134:136 */     list.add("Alpha : " + (color >> 24 & 0xFF));
/* 135:137 */     list.add("§cRed : " + (color >> 16 & 0xFF));
/* 136:138 */     list.add("§aGreen : " + (color >> 8 & 0xFF));
/* 137:139 */     list.add("§9Blue : " + (color >> 0 & 0xFF));
/* 138:    */   }
/* 139:    */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.item.Palette
 * JD-Core Version:    0.7.0.1
 */