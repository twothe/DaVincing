/*  1:   */ package hx.minepainter.painting;
/*  2:   */ 
/*  3:   */ import cpw.mods.fml.common.network.simpleimpl.IMessage;
/*  4:   */ import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
/*  5:   */ import cpw.mods.fml.common.network.simpleimpl.MessageContext;
/*  6:   */ import io.netty.buffer.ByteBuf;
/*  7:   */ import net.minecraft.entity.player.EntityPlayerMP;
/*  8:   */ import net.minecraft.item.Item;
/*  9:   */ import net.minecraft.network.NetHandlerPlayServer;
/* 10:   */ 
/* 11:   */ public class PaintingOperationMessage
/* 12:   */   implements IMessage
/* 13:   */ {
/* 14:   */   PaintTool tool;
/* 15:   */   int x;
/* 16:   */   int y;
/* 17:   */   int z;
/* 18:   */   float xs;
/* 19:   */   float ys;
/* 20:   */   float zs;
/* 21:   */   int color;
/* 22:   */   
/* 23:   */   public PaintingOperationMessage() {}
/* 24:   */   
/* 25:   */   public PaintingOperationMessage(PaintTool tool, int x, int y, int z, float xs, float ys, float zs, int color)
/* 26:   */   {
/* 27:19 */     this.tool = tool;
/* 28:20 */     this.x = x;this.y = y;this.z = z;
/* 29:21 */     this.xs = xs;this.ys = ys;this.zs = zs;
/* 30:22 */     this.color = color;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void fromBytes(ByteBuf buf)
/* 34:   */   {
/* 35:27 */     this.tool = ((PaintTool)Item.func_150899_d(buf.readInt()));
/* 36:28 */     this.x = buf.readInt();this.y = buf.readInt();this.z = buf.readInt();
/* 37:29 */     this.xs = buf.readFloat();this.ys = buf.readFloat();this.zs = buf.readFloat();
/* 38:30 */     this.color = buf.readInt();
/* 39:   */   }
/* 40:   */   
/* 41:   */   public void toBytes(ByteBuf buf)
/* 42:   */   {
/* 43:35 */     buf.writeInt(Item.func_150891_b(this.tool));
/* 44:36 */     buf.writeInt(this.x);buf.writeInt(this.y);buf.writeInt(this.z);
/* 45:37 */     buf.writeFloat(this.xs);buf.writeFloat(this.ys);buf.writeFloat(this.zs);
/* 46:38 */     buf.writeInt(this.color);
/* 47:   */   }
/* 48:   */   
/* 49:   */   public static class PaintingOperationHandler
/* 50:   */     implements IMessageHandler<PaintingOperationMessage, IMessage>
/* 51:   */   {
/* 52:   */     public IMessage onMessage(PaintingOperationMessage message, MessageContext ctx)
/* 53:   */     {
/* 54:47 */       message.tool.paintAt(ctx.getServerHandler().field_147369_b.field_70170_p, message.x, message.y, message.z, message.xs, message.ys, message.zs, message.color);
/* 55:   */       
/* 56:   */ 
/* 57:   */ 
/* 58:   */ 
/* 59:52 */       return null;
/* 60:   */     }
/* 61:   */   }
/* 62:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.painting.PaintingOperationMessage
 * JD-Core Version:    0.7.0.1
 */