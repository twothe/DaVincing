/*  1:   */ package hx.minepainter.sculpture;
/*  2:   */ 
/*  3:   */ import cpw.mods.fml.relauncher.Side;
/*  4:   */ import cpw.mods.fml.relauncher.SideOnly;
/*  5:   */ import net.minecraft.nbt.NBTTagCompound;
/*  6:   */ import net.minecraft.network.NetworkManager;
/*  7:   */ import net.minecraft.network.Packet;
/*  8:   */ import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
/*  9:   */ import net.minecraft.tileentity.TileEntity;
/* 10:   */ import net.minecraft.world.World;
/* 11:   */ 
/* 12:   */ public class SculptureEntity
/* 13:   */   extends TileEntity
/* 14:   */ {
/* 15:15 */   Sculpture sculpture = new Sculpture();
/* 16:   */   @SideOnly(Side.CLIENT)
/* 17:   */   private SculptureRenderCompiler render;
/* 18:   */   
/* 19:   */   public Sculpture sculpture()
/* 20:   */   {
/* 21:21 */     return this.sculpture;
/* 22:   */   }
/* 23:   */   
/* 24:   */   @SideOnly(Side.CLIENT)
/* 25:   */   public SculptureRenderCompiler getRender()
/* 26:   */   {
/* 27:26 */     if (this.render == null) {
/* 28:26 */       this.render = new SculptureRenderCompiler();
/* 29:   */     }
/* 30:27 */     return this.render;
/* 31:   */   }
/* 32:   */   
/* 33:   */   @SideOnly(Side.CLIENT)
/* 34:   */   public void updateRender()
/* 35:   */   {
/* 36:32 */     if (this.field_145850_b.field_72995_K)
/* 37:   */     {
/* 38:33 */       BlockSlice slice = BlockSlice.at(this.field_145850_b, this.field_145851_c, this.field_145848_d, this.field_145849_e);
/* 39:34 */       getRender().update(slice);
/* 40:35 */       BlockSlice.clear();
/* 41:   */     }
/* 42:   */   }
/* 43:   */   
/* 44:   */   @SideOnly(Side.CLIENT)
/* 45:   */   public void func_145843_s()
/* 46:   */   {
/* 47:42 */     super.func_145843_s();
/* 48:43 */     if (this.field_145850_b.field_72995_K) {
/* 49:44 */       getRender().clear();
/* 50:   */     }
/* 51:   */   }
/* 52:   */   
/* 53:   */   @SideOnly(Side.CLIENT)
/* 54:   */   public void onChunkUnload()
/* 55:   */   {
/* 56:50 */     super.onChunkUnload();
/* 57:51 */     if (this.field_145850_b.field_72995_K) {
/* 58:52 */       getRender().clear();
/* 59:   */     }
/* 60:   */   }
/* 61:   */   
/* 62:   */   public void func_145841_b(NBTTagCompound nbt)
/* 63:   */   {
/* 64:60 */     super.func_145841_b(nbt);
/* 65:61 */     this.sculpture.write(nbt);
/* 66:   */   }
/* 67:   */   
/* 68:   */   public void func_145839_a(NBTTagCompound nbt)
/* 69:   */   {
/* 70:67 */     if ((this.field_145850_b != null) && (this.field_145850_b.field_72995_K)) {
/* 71:68 */       getRender().changed = true;
/* 72:   */     }
/* 73:69 */     super.func_145839_a(nbt);
/* 74:70 */     this.sculpture.read(nbt);
/* 75:   */   }
/* 76:   */   
/* 77:   */   public Packet func_145844_m()
/* 78:   */   {
/* 79:75 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 80:76 */     func_145841_b(nbttagcompound);
/* 81:77 */     return new S35PacketUpdateTileEntity(this.field_145851_c, this.field_145848_d, this.field_145849_e, 17, nbttagcompound);
/* 82:   */   }
/* 83:   */   
/* 84:   */   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
/* 85:   */   {
/* 86:82 */     func_145839_a(pkt.func_148857_g());
/* 87:   */   }
/* 88:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.sculpture.SculptureEntity
 * JD-Core Version:    0.7.0.1
 */