package two.davincing.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;
import two.davincing.DaVincing;
import two.davincing.painting.PaintingEntity;
import two.davincing.painting.PaintingPlacement;

@SideOnly(Side.CLIENT)
public class PaintingRenderer extends TileEntitySpecialRenderer {

  @Override
  public void renderTileEntityAt(final TileEntity entity, double x, double y, double z, float partial) {
    if (entity instanceof PaintingEntity) {
      final PaintingEntity pe = (PaintingEntity) entity;
      Tessellator tes = Tessellator.instance;
      PaintingIcon icon = pe.getIcon();
      PaintingPlacement placement = PaintingPlacement.of(pe.getBlockMetadata());

      GL11.glPushMatrix();
      GL11.glBindTexture(GL11.GL_TEXTURE_2D, icon.sheet.glTexId);
      RenderHelper.disableStandardItemLighting();
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glTranslated(x, y, z);
//		GL11.glScalef(0.875f, 0.875f, 0.875f);

      //face
      tes.startDrawingQuads();
      addPoint(placement, 0, 0, icon);
      addPoint(placement, 0, 1, icon);
      addPoint(placement, 1, 1, icon);
      addPoint(placement, 1, 0, icon);
      tes.draw();

      GL11.glPopMatrix();
      GL11.glDisable(GL11.GL_BLEND);
    } else {
      DaVincing.log.error("[PaintingRenderer.renderTileEntityAt]: tried to render PaintingEntity, but found %s", (entity == null ? "null" : entity.getClass().getName()));
    }
  }

  private void addPoint(PaintingPlacement pp, int x, int y, IIcon icon) {
    float[] pos = pp.painting2blockWithShift(x, y, 0.003f);
    Tessellator.instance.addVertexWithUV(pos[0], pos[1], pos[2],
            x == 0 ? icon.getMinU() : icon.getMaxU(),
            y == 0 ? icon.getMinV() : icon.getMaxV());
  }
//
//  private void drawSides(Tessellator tes, PaintingPlacement placement) {
//    //sides
//
//    tes.startDrawingQuads();
//    addColoredPoint(placement, 0, 0, 0.0625f);
//    addColoredPoint(placement, 1, 0, 0.0625f);
//    addColoredPoint(placement, 1, 0, 0f);
//    addColoredPoint(placement, 0, 0, 0f);
//    tes.draw();
//
//    tes.startDrawingQuads();
//    addColoredPoint(placement, 1, 0, 0.0625f);
//    addColoredPoint(placement, 1, 1, 0.0625f);
//    addColoredPoint(placement, 1, 1, 0f);
//    addColoredPoint(placement, 1, 0, 0f);
//    tes.draw();
//
//    tes.startDrawingQuads();
//    addColoredPoint(placement, 1, 1, 0.0625f);
//    addColoredPoint(placement, 0, 1, 0.0625f);
//    addColoredPoint(placement, 0, 1, 0f);
//    addColoredPoint(placement, 1, 1, 0f);
//    tes.draw();
//
//    tes.startDrawingQuads();
//    addColoredPoint(placement, 0, 1, 0.0625f);
//    addColoredPoint(placement, 0, 0, 0.0625f);
//    addColoredPoint(placement, 0, 0, 0f);
//    addColoredPoint(placement, 0, 1, 0f);
//    tes.draw();
//  }
//
//  private void addColoredPoint(PaintingPlacement pp, int x, int y, float d) {
//    float[] pos = pp.painting2blockWithShift(x, y, 0);
//    Tessellator.instance.setColorOpaque_I(0xffffff);
//    Tessellator.instance.addVertex(pos[0], pos[1], pos[2]);
//  }
}
