package two.davincing.sculpture;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import two.davincing.DaVincing;
import two.davincing.ProxyBase;

@SideOnly(Side.CLIENT)
public class SculptureRender implements ISimpleBlockRenderingHandler {

  @Override
  public void renderInventoryBlock(Block block, int metadata, int modelId,
          RenderBlocks renderer) {
  }

  @Override
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
          Block block, int modelId, RenderBlocks renderer) {

    final TileEntity tileEntity = world.getTileEntity(x, y, z);
    if (tileEntity instanceof SculptureEntity) {
      final SculptureEntity se = (SculptureEntity) tileEntity;

      se.getRender().updateLight(block.getMixedBrightnessForBlock(world, x, y, z));
      se.getRender().updateAO(world, x, y, z);
    } else {
      DaVincing.log.error("[SculptureRender.renderWorldBlock]: expected SculptureEntity at %d, %d, %d, but got %s", x, y, z, (tileEntity == null ? "null" : tileEntity.getClass().getName()));
    }

    return false;
  }

  @Override
  public boolean shouldRender3DInInventory(int modelId) {
    return false;
  }

  @Override
  public int getRenderId() {
    return ProxyBase.blockSculpture.getRenderID();
  }

}
