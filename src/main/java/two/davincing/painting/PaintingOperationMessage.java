package two.davincing.painting;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;

public class PaintingOperationMessage implements IMessage {

  PaintTool tool;
  int x, y, z;
  float xs, ys, zs;
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
    tool = (PaintTool) Item.getItemById(buf.readInt());
    x = buf.readInt();
    y = buf.readInt();
    z = buf.readInt();
    xs = buf.readFloat();
    ys = buf.readFloat();
    zs = buf.readFloat();
    color = buf.readInt();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(Item.getIdFromItem(tool));
    buf.writeInt(x);
    buf.writeInt(y);
    buf.writeInt(z);
    buf.writeFloat(xs);
    buf.writeFloat(ys);
    buf.writeFloat(zs);
    buf.writeInt(color);
  }

  public static class PaintingOperationHandler implements IMessageHandler<PaintingOperationMessage, IMessage> {

    @Override
    public IMessage onMessage(PaintingOperationMessage message,
            MessageContext ctx) {

      message.tool.paintAt(ctx.getServerHandler().playerEntity.worldObj,
              message.x, message.y, message.z,
              message.xs, message.ys, message.zs,
              message.color, ctx.getServerHandler().playerEntity.isSneaking());

      return null;
    }

  }
}
