package hx.minepainter.sculpture;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class SculptureOperationMessage implements IMessage {

  int[] pos = new int[4];
  int x, y, z;
  Block block;
  int meta;
  int flags;

  public SculptureOperationMessage() {
  }

  public SculptureOperationMessage(int[] pos, int x, int y, int z, Block block, int meta, int flags) {
    this.pos = pos;
    this.x = x;
    this.y = y;
    this.z = z;
    this.block = block;
    this.meta = meta;
    this.flags = flags;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    pos[0] = buf.readByte();
    pos[1] = buf.readByte();
    pos[2] = buf.readByte();
    pos[3] = buf.readByte();
    x = buf.readInt();
    y = buf.readInt();
    z = buf.readInt();
    block = Block.getBlockById(buf.readInt());
    meta = buf.readByte();
    flags = buf.readByte();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeByte(pos[0]);
    buf.writeByte(pos[1]);
    buf.writeByte(pos[2]);
    buf.writeByte(pos[3]);
    buf.writeInt(x);
    buf.writeInt(y);
    buf.writeInt(z);
    buf.writeInt(Block.getIdFromBlock(block));
    buf.writeByte(meta);
    buf.writeByte(flags);
  }

  public static class SculptureOperationHandler implements IMessageHandler<SculptureOperationMessage, IMessage> {

    @Override
    public IMessage onMessage(SculptureOperationMessage message,
            MessageContext ctx) {

      World w = ctx.getServerHandler().playerEntity.worldObj;
      if (Operations.validOperation(w, message.x, message.y, message.z, message.pos, message.flags)) {
        Operations.applyOperation(w, message.x, message.y, message.z, message.pos, message.flags, message.block, message.meta);
      }

      EntityPlayer ep = ctx.getServerHandler().playerEntity;
      ItemStack is = ep.getCurrentEquippedItem();

      if ((message.flags & Operations.DAMAGE) > 0) {
        is.damageItem(1, ep);
      } else if ((Operations.CONSUME & message.flags) > 0) {
        if (!ep.capabilities.isCreativeMode) {
          is.stackSize--;
          if (is.stackSize <= 0) {
            ForgeEventFactory.onPlayerDestroyItem(ep, is);
            ep.inventory.mainInventory[ep.inventory.currentItem] = null;
          }
        }
      }

      return null;
    }

  }
}
