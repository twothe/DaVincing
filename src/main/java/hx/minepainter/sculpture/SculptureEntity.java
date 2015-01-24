package hx.minepainter.sculpture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SculptureEntity
        extends TileEntity {

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
    if (this.field_145850_b.isRemote) {
      BlockSlice slice = BlockSlice.at(this.field_145850_b, this.field_145851_c, this.field_145848_d, this.field_145849_e);
      getRender().update(slice);
      BlockSlice.clear();
    }
  }

  @SideOnly(Side.CLIENT)
  public void func_145843_s() {
    super.func_145843_s();
    if (this.field_145850_b.isRemote) {
      getRender().clear();
    }
  }

  @SideOnly(Side.CLIENT)
  public void onChunkUnload() {
    super.onChunkUnload();
    if (this.field_145850_b.isRemote) {
      getRender().clear();
    }
  }

  public void func_145841_b(NBTTagCompound nbt) {
    super.func_145841_b(nbt);
    this.sculpture.write(nbt);
  }

  public void func_145839_a(NBTTagCompound nbt) {
    if ((this.field_145850_b != null) && (this.field_145850_b.isRemote)) {
      getRender().changed = true;
    }
    super.func_145839_a(nbt);
    this.sculpture.read(nbt);
  }

  public Packet func_145844_m() {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    func_145841_b(nbttagcompound);
    return new S35PacketUpdateTileEntity(this.field_145851_c, this.field_145848_d, this.field_145849_e, 17, nbttagcompound);
  }

  public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    func_145839_a(pkt.func_148857_g());
  }
}
