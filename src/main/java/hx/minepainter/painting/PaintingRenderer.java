package hx.minepainter.painting;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

public class PaintingRenderer
        extends TileEntitySpecialRenderer {

  @Override
  public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float partial) {
    PaintingEntity pe = (PaintingEntity) entity;
    Tessellator tes = Tessellator.instance;
    PaintingIcon icon = pe.getIcon();
    PaintingPlacement placement = PaintingPlacement.of(pe.getBlockMetadata());

    GL11.glPushMatrix();
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, icon.sheet.glTexId);
    RenderHelper.disableStandardItemLighting();
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glTranslated(x, y, z);

    tes.startDrawingQuads();
    addPoint(placement, 0, 0, icon);
    addPoint(placement, 0, 1, icon);
    addPoint(placement, 1, 1, icon);
    addPoint(placement, 1, 0, icon);
    tes.draw();

    RenderHelper.enableStandardItemLighting();
    GL11.glPopMatrix();
    GL11.glDisable(GL11.GL_BLEND);
  }

  private void addPoint(PaintingPlacement pp, int x, int y, IIcon icon) {
    float[] pos = pp.painting2blockWithShift(x, y, 0.003F);
    Tessellator.instance.addVertexWithUV(pos[0], pos[1], pos[2], x == 0 ? icon.getMinU() : icon.getMaxU(), y == 0 ? icon.getMinV() : icon.getMaxV());
  }

  // Seems like obsolete code
//
//  private void drawSides(Tessellator tes, PaintingPlacement placement) {
//    tes.startDrawingQuads();
//    addColoredPoint(placement, 0, 0, 0.0625F);
//    addColoredPoint(placement, 1, 0, 0.0625F);
//    addColoredPoint(placement, 1, 0, 0.0F);
//    addColoredPoint(placement, 0, 0, 0.0F);
//    tes.draw();
//
//    tes.startDrawingQuads();
//    addColoredPoint(placement, 1, 0, 0.0625F);
//    addColoredPoint(placement, 1, 1, 0.0625F);
//    addColoredPoint(placement, 1, 1, 0.0F);
//    addColoredPoint(placement, 1, 0, 0.0F);
//    tes.draw();
//
//    tes.startDrawingQuads();
//    addColoredPoint(placement, 1, 1, 0.0625F);
//    addColoredPoint(placement, 0, 1, 0.0625F);
//    addColoredPoint(placement, 0, 1, 0.0F);
//    addColoredPoint(placement, 1, 1, 0.0F);
//    tes.draw();
//
//    tes.startDrawingQuads();
//    addColoredPoint(placement, 0, 1, 0.0625F);
//    addColoredPoint(placement, 0, 0, 0.0625F);
//    addColoredPoint(placement, 0, 0, 0.0F);
//    addColoredPoint(placement, 0, 1, 0.0F);
//    tes.draw();
//  }
//
//  private void addColoredPoint(PaintingPlacement pp, int x, int y, float d) {
//    float[] pos = pp.painting2blockWithShift(x, y, 0.0F);
//    Tessellator.instance.setColorOpaque_I(16777215);
//    Tessellator.instance.setNormal(pos[0], pos[1], pos[2]);
//  }
}
