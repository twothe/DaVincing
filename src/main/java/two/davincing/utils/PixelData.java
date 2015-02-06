/*
 */
package two.davincing.utils;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Two
 */
public class PixelData {

  public static int[] getPixelsFromImage(final BufferedImage image) {
    return image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
  }

  public final int size;
  public final int width;
  public final int height;
  public final AtomicBoolean hasChanged = new AtomicBoolean(false);
  protected final int[] pixels;

  // allocate new texture and fill from IntBuffer
  public PixelData(final int width, final int height) {
    if (width < 0) {
      throw new IllegalArgumentException("PixelData width is invalid. Must be > 0, but got " + width);
    }
    if (height < 0) {
      throw new IllegalArgumentException("PixelData height is invalid. Must be > 0, but got " + height);
    }
    this.width = width;
    this.height = height;
    this.size = this.width * this.height;
    this.pixels = new int[size];
  }

  public int[] getRGB() {
    synchronized (this.pixels) {
      return Arrays.copyOf(this.pixels, this.size);
    }
  }

  // Copy a rectangular sub-region of dimensions 'w' x 'h' from the pixel buffer to the array 'pixels'.
  public void getRGB(final int x, final int y, final int width, final int height, final int[] pixels, final int offset) {
    if ((x < 0) || (x >= this.width)) {
      throw new ArrayIndexOutOfBoundsException("X position must be within data size. Expected 0 <= x < " + this.width + ", but got " + x);
    }
    if ((y < 0) || (y >= this.height)) {
      throw new ArrayIndexOutOfBoundsException("Y position must be within data size. Expected 0 <= y < " + this.height + ", but got " + y);
    }
    if ((width < 0) || (width > this.width)) {
      throw new ArrayIndexOutOfBoundsException("Width must be within data size. Expected 0 <= width <= " + this.width + ", but got " + width);
    }
    if ((height < 0) || (height > this.height)) {
      throw new ArrayIndexOutOfBoundsException("Height must be within data size. Expected 0 <= height <= " + this.height + ", but got " + height);
    }
    if ((x + width < 0) || (x + width > this.width)) {
      throw new ArrayIndexOutOfBoundsException("X + width  must be within data size. Expected 0 <= x + width <= " + this.width + ", but got " + (x + width));
    }
    if ((y + height < 0) || (y + height > this.height)) {
      throw new ArrayIndexOutOfBoundsException("Y + height  must be within data size. Expected 0 <= y + height <= " + this.height + ", but got " + (x + height));
    }
    if ((offset < 0) || (offset >= pixels.length)) {
      throw new ArrayIndexOutOfBoundsException("Offset position must be within output size. Expected 0 <= offset < " + pixels.length + ", but got " + offset);
    }
    if (pixels.length - offset < width * height) {
      throw new ArrayIndexOutOfBoundsException("Target array size (including offset) must be big enough to hold the requested area. Expected 0 < size{" + (pixels.length - offset) + "} >= " + (width * height) + " but got (length{" + pixels.length + "} - offset{" + offset + "}");
    }
    final int bufferOffset = (y * this.width) + x;
    synchronized (this.pixels) {
      for (int line = 0; line < height; ++line) {
        System.arraycopy(this.pixels, bufferOffset + line * this.width, pixels, offset + line * width, width);
      }
    }
  }

  public int getRGB(final int x, final int y) {
    if ((x < 0) || (x >= this.width)) {
      throw new ArrayIndexOutOfBoundsException("X position must be within data size. Expected 0 <= x < " + this.width + ", but got " + x);
    }
    if ((y < 0) || (y >= this.height)) {
      throw new ArrayIndexOutOfBoundsException("Y position must be within data size. Expected 0 <= y < " + this.height + ", but got " + y);
    }
    final int position = y * this.width + x;
    return this.pixels[position];
  }

  public void setRGB(final BufferedImage image) {
    this.setRGB(getPixelsFromImage(image));
  }

  public void setRGB(final int[] pixels) {
    if (pixels.length != this.pixels.length) {
      throw new IllegalArgumentException("Pixel size does not match. Expected " + this.pixels.length + " but got " + pixels.length);
    }
    synchronized (this.pixels) {
      hasChanged.set(true);
      System.arraycopy(pixels, 0, this.pixels, 0, this.pixels.length);
    }
  }

  // Copy a rectangular sub-region of dimensions 'w' x 'h' from the array 'pixels' to the pixel buffer.
  public void setRGB(final int x, final int y, final int width, final int height, final int[] pixels, final int offset) {
    if ((x < 0) || (x >= this.width)) {
      throw new ArrayIndexOutOfBoundsException("X position must be within data size. Expected 0 <= x < " + this.width + ", but got " + x);
    }
    if ((y < 0) || (y >= this.height)) {
      throw new ArrayIndexOutOfBoundsException("Y position must be within data size. Expected 0 <= y < " + this.height + ", but got " + y);
    }
    if ((width < 0) || (width > this.width)) {
      throw new ArrayIndexOutOfBoundsException("Width must be within data size. Expected 0 <= width <= " + this.width + ", but got " + width);
    }
    if ((height < 0) || (height > this.height)) {
      throw new ArrayIndexOutOfBoundsException("Height must be within data size. Expected 0 <= height <= " + this.height + ", but got " + height);
    }
    if ((x + width < 0) || (x + width > this.width)) {
      throw new ArrayIndexOutOfBoundsException("X + width  must be within data size. Expected 0 <= x + width <= " + this.width + ", but got " + (x + width));
    }
    if ((y + height < 0) || (y + height > this.height)) {
      throw new ArrayIndexOutOfBoundsException("Y + height  must be within data size. Expected 0 <= y + height <= " + this.height + ", but got " + (x + height));
    }
    if ((offset < 0) || (offset >= pixels.length)) {
      throw new ArrayIndexOutOfBoundsException("Offset position must be within input size. Expected 0 <= offset < " + pixels.length + ", but got " + offset);
    }
    if (pixels.length - offset < width * height) {
      throw new ArrayIndexOutOfBoundsException("Source array size (including offset) must be >= the area requested to set. Expected size{" + (pixels.length - offset) + "} >= " + (width * height) + " but got (length{" + pixels.length + "} - offset{" + offset + "}");
    }
    final int bufferOffset = x + (y * this.width);
    synchronized (this.pixels) {
      hasChanged.set(true);
      for (int line = 0; line < height; ++line) {
        System.arraycopy(pixels, offset + line * width, this.pixels, bufferOffset + line * this.width, width);
      }
    }
  }

  public void setRGB(final int x, final int y, final int color) {
    if ((x < 0) || (x >= this.width)) {
      throw new ArrayIndexOutOfBoundsException("X position must be within data size. Expected 0 <= x < " + this.width + ", but got " + x);
    }
    if ((y < 0) || (y >= this.height)) {
      throw new ArrayIndexOutOfBoundsException("Y position must be within data size. Expected 0 <= y < " + this.height + ", but got " + y);
    }
    final int position = y * this.width + x;
    synchronized (this.pixels) {
      if (pixels[position] != color) {
        hasChanged.set(true);
        pixels[position] = color;
      }
    }
  }

  public void fillRGB(final int color) {
    synchronized (this.pixels) {
      hasChanged.set(true);
      Arrays.fill(this.pixels, color);
    }
  }

  public BufferedImage asImage() {
    final BufferedImage result = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
    synchronized (this.pixels) {
      result.setRGB(0, 0, this.width, this.height, this.pixels, 0, this.width);
    }
    return result;
  }
}
