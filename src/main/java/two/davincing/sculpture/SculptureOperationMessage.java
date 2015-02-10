package two.davincing.sculpture;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import java.util.Arrays;
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

  public SculptureOperationMessage(final int[] pos, int x, int y, int z, Block block, int meta, int flags) {
    if ((pos == null) || (this.pos.length != pos.length)) {
      throw new IllegalStateException(Arrays.toString(pos) + " is not a valid position array");
    }
    System.arraycopy(pos, 0, this.pos, 0, this.pos.length);
    this.x = x;
    this.y = y;
    this.z = z;
    this.block = block;
    this.meta = meta;
    this.flags = flags;
  }

  @Override
  public void fromBytes(final ByteBuf buf) {
    pos[0] = buf.readByte() & 0xFF;
    pos[1] = buf.readByte() & 0xFF;
    pos[2] = buf.readByte() & 0xFF;
    pos[3] = buf.readByte() & 0xFF;
    x = buf.readInt();
    y = buf.readInt();
    z = buf.readInt();
    block = Block.getBlockById(buf.readInt());
    meta = buf.readByte() & 0xFF;
    flags = buf.readByte() & 0xFF;
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeByte(pos[0] & 0xFF);
    buf.writeByte(pos[1] & 0xFF);
    buf.writeByte(pos[2] & 0xFF);
    buf.writeByte(pos[3] & 0xFF);
    buf.writeInt(x);
    buf.writeInt(y);
    buf.writeInt(z);
    buf.writeInt(Block.getIdFromBlock(block));
    buf.writeByte(meta & 0xFF);
    buf.writeByte(flags & 0xFF);
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

      if ((message.flags & Operations.DAMAGE) != 0) {
        is.damageItem(1, ep);
      } else if ((Operations.CONSUME & message.flags) != 0) {
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
