package hx.minepainter.sculpture;

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
    return this.sculpture;
  }

  @SideOnly(Side.CLIENT)
  public SculptureRenderCompiler getRender() {
    if (this.render == null) {
      this.render = new SculptureRenderCompiler();
    }
    return this.render;
  }

  @SideOnly(Side.CLIENT)
  public void updateRender() {
    if (this.worldObj.isRemote) {
      BlockSlice slice = BlockSlice.at(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
      getRender().update(slice);
      BlockSlice.clear();
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void invalidate() {
    super.invalidate();
    if (this.worldObj.isRemote) {
      getRender().clear();
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onChunkUnload() {
    super.onChunkUnload();
    if (this.worldObj.isRemote) {
      getRender().clear();
    }
  }

  @Override
  public void writeToNBT(NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    this.sculpture.write(nbt);
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    if ((this.worldObj != null) && (this.worldObj.isRemote)) {
      getRender().changed = true;
    }
    super.readFromNBT(nbt);
    this.sculpture.read(nbt);
  }

  @Override
  public Packet getDescriptionPacket() {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    writeToNBT(nbttagcompound);
    return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 17, nbttagcompound);
  }

  @Override
  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    readFromNBT(pkt.func_148857_g());
  }
}
