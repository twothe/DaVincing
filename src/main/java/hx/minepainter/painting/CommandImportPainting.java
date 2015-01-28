package hx.minepainter.painting;

import hx.minepainter.ModMinePainter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CommandImportPainting extends CommandBase {
	private static LinkedBlockingQueue<Runnable> tasks = new LinkedBlockingQueue<Runnable>();
	private static Thread worker;
	
	@Override
	public String getCommandName() {
		return "mpimport";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "mpimport <image url> [--size <w> <h>]\n" +
				"to import image as w * h pieces of 16x16 paintings";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		startWorking();
		int w = 1,h = 1;
		String url = var2[0];
		for(int i = 0 ; i < var2.length; i++){
			if(var2[i].equals("--size") && var2.length - i > 2){
				w = Integer.parseInt(var2[i+1]);
				h = Integer.parseInt(var2[i+2]);
			}
		}
		try {
			BufferedImage img = null;
			if(url.startsWith("http://")){
				img = ImageIO.read(new URL(url));
			}else{
				img = ImageIO.read(new File(url));
			}
			
			for(int i = 0; i < w;i ++)
				for(int j = 0; j < h; j++){
					
					BufferedImage slot = new BufferedImage(16,16, BufferedImage.TYPE_INT_ARGB);
					Graphics g = slot.getGraphics();
					g.drawImage(img, 0, 0, 16, 16, i * img.getWidth() / w, 
																    j * img.getHeight() / h, 
																    (i+1) * img.getWidth() / w, 
																    (j+1) * img.getHeight() / h, null);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(slot, "png", baos);
					ItemStack is = new ItemStack(ModMinePainter.canvas.item);
			    	NBTTagCompound nbt = new NBTTagCompound();
			    	nbt.setByteArray("image_data", baos.toByteArray());
			    	is.setTagCompound(nbt);
			    	
			    	EntityItem entityitem = new EntityItem(var1.getEntityWorld(), 
			    			var1.getPlayerCoordinates().posX + 0.5f, 
			    			var1.getPlayerCoordinates().posY + 0.5f, 
			    			var1.getPlayerCoordinates().posZ + 0.5f, is);
		            entityitem.delayBeforeCanPickup = 0;
		            var1.getEntityWorld().spawnEntityInWorld(entityitem);
				}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void startWorking(){
		if(worker == null || !worker.isAlive()){
			worker = new Thread(){
				@Override public void run(){
					while(true)
						try {
							tasks.take().run();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
			};
			worker.start();
		}
	}
}
