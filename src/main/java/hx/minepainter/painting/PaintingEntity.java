/*   1:    */ package hx.minepainter.painting;
/*   2:    */ 
/*   3:    */ import cpw.mods.fml.relauncher.Side;
/*   4:    */ import cpw.mods.fml.relauncher.SideOnly;
/*   5:    */ import java.awt.Color;
/*   6:    */ import java.awt.Graphics;
/*   7:    */ import java.awt.image.BufferedImage;
/*   8:    */ import java.io.ByteArrayInputStream;
/*   9:    */ import java.io.ByteArrayOutputStream;
/*  10:    */ import java.io.IOException;
/*  11:    */ import javax.imageio.ImageIO;
/*  12:    */ import net.minecraft.nbt.NBTTagCompound;
/*  13:    */ import net.minecraft.network.NetworkManager;
/*  14:    */ import net.minecraft.network.Packet;
/*  15:    */ import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
/*  16:    */ import net.minecraft.tileentity.TileEntity;
/*  17:    */ import net.minecraft.world.World;
/*  18:    */ 
/*  19:    */ public class PaintingEntity
/*  20:    */   extends TileEntity
/*  21:    */ {
/*  22:    */   BufferedImage image;
/*  23:    */   @SideOnly(Side.CLIENT)
/*  24:    */   private PaintingIcon icon;
/*  25:    */   
/*  26:    */   public PaintingEntity()
/*  27:    */   {
/*  28: 28 */     this.image = new BufferedImage(16, 16, 2);
/*  29: 31 */     for (int i = 0; i < 16; i++) {
/*  30: 32 */       this.image.setRGB(i, 0, getColorForDye(i) | 0xFF000000);
/*  31:    */     }
/*  32: 35 */     Graphics g = this.image.getGraphics();
/*  33: 36 */     g.setColor(Color.white);
/*  34: 37 */     g.fillRect(0, 1, 16, 15);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public int getColorForDye(int dye_index)
/*  38:    */   {
/*  39: 41 */     return net.minecraft.item.ItemDye.field_150922_c[dye_index];
/*  40:    */   }
/*  41:    */   
/*  42:    */   public BufferedImage getImg()
/*  43:    */   {
/*  44: 45 */     return this.image;
/*  45:    */   }
/*  46:    */   
/*  47:    */   @SideOnly(Side.CLIENT)
/*  48:    */   public PaintingIcon getIcon()
/*  49:    */   {
/*  50: 53 */     if (this.icon == null) {
/*  51: 53 */       this.icon = PaintingCache.get();
/*  52:    */     }
/*  53: 54 */     return this.icon;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void func_145843_s()
/*  57:    */   {
/*  58: 60 */     super.func_145843_s();
/*  59: 61 */     if (this.field_145850_b.field_72995_K)
/*  60:    */     {
/*  61: 62 */       getIcon().release();
/*  62: 63 */       this.icon = null;
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void onChunkUnload()
/*  67:    */   {
/*  68: 70 */     super.onChunkUnload();
/*  69: 71 */     if (this.field_145850_b.field_72995_K)
/*  70:    */     {
/*  71: 72 */       getIcon().release();
/*  72: 73 */       this.icon = null;
/*  73:    */     }
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void func_145841_b(NBTTagCompound nbt)
/*  77:    */   {
/*  78: 80 */     super.func_145841_b(nbt);
/*  79: 81 */     writeImageToNBT(nbt);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void writeImageToNBT(NBTTagCompound nbt)
/*  83:    */   {
/*  84: 85 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  85:    */     try
/*  86:    */     {
/*  87: 87 */       ImageIO.write(this.image, "png", baos);
/*  88:    */     }
/*  89:    */     catch (IOException e)
/*  90:    */     {
/*  91: 89 */       e.printStackTrace();
/*  92:    */     }
/*  93: 91 */     nbt.func_74773_a("image_data", baos.toByteArray());
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void func_145839_a(NBTTagCompound nbt)
/*  97:    */   {
/*  98: 97 */     readFromNBTToImage(nbt);
/*  99: 98 */     super.func_145839_a(nbt);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public void readFromNBTToImage(NBTTagCompound nbt)
/* 103:    */   {
/* 104:102 */     if (nbt == null) {
/* 105:102 */       return;
/* 106:    */     }
/* 107:103 */     byte[] data = nbt.func_74770_j("image_data");
/* 108:104 */     ByteArrayInputStream bais = new ByteArrayInputStream(data);
/* 109:    */     try
/* 110:    */     {
/* 111:106 */       BufferedImage img = ImageIO.read(bais);
/* 112:107 */       this.image = img;
/* 113:108 */       if ((this.field_145850_b != null) && (this.field_145850_b.field_72995_K)) {
/* 114:109 */         getIcon().fill(img);
/* 115:    */       }
/* 116:    */     }
/* 117:    */     catch (IOException e)
/* 118:    */     {
/* 119:111 */       getIcon().fill(this.image);
/* 120:    */     }
/* 121:    */   }
/* 122:    */   
/* 123:    */   public Packet func_145844_m()
/* 124:    */   {
/* 125:117 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 126:118 */     func_145841_b(nbttagcompound);
/* 127:119 */     return new S35PacketUpdateTileEntity(this.field_145851_c, this.field_145848_d, this.field_145849_e, 17, nbttagcompound);
/* 128:    */   }
/* 129:    */   
/* 130:    */   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
/* 131:    */   {
/* 132:124 */     func_145839_a(pkt.func_148857_g());
/* 133:    */   }
/* 134:    */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.painting.PaintingEntity
 * JD-Core Version:    0.7.0.1
 */