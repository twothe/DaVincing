package two.davincing.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import two.davincing.DaVincing;

public abstract class Texture extends PixelData implements IIcon {

  protected final IntBuffer pixelBuf;
  protected int id;
  protected boolean initialized;

  public Texture(final int width, final int height) {
    this(width, height, -1);
  }

  protected Texture(final int width, final int height, final int id) {
    super(width, height);
    this.id = id;
    this.initialized = (id > 0);
    this.pixelBuf = ByteBuffer.allocateDirect(width * height * Integer.SIZE / 8).order(ByteOrder.nativeOrder()).asIntBuffer();
  }

  @SideOnly(Side.CLIENT)
  public void initializeGL() {
    if (this.initialized == false) {
      if (id < 0) {
        this.id = GL11.glGenTextures();
        if (this.id <= 0) {
          DaVincing.log.error("Unable to generate new texture");
          throw new IllegalStateException("Unable to create new texture");
        }
      }

      if (this.id > 0) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
        setWrapBehaviour();
        setFilters();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
      }
      this.initialized = true;
    }
  }

  @SideOnly(Side.CLIENT)
  protected void setWrapBehaviour() {
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
  }

  @SideOnly(Side.CLIENT)
  protected void setFilters() {
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
  }

  // free up the resources used by the GL texture
  @SideOnly(Side.CLIENT)
  public void dispose() {
    if (this.id > 0) {
      GL11.glDeleteTextures(this.id);
      this.id = -1;
      this.initialized = false;
    }
  }

  @SideOnly(Side.CLIENT)
  public void bind() {
    if (this.initialized == false) {
      throw new IllegalStateException("Texture must be initialized before first use");
    }
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
    this.updateTextureData();
  }

  @SideOnly(Side.CLIENT)
  public void unbind() {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
  }

  @SideOnly(Side.CLIENT)
  protected void updateTextureData() {
    if (this.initialized == false) {
      throw new IllegalStateException("Texture must be initialized before first use");
    }
    if (hasChanged.compareAndSet(true, false)) {
      synchronized (this.pixels) {
        this.pixelBuf.clear();
        this.pixelBuf.put(this.pixels);
        this.pixelBuf.flip();
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, this.width, this.height, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, this.pixelBuf);
      }
    }
  }

  @SideOnly(Side.CLIENT)
  public void fetchTextureData() {
    if (this.initialized == false) {
      throw new IllegalStateException("Texture must be initialized before first use");
    }
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
    synchronized (this.pixels) {
      this.pixelBuf.clear();
      GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, this.pixelBuf);
      // getTexImage does not seem to advance the buffer position, so flip does not work here
      this.pixelBuf.limit(this.width * this.height);
      this.pixelBuf.position(0);
      this.pixelBuf.get(this.pixels);
    }
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
  }

  @Override
  public int getIconWidth() {
    return this.width;
  }

  @Override
  public int getIconHeight() {
    return this.height;
  }

  @Override
  public float getMinU() {
    return 0.0f;
  }

  @Override
  public float getMaxU() {
    return 1.0f;
  }

  @Override
  public float getInterpolatedU(final double step) {
    return (float) (getMinU() + (getMaxU() - getMinU()) * step / 16.0); // interpolates 0-16 on umin-umax
  }

  @Override
  public float getMinV() {
    return 0.0f;
  }

  @Override
  public float getMaxV() {
    return 1.0f;
  }

  @Override
  public float getInterpolatedV(double step) {
    return (float) (getMinV() + (getMaxV() - getMinV()) * step / 16.0); // interpolates 0-16 on vmin-vmax
  }

}
