package hx.minepainter;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hx.minepainter.item.ChiselItem;
import hx.minepainter.painting.PaintTool;
import hx.minepainter.painting.PaintingBlock;
import hx.minepainter.painting.PaintingPlacement;
import hx.minepainter.sculpture.Operations;
import hx.minepainter.sculpture.SculptureRender;
import hx.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderWorldEvent;

public class EventHandler {

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onPreRenderWorld(RenderWorldEvent.Pre e) {
    SculptureRender.setCurrentChunkPos(e.renderer.posX, e.renderer.posY, e.renderer.posZ);
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onDrawBlockhightlight(DrawBlockHighlightEvent event) {
    ItemStack is = event.player.getHeldItem();
    if ((is == null) || (!(is.getItem() instanceof ChiselItem))) {
      return;
    }
    int x = event.target.blockX;
    int y = event.target.blockY;
    int z = event.target.blockZ;
    Block sculpture = event.player.worldObj.getBlock(x, y, z);

    int[] pos = Operations.raytrace(x, y, z, event.player);
    if (pos[0] == -1) {
      return;
    }
    ChiselItem ci = (ChiselItem) Utils.getItem(is);
    int flags = ci.getChiselFlags(event.player);
    if (!Operations.validOperation(event.player.worldObj, x, y, z, pos, flags)) {
      return;
    }
    Operations.setBlockBoundsFromRaytrace(pos, sculpture, flags);
    event.context.drawSelectionBox(event.player, event.target, 0, event.partialTicks);
    sculpture.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onDrawPaintingPixel(DrawBlockHighlightEvent event) {
    ItemStack is = event.player.getHeldItem();
    if ((is == null) || (!(is.getItem() instanceof PaintTool))) {
      return;
    }
    int x = event.target.blockX;
    int y = event.target.blockY;
    int z = event.target.blockZ;
    World w = event.player.worldObj;
    if (w.getBlock(x, y, z) != ModMinePainter.painting.block) {
      return;
    }
    PaintingBlock painting = (PaintingBlock) w.getBlock(x, y, z);
    PaintingPlacement pp = PaintingPlacement.of(w.getBlockMetadata(x, y, z));

    Vec3 pos = event.player.getPosition(1.0F);
    Vec3 look = event.player.getLook(z);
    look = pos.addVector(look.xCoord * 5.0D, look.yCoord * 5.0D, look.zCoord * 5.0D);

    MovingObjectPosition mop = painting.collisionRayTrace(w, x, y, z, pos, look);
    if (mop == null) {
      return;
    }
    float[] point = pp.block2painting((float) (mop.hitVec.xCoord - mop.blockX), (float) (mop.hitVec.yCoord - mop.blockY), (float) (mop.hitVec.zCoord - mop.blockZ));

    point[0] = ((int) (point[0] * 16.0F) / 16.0F);
    point[1] = ((int) (point[1] * 16.0F) / 16.0F);

    float[] bound1 = pp.painting2blockWithShift(point[0], point[1], 0.002F);
    float[] bound2 = pp.painting2blockWithShift(point[0] + 0.0625F, point[1] + 0.0625F, 0.002F);

    painting.setBlockBounds(Math.min(bound1[0], bound2[0]), Math.min(bound1[1], bound2[1]), Math.min(bound1[2], bound2[2]), Math.max(bound1[0], bound2[0]), Math.max(bound1[1], bound2[1]), Math.max(bound1[2], bound2[2]));

    painting.ignore_bounds_on_state = true;
    event.context.drawSelectionBox(event.player, event.target, 0, event.partialTicks);
    painting.ignore_bounds_on_state = false;
  }
}
