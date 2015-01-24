package hx.minepainter.painting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PaintingEntity
        extends TileEntity {

  BufferedImage image;
  @SideOnly(Side.CLIENT)
  private PaintingIcon icon;

  public PaintingEntity() {
    this.image = new BufferedImage(16, 16, 2);
    for (int i = 0; i < 16; i++) {
      this.image.setRGB(i, 0, getColorForDye(i) | 0xFF000000);
    }
    Graphics g = this.image.getGraphics();
    g.setColor(Color.white);
    g.fillRect(0, 1, 16, 15);
  }

  public int getColorForDye(int dye_index) {
    return net.minecraft.item.ItemDye.field_150922_c[dye_index];
  }

  public BufferedImage getImg() {
    return this.image;
  }

  @SideOnly(Side.CLIENT)
  public PaintingIcon getIcon() {
    if (this.icon == null) {
      this.icon = PaintingCache.get();
    }
    return this.icon;
  }

  public void func_145843_s() {
    super.func_145843_s();
    if (this.field_145850_b.isRemote) {
      getIcon().release();
      this.icon = null;
    }
  }

  public void onChunkUnload() {
    super.onChunkUnload();
    if (this.field_145850_b.isRemote) {
      getIcon().release();
      this.icon = null;
    }
  }

  public void func_145841_b(NBTTagCompound nbt) {
    super.func_145841_b(nbt);
    writeImageToNBT(nbt);
  }

  public void writeImageToNBT(NBTTagCompound nbt) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      ImageIO.write(this.image, "png", baos);
    } catch (IOException e) {
      e.printStackTrace();
    }
    nbt.func_74773_a("image_data", baos.toByteArray());
  }

  public void func_145839_a(NBTTagCompound nbt) {
    readFromNBTToImage(nbt);
    super.func_145839_a(nbt);
  }

  public void readFromNBTToImage(NBTTagCompound nbt) {
    if (nbt == null) {
      return;
    }
    byte[] data = nbt.func_74770_j("image_data");
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    try {
      BufferedImage img = ImageIO.read(bais);
      this.image = img;
      if ((this.field_145850_b != null) && (this.field_145850_b.isRemote)) {
        getIcon().fill(img);
      }
    } catch (IOException e) {
      getIcon().fill(this.image);
    }
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
