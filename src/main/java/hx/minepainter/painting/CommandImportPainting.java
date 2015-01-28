package hx.minepainter.painting;

import java.util.concurrent.LinkedBlockingQueue;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandImportPainting extends CommandBase {

  private static final LinkedBlockingQueue<Runnable> tasks = new LinkedBlockingQueue();
  private static Thread worker;

  @Override
  public String getCommandName() {
    return "mpimport";
  }

  @Override
  public String getCommandUsage(ICommandSender var1) {
    return "mpimport <image url> [--size <w> <h>]\nto import image as w * h pieces of 16x16 paintings";
  }

  @Override
  public void processCommand(ICommandSender var1, String[] parameters) {
//    startWorking();
    int w = 1;
    int h = 1;
    String url = parameters[0];
    for (int i = 0; i < parameters.length; i++) {
      if ((parameters[i].equals("--size")) && (parameters.length - i > 2)) {
        w = Integer.parseInt(parameters[(i + 1)]);
        h = Integer.parseInt(parameters[(i + 2)]);
      }
    }
    //TODO: not yet implemented
//    try {
//      img = ImageIO.read(new URL(url));
//    } catch (MalformedURLException e) {
//      BufferedImage img;
//      e.printStackTrace();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
  }

  private void startWorking() {
    if ((worker == null) || (!worker.isAlive())) {
      worker = new Thread() {
        @Override
        public void run() {
          try {
            for (;;) {
              ((Runnable) CommandImportPainting.tasks.take()).run();
            }
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      };
      worker.start();
    }
  }
}
