/*
 */
package two.davincing.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * @author Two
 */
public class PaintingOperationHandler implements IMessageHandler<PaintingOperationMessage, IMessage> {

  @Override
  public IMessage onMessage(PaintingOperationMessage message, MessageContext ctx) {
    message.tool.paintAt(ctx.getServerHandler().playerEntity.worldObj,
            message.x, message.y, message.z,
            message.xs, message.ys, message.zs,
            message.color, ctx.getServerHandler().playerEntity.isSneaking());
    return null;
  }
}
