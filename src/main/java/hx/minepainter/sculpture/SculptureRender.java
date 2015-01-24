package hx.minepainter.sculpture;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hx.minepainter.ModMinePainter;
import hx.utils.BlockLoader;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

@SideOnly(Side.CLIENT)
public class SculptureRender
        implements ISimpleBlockRenderingHandler {

  private static int chunk_x;
  private static int chunk_y;
  private static int chunk_z;

  public static void setCurrentChunkPos(int x, int y, int z) {
    chunk_x = x;
    chunk_y = y;
    chunk_z = z;
  }

  public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
  }

  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    if (world.getBlock(x, y, z) != ModMinePainter.sculpture.block) {
      return false;
    }
    SculptureEntity se = (SculptureEntity) world.func_147438_o(x, y, z);

    se.getRender().updateLight(block.func_149677_c(world, x, y, z));
    se.getRender().updateAO(world, x, y, z);

    return false;
  }

  public boolean shouldRender3DInInventory(int modelId) {
    return false;
  }

  public int getRenderId() {
    return ModMinePainter.sculpture.renderID;
  }
}
