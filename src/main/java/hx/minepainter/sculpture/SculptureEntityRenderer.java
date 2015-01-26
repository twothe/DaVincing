package hx.minepainter.sculpture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class SculptureEntityRenderer
        extends TileEntitySpecialRenderer {

  @Override
  public void renderTileEntityAt(TileEntity var1, double xd, double yd, double zd, float partial) {
    SculptureEntity se = (SculptureEntity) var1;

    RenderHelper.disableStandardItemLighting();
    GL11.glShadeModel(GL11.GL_SMOOTH);
    GL11.glEnable(GL11.GL_BLEND);
    OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
    if ((!se.getRender().ready()) && (!se.getRender().hasContext())) {
      int lightX = (int) OpenGlHelper.lastBrightnessX;
      int lightY = (int) OpenGlHelper.lastBrightnessY;
      int light = lightY * 65536 + lightX;

      se.getRender().initFromSculptureAndLight(se.sculpture(), light);
    } else {
      se.updateRender();
    }
    GL11.glPushMatrix();
    GL11.glTranslated(xd, yd, zd);
    GL11.glCallList(se.getRender().glDisplayList);
    GL11.glPopMatrix();

    GL11.glDisable(GL11.GL_BLEND);
    GL11.glShadeModel(GL11.GL_FLAT);
    RenderHelper.enableStandardItemLighting();
  }
}
