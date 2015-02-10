package two.davincing.painting;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import two.davincing.DaVincing;
import two.davincing.ProxyBase;
import two.davincing.renderer.PaintingTexture;

public class CommandImportPainting extends CommandBase {

  @Override
  public String getCommandName() {
    return "importImage";
  }

  protected static final List<String> aliases = Arrays.asList(new String[]{"iI"});

  @Override
  public List getCommandAliases() {
    return aliases;
  }

  @Override
  public String getCommandUsage(ICommandSender var1) {
    return StatCollector.translateToLocal("msg.imageDownload.usage");
  }

  @Override
  public void processCommand(ICommandSender sender, String[] message) {
    if ((message == null) || (message.length == 0) || (message.length == 2) || (message.length > 3)) {
      sender.addChatMessage(new ChatComponentTranslation("msg.imageDownload.usage"));
      return;
    }
    int w = 1, h = 1;
    final String locationStr = message[0];
    try {
      if (message.length == 3) {
        w = Integer.parseInt(message[1]);
        h = Integer.parseInt(message[2]);
      }
      if ((w > 9 * 4) || (h > 9 * 4) || (w * h > 9 * 4)) {
        sender.addChatMessage(new ChatComponentTranslation("msg.imageImageTooLarge.txt"));
      } else {
        final BufferedImage downloadedImage = readFromLocation(locationStr);
        if (downloadedImage != null) {
          final BufferedImage centeredImage = new BufferedImage(PaintingTexture.DEFAULT_WIDTH * w, PaintingTexture.DEFAULT_WIDTH * h, BufferedImage.TYPE_INT_ARGB);
          final Image scaledImage = getScaledImage(downloadedImage, centeredImage.getWidth(), centeredImage.getHeight());
          final int x = Math.max(0, (centeredImage.getWidth() - scaledImage.getWidth(null)) / 2);
          final int y = Math.max(0, (centeredImage.getHeight() - scaledImage.getHeight(null)) / 2);
          centeredImage.getGraphics().drawImage(scaledImage, x, y, scaledImage.getWidth(null), scaledImage.getHeight(null), null);

          for (h = 0; h < centeredImage.getHeight(); h += PaintingTexture.DEFAULT_HEIGHT) {
            for (w = 0; w < centeredImage.getWidth(); w += PaintingTexture.DEFAULT_WIDTH) {
              final BufferedImage slot = centeredImage.getSubimage(w, h, PaintingTexture.DEFAULT_WIDTH, PaintingTexture.DEFAULT_HEIGHT);
              final ByteArrayOutputStream baos = new ByteArrayOutputStream();
              ImageIO.write(slot, "png", baos);
              final ItemStack is = new ItemStack(ProxyBase.itemCanvas);
              final NBTTagCompound nbt = new NBTTagCompound();
              nbt.setByteArray("image_data", baos.toByteArray());
              is.setTagCompound(nbt);

              final EntityItem entityitem = new EntityItem(sender.getEntityWorld(),
                      sender.getPlayerCoordinates().posX + 0.5f,
                      sender.getPlayerCoordinates().posY + 0.5f,
                      sender.getPlayerCoordinates().posZ + 0.5f, is);
              entityitem.delayBeforeCanPickup = 0;
              sender.getEntityWorld().spawnEntityInWorld(entityitem);
            }
          }
        } else {
          sender.addChatMessage(new ChatComponentTranslation("msg.imageDownloadFailedNoImage.txt", locationStr));
        }
      }
    } catch (IOException e) {
      sender.addChatMessage(new ChatComponentTranslation("msg.imageDownloadFailed.txt", locationStr, e.getLocalizedMessage()));
    } catch (NumberFormatException e) {
      sender.addChatMessage(new ChatComponentTranslation("msg.imageDownloadFailed.txt", locationStr, e.getLocalizedMessage()));
    } catch (Throwable t) {
      sender.addChatMessage(new ChatComponentTranslation("msg.imageDownloadFailed.txt", locationStr, "internal error"));
      DaVincing.log.info("Dowload of image '" + String.valueOf(locationStr) + "' failed: ", t);
    }
  }

  protected Image getScaledImage(final BufferedImage source, final int widthMax, final int heightMax) {
    if ((source.getWidth() <= widthMax) && (source.getHeight() <= heightMax)) {
      return source;
    } else {
      final double scale = ((double) Math.min(widthMax, heightMax)) / ((double) Math.max(source.getWidth(), source.getHeight()));
      return source.getScaledInstance((int) (scale * source.getWidth()), (int) (scale * source.getHeight()), Image.SCALE_SMOOTH);
    }
  }

  protected BufferedImage readFromLocation(final String locationStr) throws IOException {
    if (locationStr.startsWith("http://")) {
      return ImageIO.read(new URL(locationStr));
    } else {
      return ImageIO.read(new File(locationStr));
    }
  }
}
