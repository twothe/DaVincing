package hx.minepainter.painting;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;

public class PaintingOperationMessage implements IMessage {

  PaintTool tool;
  int x;
  int y;
  int z;
  float xs;
  float ys;
  float zs;
  int color;

  public PaintingOperationMessage() {
  }

  public PaintingOperationMessage(PaintTool tool, int x, int y, int z, float xs, float ys, float zs, int color) {
    this.tool = tool;
    this.x = x;
    this.y = y;
    this.z = z;
    this.xs = xs;
    this.ys = ys;
    this.zs = zs;
    this.color = color;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    this.tool = ((PaintTool) Item.getItemById(buf.readInt()));
    this.x = buf.readInt();
    this.y = buf.readInt();
    this.z = buf.readInt();
    this.xs = buf.readFloat();
    this.ys = buf.readFloat();
    this.zs = buf.readFloat();
    this.color = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(Item.getIdFromItem(this.tool));
    buf.writeInt(this.x);
    buf.writeInt(this.y);
    buf.writeInt(this.z);
    buf.writeFloat(this.xs);
    buf.writeFloat(this.ys);
    buf.writeFloat(this.zs);
    buf.writeInt(this.color);
  }

  public static class PaintingOperationHandler implements IMessageHandler<PaintingOperationMessage, IMessage> {

    @Override
    public IMessage onMessage(PaintingOperationMessage message, MessageContext ctx) {
      message.tool.paintAt(ctx.getServerHandler().playerEntity.worldObj, message.x, message.y, message.z, message.xs, message.ys, message.zs, message.color);

      return null;
    }
  }
}
