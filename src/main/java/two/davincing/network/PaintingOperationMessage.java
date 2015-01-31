package two.davincing.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import two.davincing.painting.PaintTool;

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
}
