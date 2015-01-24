/*  1:   */ package hx.minepainter.painting;
/*  2:   */ 
/*  3:   */ import java.awt.image.BufferedImage;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.net.MalformedURLException;
/*  6:   */ import java.net.URL;
/*  7:   */ import java.util.concurrent.LinkedBlockingQueue;
/*  8:   */ import javax.imageio.ImageIO;
/*  9:   */ import net.minecraft.command.CommandBase;
/* 10:   */ import net.minecraft.command.ICommandSender;
/* 11:   */ 
/* 12:   */ public class CommandImportPainting
/* 13:   */   extends CommandBase
/* 14:   */ {
/* 15:15 */   private static LinkedBlockingQueue<Runnable> tasks = new LinkedBlockingQueue();
/* 16:   */   private static Thread worker;
/* 17:   */   
/* 18:   */   public String func_71517_b()
/* 19:   */   {
/* 20:20 */     return "mpimport";
/* 21:   */   }
/* 22:   */   
/* 23:   */   public String func_71518_a(ICommandSender var1)
/* 24:   */   {
/* 25:25 */     return "mpimport <image url> [--size <w> <h>]\nto import image as w * h pieces of 16x16 paintings";
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void func_71515_b(ICommandSender var1, String[] var2)
/* 29:   */   {
/* 30:31 */     startWorking();
/* 31:32 */     int w = 1;int h = 1;
/* 32:33 */     String url = var2[0];
/* 33:34 */     for (int i = 0; i < var2.length; i++) {
/* 34:35 */       if ((var2[i].equals("--size")) && (var2.length - i > 2))
/* 35:   */       {
/* 36:36 */         w = Integer.parseInt(var2[(i + 1)]);
/* 37:37 */         h = Integer.parseInt(var2[(i + 2)]);
/* 38:   */       }
/* 39:   */     }
/* 40:   */     try
/* 41:   */     {
/* 42:41 */       img = ImageIO.read(new URL(url));
/* 43:   */     }
/* 44:   */     catch (MalformedURLException e)
/* 45:   */     {
/* 46:   */       BufferedImage img;
/* 47:44 */       e.printStackTrace();
/* 48:   */     }
/* 49:   */     catch (IOException e)
/* 50:   */     {
/* 51:46 */       e.printStackTrace();
/* 52:   */     }
/* 53:   */   }
/* 54:   */   
/* 55:   */   private void startWorking()
/* 56:   */   {
/* 57:52 */     if ((worker == null) || (!worker.isAlive()))
/* 58:   */     {
/* 59:53 */       worker = new Thread()
/* 60:   */       {
/* 61:   */         public void run()
/* 62:   */         {
/* 63:   */           try
/* 64:   */           {
/* 65:   */             for (;;)
/* 66:   */             {
/* 67:57 */               ((Runnable)CommandImportPainting.tasks.take()).run();
/* 68:   */             }
/* 69:   */           }
/* 70:   */           catch (InterruptedException e)
/* 71:   */           {
/* 72:59 */             e.printStackTrace();
/* 73:   */           }
/* 74:   */         }
/* 75:62 */       };
/* 76:63 */       worker.start();
/* 77:   */     }
/* 78:   */   }
/* 79:   */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.painting.CommandImportPainting
 * JD-Core Version:    0.7.0.1
 */