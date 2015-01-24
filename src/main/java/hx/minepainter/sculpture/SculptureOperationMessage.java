/*  1:   */ package hx.minepainter.sculpture;
/*  2:   */ 
/*  3:   */ import cpw.mods.fml.common.network.simpleimpl.IMessage;
/*  4:   */ import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
/*  5:   */ import cpw.mods.fml.common.network.simpleimpl.MessageContext;
/*  6:   */ import io.netty.buffer.ByteBuf;
/*  7:   */ import net.minecraft.block.Block;
/*  8:   */ import net.minecraft.entity.player.EntityPlayer;
/*  9:   */ import net.minecraft.entity.player.EntityPlayerMP;
/* 10:   */ import net.minecraft.entity.player.PlayerCapabilities;
/* 11:   */ import net.minecraft.item.ItemStack;
/* 12:   */ import net.minecraft.network.NetHandlerPlayServer;
/* 13:   */ import net.minecraft.world.World;
/* 14:   */ import net.minecraftforge.event.ForgeEventFactory;
/* 15:   */ 
/* 16:   */ public class SculptureOperationMessage
/* 17:   */   implements IMessage
/* 18:   */ {
/* 19:17 */   int[] pos = new int[4];
/* 20:   */   int x;
/* 21:   */   int y;
/* 22:   */   int z;
/* 23:   */   Block block;
/* 24:   */   int meta;
/* 25:   */   int flags;
/* 26:   */   
/* 27:   */   public SculptureOperationMessage() {}
/* 28:   */   
/* 29:   */   public SculptureOperationMessage(int[] pos, int x, int y, int z, Block block, int meta, int flags)
/* 30:   */   {
/* 31:26 */     this.pos = pos;
/* 32:27 */     this.x = x;this.y = y;this.z = z;
/* 33:28 */     this.block = block;this.meta = meta;
/* 34:29 */     this.flags = flags;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void fromBytes(ByteBuf buf)
/* 38:   */   {
/* 39:34 */     this.pos[0] = buf.readByte();
/* 40:35 */     this.pos[1] = buf.readByte();
/* 41:36 */     this.pos[2] = buf.readByte();
/* 42:37 */     this.pos[3] = buf.readByte();
/* 43:38 */     this.x = buf.readInt();
/* 44:39 */     this.y = buf.readInt();
/* 45:40 */     this.z = buf.readInt();
/* 46:41 */     this.block = Block.func_149729_e(buf.readInt());
/* 47:42 */     this.meta = buf.readByte();
/* 48:43 */     this.flags = buf.readByte();
/* 49:   */   }
/* 50:   */   
/* 51:   */   public void toBytes(ByteBuf buf)
/* 52:   */   {
/* 53:48 */     buf.writeByte(this.pos[0]);
/* 54:49 */     buf.writeByte(this.pos[1]);
/* 55:50 */     buf.writeByte(this.pos[2]);
/* 56:51 */     buf.writeByte(this.pos[3]);
/* 57:52 */     buf.writeInt(this.x);
/* 58:53 */     buf.writeInt(this.y);
/* 59:54 */     buf.writeInt(this.z);
/* 60:55 */     buf.writeInt(Block.func_149682_b(this.block));
/* 61:56 */     buf.writeByte(this.meta);
/* 62:57 */     buf.writeByte(this.flags);
/* 63:   */   }
/* 64:   */   
/* 65:   */   public static class SculptureOperationHandler
/* 66:   */     implements IMessageHandler<SculptureOperationMessage, IMessage>
/* 67:   */   {
/* 68:   */     public IMessage onMessage(SculptureOperationMessage message, MessageContext ctx)
/* 69:   */     {
/* 70:66 */       World w = ctx.getServerHandler().field_147369_b.field_70170_p;
/* 71:67 */       if (Operations.validOperation(w, message.x, message.y, message.z, message.pos, message.flags)) {
/* 72:68 */         Operations.applyOperation(w, message.x, message.y, message.z, message.pos, message.flags, message.block, message.meta);
/* 73:   */       }
/* 74:70 */       EntityPlayer ep = ctx.getServerHandler().field_147369_b;
/* 75:71 */       ItemStack is = ep.func_71045_bC();
/* 76:73 */       if ((message.flags & 0x10) > 0)
/* 77:   */       {
/* 78:74 */         is.func_77972_a(1, ep);
/* 79:   */       }
/* 80:75 */       else if (((0x20 & message.flags) > 0) && 
/* 81:76 */         (!ep.field_71075_bZ.field_75098_d))
/* 82:   */       {
/* 83:77 */         is.field_77994_a -= 1;
/* 84:78 */         if (is.field_77994_a <= 0)
/* 85:   */         {
/* 86:79 */           ForgeEventFactory.onPlayerDestroyItem(ep, is);
/* 87:80 */           ep.field_71071_by.field_70462_a[ep.field_71071_by.field_70461_c] = null;
/* 88:   */         }
/* 89:   */       }
/* 90:85 */       return null;
/* 91:   */     }
/* 92:   */   }
/* 93:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.sculpture.SculptureOperationMessage
 * JD-Core Version:    0.7.0.1
 */