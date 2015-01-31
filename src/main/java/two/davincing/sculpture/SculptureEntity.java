package two.davincing.sculpture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class SculptureEntity extends TileEntity {

  Sculpture sculpture = new Sculpture();

  @SideOnly(Side.CLIENT)
  private SculptureRenderCompiler render;

  public Sculpture sculpture() {
    return sculpture;
  }

  @SideOnly(Side.CLIENT)
  public SculptureRenderCompiler getRender() {
    if (render == null) {
      render = new SculptureRenderCompiler();
    }
    return render;
  }

  @SideOnly(Side.CLIENT)
  public void updateRender() {
    if (this.worldObj.isRemote) {
      BlockSlice slice = BlockSlice.at(worldObj, xCoord, yCoord, zCoord);
      getRender().update(slice);
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void invalidate() {
    super.invalidate();
    if (this.worldObj.isRemote) {
      getRender().clear();
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void onChunkUnload() {
    super.onChunkUnload();
    if (this.worldObj.isRemote) {
      getRender().clear();
    }
  }

	//TileEntity util
  @Override
  public void writeToNBT(NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    sculpture.write(nbt);
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    if (worldObj != null && worldObj.isRemote) {
      getRender().changed = true;
    }
    super.readFromNBT(nbt);
    sculpture.read(nbt);
  }

  @Override
  public Packet getDescriptionPacket() {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    this.writeToNBT(nbttagcompound);
    return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 17, nbttagcompound);
  }

  @Override
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    readFromNBT(pkt.func_148857_g());
  }
}
