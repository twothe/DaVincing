package two.davincing.painting;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import two.davincing.DaVincing;
import two.davincing.ProxyBase;
import two.davincing.renderer.PaintingTexture;

public class PaintingEntity extends TileEntity {

  protected static BufferedImage imageFromNBT(final NBTTagCompound nbt) {
    try {
      byte[] data = nbt.getByteArray("image_data");
      return ImageIO.read(new ByteArrayInputStream(data));
    } catch (Exception e) {
      DaVincing.log.warn("Unable to read image from NBT data. Image might be lost.", e);
      return new PaintingTexture().asImage();
    }
  }

  protected static void imageToNBT(final BufferedImage image, final NBTTagCompound nbt) {
    try {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(image, "png", baos);
      nbt.setByteArray("image_data", baos.toByteArray());
    } catch (Exception e) {
      DaVincing.log.warn("Unable to write image to NBT data. Image might be lost.", e);
      nbt.setByteArray("image_data", new byte[0]);
    }
  }

  public static BufferedImage getPaintingFromItem(final ItemStack itemStack) {
    final NBTTagCompound nbt = itemStack.getTagCompound();
    return imageFromNBT(nbt);
  }

  protected final PaintingTexture paintingTexture;

  public PaintingEntity() {
    this.paintingTexture = new PaintingTexture();
  }

  protected void releaseTexture() {
    if (this.worldObj.isRemote && (paintingTexture != null)) {
      paintingTexture.dispose();
    }
  }

  public PaintingTexture getTexture() {
    if (this.worldObj.isRemote) {
      paintingTexture.initializeGL();
    }
    return paintingTexture;
  }

  @Override
  public void invalidate() {
    super.invalidate();
    releaseTexture();
  }

  @Override
  public void onChunkUnload() {
    super.onChunkUnload();
    releaseTexture();
  }

  @Override
  public void writeToNBT(NBTTagCompound nbt) {
    super.writeToNBT(nbt);
    imageToNBT(this.paintingTexture.asImage(), nbt);
  }

  public void setImage(final BufferedImage image) {
    this.paintingTexture.setRGB(image);
  }

  @Override
  public void readFromNBT(final NBTTagCompound nbt) {
    this.setImage(imageFromNBT(nbt));
    super.readFromNBT(nbt);
  }

  public ItemStack getPaintingAsItem() {
    final ItemStack result = new ItemStack(ProxyBase.itemCanvas);
    final NBTTagCompound nbt = new NBTTagCompound();
    PaintingEntity.imageToNBT(paintingTexture.asImage(), nbt);
    result.setTagCompound(nbt);
    return result;
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
