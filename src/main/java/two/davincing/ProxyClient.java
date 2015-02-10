/*
 */
package two.davincing;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import org.lwjgl.opengl.GL11;
import two.davincing.item.ChiselItem;
import two.davincing.painting.PaintTool;
import two.davincing.painting.PaintingBlock;
import two.davincing.painting.PaintingPlacement;
import two.davincing.renderer.CanvasRenderer;
import two.davincing.renderer.DroppedSculptureRenderer;
import two.davincing.renderer.PaintingRenderer;
import two.davincing.renderer.PieceRenderer;
import two.davincing.sculpture.Operations;
import two.davincing.sculpture.SculptureEntityRenderer;
import two.davincing.sculpture.SculptureRender;

/**
 * This class will only be loaded if this is running in client mode.
 * All client-only initialization (rendering, sound, ...) goes here.
 *
 * @author Two
 */
public class ProxyClient extends ProxyBase {

  @Override
  protected void registerRenderers() {
    ProxyBase.blockSculpture.registerRendering(new SculptureRender(), new SculptureEntityRenderer());
    ProxyBase.blockPainting.registerRendering(null, new PaintingRenderer());

    MinecraftForgeClient.registerItemRenderer(ProxyBase.itemPiece, new PieceRenderer());
    MinecraftForgeClient.registerItemRenderer(ProxyBase.itemBar, new PieceRenderer.Bar());
    MinecraftForgeClient.registerItemRenderer(ProxyBase.itemCover, new PieceRenderer.Cover());
    MinecraftForgeClient.registerItemRenderer(ProxyBase.itemDroppedSculpture, new DroppedSculptureRenderer());
    MinecraftForgeClient.registerItemRenderer(ProxyBase.itemCanvas, new CanvasRenderer());
  }

  @SubscribeEvent
  public void onClientRender(RenderWorldEvent.Pre event) {
    Runnable r;
    while ((r = DaVincing.glTasks.poll()) != null) {
      try {
        r.run();
      } catch (Throwable t) {
        DaVincing.log.error("[ProxyClient.onClientRender] failed", t);
      }
    }
  }

  @SubscribeEvent
  public void onDrawPlayerHelmet(RenderPlayerEvent.Specials.Pre event) {
    if (!event.renderHelmet) {
      return;
    }

    ItemStack is = event.entityPlayer.getEquipmentInSlot(4);
    if (!needsHelmetRenderHook(is)) {
      return;
    }

    event.renderHelmet = false;

    GL11.glPushMatrix();
    if (needsHeadHiding(is)) {
      event.renderer.modelBipedMain.bipedHead.showModel = true;
      event.renderer.modelBipedMain.bipedHead.postRender(0.0625F);
      event.renderer.modelBipedMain.bipedHead.showModel = false;
    } else {
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

  @SubscribeEvent
  public void onDrawPlayerHead(RenderPlayerEvent.Pre event) {
    ItemStack is = event.entityPlayer.getEquipmentInSlot(4);
    if (!needsHeadHiding(is)) {
      return;
    }

    event.renderer.modelBipedMain.bipedHead.showModel = false;
    event.renderer.modelBipedMain.bipedHeadwear.showModel = false;
  }

  @SubscribeEvent
  public void onDrawPlayerHead(RenderPlayerEvent.Post event) {
    ItemStack is = event.entityPlayer.getEquipmentInSlot(4);
    if (!needsHeadHiding(is)) {
      return;
    }

    event.renderer.modelBipedMain.bipedHead.showModel = true;
    event.renderer.modelBipedMain.bipedHeadwear.showModel = true;
  }

  private boolean needsHelmetRenderHook(ItemStack is) {
    return ((is != null)
            && ((is.getItem() == ProxyBase.itemDroppedSculpture) || (is.getItem() == ProxyBase.itemCanvas)));
  }

  private boolean needsHeadHiding(final ItemStack is) {
    return (is != null) && (is.getItem() == ProxyBase.itemDroppedSculpture);
  }

  @SubscribeEvent
  public void onDrawBlockhightlight(DrawBlockHighlightEvent event) {
    final ItemStack is = event.player.getCurrentEquippedItem();
    if (is == null) {
      return;
    }
    final Item item = is.getItem();
    if ((item == null) || !(is.getItem() instanceof ChiselItem)) {
      return;
    }
    final ChiselItem ci = (ChiselItem) item;

    final int x = event.target.blockX;
    final int y = event.target.blockY;
    final int z = event.target.blockZ;
    final Block sculpture = event.player.worldObj.getBlock(x, y, z);

    final int[] pos = Operations.raytrace(x, y, z, event.player);
    if (pos[0] == -1) {
      return;
    }

    int flags = ci.getChiselFlags(event.player);
    if (!Operations.validOperation(event.player.worldObj, x, y, z, pos, flags)) {
      return;
    }

    Operations.setBlockBoundsFromRaytrace(pos, sculpture, flags);
    event.context.drawSelectionBox(event.player, event.target, 0, event.partialTicks);
    sculpture.setBlockBounds(0, 0, 0, 1, 1, 1);

//		event.setCanceled(true);
  }

  @SubscribeEvent
  public void onDrawPaintingPixel(DrawBlockHighlightEvent event) {
    ItemStack is = event.player.getCurrentEquippedItem();
    if (is == null || !(is.getItem() instanceof PaintTool)) {
      return;
    }

    final int x = event.target.blockX;
    final int y = event.target.blockY;
    final int z = event.target.blockZ;
    final World world = event.player.worldObj;
    final Block block = world.getBlock(x, y, z);

    if (block instanceof PaintingBlock) {
      final PaintingBlock painting = (PaintingBlock) block;
      PaintingPlacement pp = PaintingPlacement.of(world.getBlockMetadata(x, y, z));

      Vec3 pos = event.player.getPosition(1.0f);
      Vec3 look = event.player.getLookVec();
      look = pos.addVector(look.xCoord * 5, look.yCoord * 5, look.zCoord * 5);

      MovingObjectPosition mop = painting.collisionRayTrace(world, x, y, z, pos, look);
      if (mop == null) {
        return;
      }
      float[] point = pp.block2painting((float) (mop.hitVec.xCoord - mop.blockX),
              (float) (mop.hitVec.yCoord - mop.blockY),
              (float) (mop.hitVec.zCoord - mop.blockZ));

      point[0] = (int) (point[0] * 16) / 16f;
      point[1] = (int) (point[1] * 16) / 16f;

      float[] bound1 = pp.painting2block(point[0], point[1], 0.002f);
      float[] bound2 = pp.painting2block(point[0] + 1 / 16f, point[1] + 1 / 16f, 0.002f);

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
}
