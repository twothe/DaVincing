/*   1:    */ package hx.minepainter.sculpture;
/*   2:    */ 
/*   3:    */ import cpw.mods.fml.relauncher.Side;
/*   4:    */ import cpw.mods.fml.relauncher.SideOnly;
/*   5:    */ import hx.minepainter.ModMinePainter;
/*   6:    */ import hx.utils.BlockLoader;
/*   7:    */ import hx.utils.ItemLoader;
/*   8:    */ import hx.utils.Utils;
/*   9:    */ import java.util.List;
/*  10:    */ import java.util.Random;
/*  11:    */ import net.minecraft.block.Block;
/*  12:    */ import net.minecraft.block.BlockContainer;
/*  13:    */ import net.minecraft.block.material.Material;
/*  14:    */ import net.minecraft.client.renderer.texture.IIconRegister;
/*  15:    */ import net.minecraft.entity.Entity;
/*  16:    */ import net.minecraft.init.Blocks;
/*  17:    */ import net.minecraft.item.Item;
/*  18:    */ import net.minecraft.item.ItemStack;
/*  19:    */ import net.minecraft.nbt.NBTTagCompound;
/*  20:    */ import net.minecraft.tileentity.TileEntity;
/*  21:    */ import net.minecraft.util.AxisAlignedBB;
/*  22:    */ import net.minecraft.util.IIcon;
/*  23:    */ import net.minecraft.util.MovingObjectPosition;
/*  24:    */ import net.minecraft.util.Vec3;
/*  25:    */ import net.minecraft.world.IBlockAccess;
/*  26:    */ import net.minecraft.world.World;
/*  27:    */ import net.minecraftforge.common.util.ForgeDirection;
/*  28:    */ 
/*  29:    */ public class SculptureBlock
/*  30:    */   extends BlockContainer
/*  31:    */ {
/*  32:    */   private int x;
/*  33:    */   private int y;
/*  34:    */   private int z;
/*  35: 32 */   private int meta = 0;
/*  36: 33 */   private Block current = Blocks.field_150348_b;
/*  37: 34 */   private int renderID = -1;
/*  38:    */   
/*  39:    */   public void setCurrentBlock(Block that, int meta)
/*  40:    */   {
/*  41: 36 */     if (that == null)
/*  42:    */     {
/*  43: 37 */       meta = 0;
/*  44: 38 */       this.renderID = -1;
/*  45: 39 */       this.current = Blocks.field_150348_b;
/*  46: 40 */       return;
/*  47:    */     }
/*  48: 42 */     this.current = that;
/*  49: 43 */     this.meta = meta;
/*  50: 44 */     this.renderID = that.func_149645_b();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setSubCoordinate(int x, int y, int z)
/*  54:    */   {
/*  55: 47 */     this.x = x;this.y = y;this.z = z;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void dropScrap(World w, int x, int y, int z, ItemStack is)
/*  59:    */   {
/*  60: 51 */     func_149642_a(w, x, y, z, is);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public SculptureBlock()
/*  64:    */   {
/*  65: 56 */     super(Material.field_151576_e);
/*  66: 57 */     func_149711_c(1.0F);
/*  67:    */   }
/*  68:    */   
/*  69:    */   public MovingObjectPosition func_149731_a(World w, int x, int y, int z, Vec3 st, Vec3 ed)
/*  70:    */   {
/*  71: 63 */     SculptureEntity tile = (SculptureEntity)Utils.getTE(w, x, y, z);
/*  72: 64 */     Sculpture sculpture = tile.sculpture();
/*  73:    */     
/*  74: 66 */     int[] pos = Operations.raytrace(sculpture, st.func_72441_c(-x, -y, -z), ed.func_72441_c(-x, -y, -z));
/*  75: 67 */     if (pos[0] == -1) {
/*  76: 67 */       return null;
/*  77:    */     }
/*  78: 69 */     ForgeDirection dir = ForgeDirection.getOrientation(pos[3]);
/*  79: 70 */     Vec3 hit = null;
/*  80: 71 */     if (dir.offsetX != 0) {
/*  81: 71 */       hit = st.func_72429_b(ed, x + pos[0] / 8.0F + (dir.offsetX + 1) / 16.0F);
/*  82: 72 */     } else if (dir.offsetY != 0) {
/*  83: 72 */       hit = st.func_72435_c(ed, y + pos[1] / 8.0F + (dir.offsetY + 1) / 16.0F);
/*  84: 73 */     } else if (dir.offsetZ != 0) {
/*  85: 73 */       hit = st.func_72434_d(ed, z + pos[2] / 8.0F + (dir.offsetZ + 1) / 16.0F);
/*  86:    */     }
/*  87: 74 */     if (hit == null)
/*  88:    */     {
/*  89: 75 */       if (sculpture.isEmpty()) {
/*  90: 75 */         return super.func_149731_a(w, x, y, z, st, ed);
/*  91:    */       }
/*  92: 76 */       return null;
/*  93:    */     }
/*  94: 79 */     return new MovingObjectPosition(x, y, z, pos[3], hit);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public void func_149743_a(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
/*  98:    */   {
/*  99: 85 */     SculptureEntity tile = (SculptureEntity)Utils.getTE(par1World, par2, par3, par4);
/* 100: 86 */     Sculpture sculpture = tile.sculpture();
/* 101: 88 */     for (int x = 0; x < 8; x++) {
/* 102: 89 */       for (int y = 0; y < 8; y++) {
/* 103: 90 */         for (int z = 0; z < 8; z++) {
/* 104: 91 */           if (sculpture.getBlockAt(x, y, z, null) != Blocks.field_150350_a)
/* 105:    */           {
/* 106: 92 */             func_149676_a(x / 8.0F, y / 8.0F, z / 8.0F, (x + 1) / 8.0F, (y + 1) / 8.0F, (z + 1) / 8.0F);
/* 107: 93 */             super.func_149743_a(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
/* 108:    */           }
/* 109:    */         }
/* 110:    */       }
/* 111:    */     }
/* 112: 95 */     func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/* 113:    */   }
/* 114:    */   
/* 115:    */   public boolean func_149646_a(IBlockAccess iba, int x, int y, int z, int side)
/* 116:    */   {
/* 117:102 */     if (iba.func_147439_a(x, y, z) == this.current) {
/* 118:102 */       return false;
/* 119:    */     }
/* 120:103 */     return (iba.func_147437_c(x, y, z)) || (!iba.func_147439_a(x, y, z).func_149662_c());
/* 121:    */   }
/* 122:    */   
/* 123:    */   public TileEntity func_149915_a(World var1, int var2)
/* 124:    */   {
/* 125:108 */     return new SculptureEntity();
/* 126:    */   }
/* 127:    */   
/* 128:    */   @SideOnly(Side.CLIENT)
/* 129:    */   public void func_149651_a(IIconRegister p_149651_1_) {}
/* 130:    */   
/* 131:    */   @SideOnly(Side.CLIENT)
/* 132:    */   public IIcon func_149691_a(int side, int meta)
/* 133:    */   {
/* 134:114 */     return this.current.func_149691_a(side, this.meta);
/* 135:    */   }
/* 136:    */   
/* 137:    */   @SideOnly(Side.CLIENT)
/* 138:    */   public int func_149645_b()
/* 139:    */   {
/* 140:119 */     if (this.renderID == -1) {
/* 141:120 */       return ModMinePainter.sculpture.renderID;
/* 142:    */     }
/* 143:121 */     return this.renderID;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public boolean func_149662_c()
/* 147:    */   {
/* 148:126 */     return false;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public boolean func_149686_d()
/* 152:    */   {
/* 153:131 */     return false;
/* 154:    */   }
/* 155:    */   
/* 156:    */   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
/* 157:    */   {
/* 158:135 */     SculptureEntity se = (SculptureEntity)Utils.getTE(world, x, y, z);
/* 159:136 */     NBTTagCompound nbt = new NBTTagCompound();
/* 160:137 */     ItemStack is = new ItemStack(ModMinePainter.droppedSculpture.item);
/* 161:    */     
/* 162:139 */     se.sculpture.write(nbt);
/* 163:140 */     is.func_77982_d(nbt);
/* 164:141 */     return is;
/* 165:    */   }
/* 166:    */   
/* 167:    */   public int getLightValue(IBlockAccess world, int x, int y, int z)
/* 168:    */   {
/* 169:145 */     TileEntity te = world.func_147438_o(x, y, z);
/* 170:146 */     if ((te == null) || (!(te instanceof SculptureEntity))) {
/* 171:146 */       return super.getLightValue(world, x, y, z);
/* 172:    */     }
/* 173:147 */     SculptureEntity se = (SculptureEntity)te;
/* 174:148 */     return se.sculpture.getLight();
/* 175:    */   }
/* 176:    */   
/* 177:    */   protected ItemStack func_149644_j(int p_149644_1_)
/* 178:    */   {
/* 179:151 */     return null;
/* 180:    */   }
/* 181:    */   
/* 182:    */   public Item func_149650_a(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
/* 183:    */   {
/* 184:155 */     return null;
/* 185:    */   }
/* 186:    */   
/* 187:    */   public void func_149749_a(World w, int x, int y, int z, Block b, int meta)
/* 188:    */   {
/* 189:159 */     SculptureEntity se = (SculptureEntity)Utils.getTE(w, x, y, z);
/* 190:160 */     if ((se == null) || (se.sculpture().isEmpty()))
/* 191:    */     {
/* 192:161 */       super.func_149749_a(w, x, y, z, b, meta);
/* 193:162 */       return;
/* 194:    */     }
/* 195:164 */     NBTTagCompound nbt = new NBTTagCompound();
/* 196:165 */     ItemStack is = new ItemStack(ModMinePainter.droppedSculpture.item);
/* 197:    */     
/* 198:167 */     se.sculpture.write(nbt);
/* 199:168 */     is.func_77982_d(nbt);
/* 200:169 */     func_149642_a(w, x, y, z, is);
/* 201:170 */     super.func_149749_a(w, x, y, z, b, meta);
/* 202:    */   }
/* 203:    */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.sculpture.SculptureBlock
 * JD-Core Version:    0.7.0.1
 */