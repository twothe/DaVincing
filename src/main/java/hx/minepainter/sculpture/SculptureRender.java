package hx.minepainter.sculpture;

import hx.minepainter.ModMinePainter;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SculptureRender implements ISimpleBlockRenderingHandler{
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {

		if(world.getBlock(x, y, z) != ModMinePainter.sculpture.block)return false;
		
		SculptureEntity se = (SculptureEntity) world.getTileEntity(x, y, z);
		
		se.getRender().updateLight(block.getMixedBrightnessForBlock(world, x, y, z));
		se.getRender().updateAO(world, x, y, z);
		
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return ModMinePainter.sculpture.renderID;
	}

}
