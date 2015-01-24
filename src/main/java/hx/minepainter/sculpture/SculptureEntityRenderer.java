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

  public void func_147500_a(TileEntity var1, double xd, double yd, double zd, float partial) {
    SculptureEntity se = (SculptureEntity) var1;

    RenderHelper.func_74518_a();
    GL11.glShadeModel(7425);
    GL11.glEnable(3042);
    OpenGlHelper.func_148821_a(770, 771, 1, 0);
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

    GL11.glDisable(3042);
    GL11.glShadeModel(7424);
    RenderHelper.func_74519_b();
  }
}
