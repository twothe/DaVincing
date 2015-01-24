/*   1:    */ package hx.minepainter.painting;
/*   2:    */ 
/*   3:    */ import hx.minepainter.ModMinePainter;
/*   4:    */ import hx.utils.ItemLoader;
/*   5:    */ import hx.utils.Utils;
/*   6:    */ import java.util.Random;
/*   7:    */ import net.minecraft.block.Block;
/*   8:    */ import net.minecraft.block.BlockContainer;
/*   9:    */ import net.minecraft.block.material.Material;
/*  10:    */ import net.minecraft.client.renderer.texture.IIconRegister;
/*  11:    */ import net.minecraft.item.Item;
/*  12:    */ import net.minecraft.item.ItemStack;
/*  13:    */ import net.minecraft.nbt.NBTTagCompound;
/*  14:    */ import net.minecraft.tileentity.TileEntity;
/*  15:    */ import net.minecraft.util.AxisAlignedBB;
/*  16:    */ import net.minecraft.util.MovingObjectPosition;
/*  17:    */ import net.minecraft.world.IBlockAccess;
/*  18:    */ import net.minecraft.world.World;
/*  19:    */ import net.minecraftforge.common.util.ForgeDirection;
/*  20:    */ 
/*  21:    */ public class PaintingBlock
/*  22:    */   extends BlockContainer
/*  23:    */ {
/*  24:    */   public boolean ignore_bounds_on_state;
/*  25:    */   
/*  26:    */   public PaintingBlock()
/*  27:    */   {
/*  28: 27 */     super(Material.field_151580_n);
/*  29: 28 */     func_149658_d("minepainter:palette");
/*  30: 29 */     func_149711_c(0.2F);
/*  31: 30 */     func_149663_c("painting");
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void func_149651_a(IIconRegister register) {}
/*  35:    */   
/*  36:    */   public TileEntity func_149915_a(World var1, int var2)
/*  37:    */   {
/*  38: 36 */     return new PaintingEntity();
/*  39:    */   }
/*  40:    */   
/*  41:    */   public boolean func_149662_c()
/*  42:    */   {
/*  43: 41 */     return false;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public boolean func_149686_d()
/*  47:    */   {
/*  48: 46 */     return false;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public AxisAlignedBB func_149668_a(World par1World, int par2, int par3, int par4)
/*  52:    */   {
/*  53: 50 */     return null;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void func_149719_a(IBlockAccess iba, int x, int y, int z)
/*  57:    */   {
/*  58: 54 */     if (this.ignore_bounds_on_state) {
/*  59: 54 */       return;
/*  60:    */     }
/*  61: 55 */     PaintingPlacement placement = PaintingPlacement.of(iba.func_72805_g(x, y, z));
/*  62: 56 */     placement.setBlockBounds(this);
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void func_149683_g()
/*  66:    */   {
/*  67: 60 */     func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*  68:    */   }
/*  69:    */   
/*  70:    */   public int func_149645_b()
/*  71:    */   {
/*  72: 64 */     return -1;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
/*  76:    */   {
/*  77: 68 */     ItemStack is = new ItemStack(ModMinePainter.canvas.item);
/*  78: 69 */     NBTTagCompound nbt = new NBTTagCompound();
/*  79: 70 */     PaintingEntity pe = (PaintingEntity)Utils.getTE(world, x, y, z);
/*  80:    */     
/*  81: 72 */     pe.writeImageToNBT(nbt);
/*  82: 73 */     is.func_77982_d(nbt);
/*  83: 74 */     return is;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void func_149695_a(World w, int x, int y, int z, Block block)
/*  87:    */   {
/*  88: 79 */     PaintingPlacement pp = PaintingPlacement.of(w.func_72805_g(x, y, z));
/*  89: 80 */     int tx = x - pp.normal.offsetX;
/*  90: 81 */     int ty = y - pp.normal.offsetY;
/*  91: 82 */     int tz = z - pp.normal.offsetZ;
/*  92: 83 */     if (w.func_147439_a(tx, ty, tz).func_149688_o().func_76220_a()) {
/*  93: 83 */       return;
/*  94:    */     }
/*  95: 85 */     w.func_147468_f(x, y, z);
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected ItemStack func_149644_j(int p_149644_1_)
/*  99:    */   {
/* 100: 88 */     return null;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public Item func_149650_a(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
/* 104:    */   {
/* 105: 92 */     return null;
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void func_149749_a(World w, int x, int y, int z, Block b, int meta)
/* 109:    */   {
/* 110: 97 */     ItemStack is = new ItemStack(ModMinePainter.canvas.item);
/* 111: 98 */     NBTTagCompound nbt = new NBTTagCompound();
/* 112: 99 */     PaintingEntity pe = (PaintingEntity)Utils.getTE(w, x, y, z);
/* 113:    */     
/* 114:101 */     pe.writeImageToNBT(nbt);
/* 115:102 */     is.func_77982_d(nbt);
/* 116:103 */     func_149642_a(w, x, y, z, is);
/* 117:    */     
/* 118:105 */     super.func_149749_a(w, x, y, z, b, meta);
/* 119:    */   }
/* 120:    */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.painting.PaintingBlock
 * JD-Core Version:    0.7.0.1
 */