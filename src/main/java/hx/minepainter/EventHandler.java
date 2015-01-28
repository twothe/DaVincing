package hx.minepainter;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import hx.minepainter.item.CanvasRenderer;
import hx.minepainter.item.ChiselItem;
import hx.minepainter.item.PieceItem;
import hx.minepainter.painting.PaintTool;
import hx.minepainter.painting.PaintingBlock;
import hx.minepainter.painting.PaintingPlacement;
import hx.minepainter.sculpture.Operations;
import hx.minepainter.sculpture.Sculpture;
import hx.minepainter.sculpture.SculptureEntity;
import hx.minepainter.sculpture.SculptureRender;
import hx.utils.Debug;
import hx.utils.Utils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class EventHandler {	
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawPlayerHelmet(RenderPlayerEvent.Specials.Pre event){
		if(!event.renderHelmet)return;
		
		ItemStack is = event.entityPlayer.getEquipmentInSlot(4);
		if(!needsHelmetRenderHook(is))return;
		
		event.renderHelmet = false;
		
		GL11.glPushMatrix();
		if(needsHeadHiding(is)){
			event.renderer.modelBipedMain.bipedHead.showModel = true;
			event.renderer.modelBipedMain.bipedHead.postRender(0.0625F);
			event.renderer.modelBipedMain.bipedHead.showModel = false;
		}else{
			event.renderer.modelBipedMain.bipedHead.postRender(0.0625F);
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		}
    
        float f1 = 0.625F;
        GL11.glTranslatef(0.0F, -0.25F, 0.0F);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(f1, -f1, -f1);

        CanvasRenderer.overrideUseRenderHelper = true;
        RenderManager.instance.itemRenderer.renderItem(event.entityPlayer, is, 0);
        CanvasRenderer.overrideUseRenderHelper = false;
        
        GL11.glPopMatrix();
	}
	
	@SideOnly(Side.CLIENT) @SubscribeEvent
	public void onDrawPlayerHead(RenderPlayerEvent.Pre event){
		ItemStack is = event.entityPlayer.getEquipmentInSlot(4);
		if(!needsHeadHiding(is))return;
		
		event.renderer.modelBipedMain.bipedHead.showModel = false;
		event.renderer.modelBipedMain.bipedHeadwear.showModel = false;
	}
	
	@SideOnly(Side.CLIENT) @SubscribeEvent
	public void onDrawPlayerHead(RenderPlayerEvent.Post event){
		ItemStack is = event.entityPlayer.getEquipmentInSlot(4);
		if(!needsHeadHiding(is))return;
		
		event.renderer.modelBipedMain.bipedHead.showModel = true;
		event.renderer.modelBipedMain.bipedHeadwear.showModel = true;
	}
	
	@SideOnly(Side.CLIENT)
	private boolean needsHelmetRenderHook(ItemStack is){
		if(is == null)return false;
		if(is.getItem() == ModMinePainter.droppedSculpture.item)return true;
		if(is.getItem() == ModMinePainter.canvas.item)return true;
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	private boolean needsHeadHiding(ItemStack is){
		if(is == null)return false;
		if(is.getItem() == ModMinePainter.droppedSculpture.item)return true;
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawBlockhightlight(DrawBlockHighlightEvent event){
		ItemStack is = event.player.getCurrentEquippedItem();
		if(is == null || !(is.getItem() instanceof ChiselItem))return;
		
		int x = event.target.blockX, y = event.target.blockY,z = event.target.blockZ;		
		Block sculpture = event.player.worldObj.getBlock(x,y,z);
		
		int[] pos = Operations.raytrace(x,y,z, event.player);
		if(pos[0] == -1)return;
		
		ChiselItem ci = Utils.getItem(is);
		int flags = ci.getChiselFlags(event.player);
		if(!Operations.validOperation(event.player.worldObj, x,y,z, pos, flags))return;
		
		Operations.setBlockBoundsFromRaytrace(pos, sculpture, flags);
		event.context.drawSelectionBox(event.player, event.target, 0, event.partialTicks);
		sculpture.setBlockBounds(0, 0, 0, 1, 1, 1);
		
//		event.setCanceled(true);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawPaintingPixel(DrawBlockHighlightEvent event){
		ItemStack is = event.player.getCurrentEquippedItem();
		if(is == null || !(is.getItem() instanceof PaintTool))return;
		
		int x = event.target.blockX, y = event.target.blockY,z = event.target.blockZ;
		World w = event.player.worldObj;
		if(w.getBlock(x, y, z) != ModMinePainter.painting.block)return;
		PaintingBlock painting = (PaintingBlock) w.getBlock(x,y,z);
		PaintingPlacement pp = PaintingPlacement.of(w.getBlockMetadata(x, y, z));
		
		Vec3 pos = event.player.getPosition(1.0f);
		Vec3 look = event.player.getLookVec();
		look = pos.addVector(look.xCoord * 5, look.yCoord * 5, look.zCoord * 5);
		
		MovingObjectPosition mop = painting.collisionRayTrace(w, x, y, z, pos, look);
		if(mop == null)return;
		float[] point = pp.block2painting( (float)(mop.hitVec.xCoord - mop.blockX), 
				(float)(mop.hitVec.yCoord-mop.blockY),
				(float)(mop.hitVec.zCoord-mop.blockZ));
		
		point[0] = (int)(point[0] * 16)/16f;
		point[1] = (int)(point[1] * 16)/16f;
		
		float[] bound1 = pp.painting2blockWithShift(point[0], point[1], 0.002f);
		float[] bound2 = pp.painting2blockWithShift(point[0]+1/16f, point[1]+1/16f, 0.002f);
		
		painting.setBlockBounds(Math.min(bound1[0], bound2[0]), 
								Math.min(bound1[1], bound2[1]),
								Math.min(bound1[2], bound2[2]),
								Math.max(bound1[0], bound2[0]),
								Math.max(bound1[1], bound2[1]),
								Math.max(bound1[2], bound2[2]));
		painting.ignore_bounds_on_state = true;
		event.context.drawSelectionBox(event.player, event.target, 0, event.partialTicks);
		painting.ignore_bounds_on_state = false;
		
	}
}
