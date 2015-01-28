package two.davincing.utils;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class BlockLoader<T extends Block> {

  public final T block;
  public final Class<? extends TileEntity> tileEntityClass;

  @SideOnly(Side.CLIENT)
  public int renderID;

  public BlockLoader(T block, Class<? extends TileEntity> clazz) {
    this.block = block;
    this.tileEntityClass = clazz;
  }

  public void load() {
    GameRegistry.registerBlock(block, block.getClass().getSimpleName());
    if (tileEntityClass != null) {
      GameRegistry.registerTileEntity(tileEntityClass, tileEntityClass.getSimpleName());
    }
  }

  @SideOnly(Side.CLIENT)
  public void registerRendering(ISimpleBlockRenderingHandler blockRenderer, TileEntitySpecialRenderer tileRenderer) {
    if (blockRenderer != null) {
      renderID = RenderingRegistry.getNextAvailableRenderId();
      RenderingRegistry.registerBlockHandler(renderID, blockRenderer);
    }
    if (tileRenderer != null) {
      ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, tileRenderer);
    }
  }
}
