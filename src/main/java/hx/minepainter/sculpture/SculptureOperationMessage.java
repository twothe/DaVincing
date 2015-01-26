package hx.minepainter.sculpture;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class SculptureOperationMessage
        implements IMessage {

  int[] pos = new int[4];
  int x;
  int y;
  int z;
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

  public void fromBytes(ByteBuf buf) {
    this.pos[0] = buf.readByte();
    this.pos[1] = buf.readByte();
    this.pos[2] = buf.readByte();
    this.pos[3] = buf.readByte();
    this.x = buf.readInt();
    this.y = buf.readInt();
    this.z = buf.readInt();
    this.block = Block.getBlockById(buf.readInt());
    this.meta = buf.readByte();
    this.flags = buf.readByte();
  }

  public void toBytes(ByteBuf buf) {
    buf.writeByte(this.pos[0]);
    buf.writeByte(this.pos[1]);
    buf.writeByte(this.pos[2]);
    buf.writeByte(this.pos[3]);
    buf.writeInt(this.x);
    buf.writeInt(this.y);
    buf.writeInt(this.z);
    buf.writeInt(Block.getIdFromBlock(this.block));
    buf.writeByte(this.meta);
    buf.writeByte(this.flags);
  }

  public static class SculptureOperationHandler
          implements IMessageHandler<SculptureOperationMessage, IMessage> {

    public IMessage onMessage(SculptureOperationMessage message, MessageContext ctx) {
      World w = ctx.getServerHandler().field_147369_b.worldObj;
      if (Operations.validOperation(w, message.x, message.y, message.z, message.pos, message.flags)) {
        Operations.applyOperation(w, message.x, message.y, message.z, message.pos, message.flags, message.block, message.meta);
      }
      EntityPlayer ep = ctx.getServerHandler().field_147369_b;
      ItemStack is = ep.getHeldItem();
      if ((message.flags & 0x10) > 0) {
        is.func_77972_a(1, ep);
      } else if (((0x20 & message.flags) > 0)
              && (!ep.field_71075_bZ.field_75098_d)) {
        is.stackSize -= 1;
        if (is.stackSize <= 0) {
          ForgeEventFactory.onPlayerDestroyItem(ep, is);
          ep.inventory.field_70462_a[ep.inventory.field_70461_c] = null;
        }
      }
      return null;
    }
  }
}
