package hx.minepainter.sculpture;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hx.minepainter.ModMinePainter;
import hx.utils.BlockLoader;
import java.lang.reflect.Field;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class SculptureRenderCompiler {

  public static RenderBlocks rb = new SculptureRenderBlocks();
  int glDisplayList = -1;
  int light;
  public boolean changed = true;
  boolean context = false;
  float[][][] neighborAO = new float[3][3][3];

  public void updateAO(IBlockAccess w, int x, int y, int z) {
    for (int i = 0; i < 27; i++) {
      int dx = i % 3;
      int dy = i / 3 % 3;
      int dz = i / 9 % 3;

      float ao = w.getBlock(x + dx - 1, y + dy - 1, z + dz - 1).func_149685_I();
      if (ao != this.neighborAO[dx][dy][dz]) {
        this.changed = true;
        this.neighborAO[dx][dy][dz] = ao;
      }
    }
    this.context = true;
  }

  public void updateLight(int light) {
    if (light != this.light) {
      this.changed = true;
    }
    this.light = light;
    this.context = true;
  }

  public boolean hasContext() {
    return this.context;
  }

  public boolean update(BlockSlice slice) {
    if ((this.glDisplayList != -1) && (!this.changed)) {
      return false;
    }
    if (this.glDisplayList < 0) {
      this.glDisplayList = GLAllocation.func_74526_a(1);
    }
    GL11.glPushMatrix();
    GL11.glNewList(this.glDisplayList, 4864);
    build(slice);
    GL11.glEndList();
    GL11.glPopMatrix();

    this.changed = false;
    return true;
  }

  public void build(BlockSlice slice) {
    rb.field_147845_a = slice;
    rb.field_147837_f = false;
    SculptureBlock sculpture = (SculptureBlock) ModMinePainter.sculpture.block;

    TextureManager tm = Minecraft.getMinecraft().getTextureManager();
    tm.bindTexture(TextureMap.locationItemsTexture);

    Tessellator tes = Tessellator.instance;
    double[] offs = getTesOffsets();
    tes.func_78373_b(0.0D, 0.0D, 0.0D);
    tes.startDrawingQuads();
    for (int i = 0; i < 512; i++) {
      int x = i >> 6 & 0x7;
      int y = i >> 3 & 0x7;
      int z = i >> 0 & 0x7;

      Block b = slice.getBlock(x, y, z);
      if (b != Blocks.field_150350_a) {
        int meta = slice.getBlockMetadata(x, y, z);
        sculpture.setCurrentBlock(b, meta);

        tes.func_78373_b(-x, -y, -z);

        sculpture.func_149676_a(x / 8.0F, y / 8.0F, z / 8.0F, (x + 1) / 8.0F, (y + 1) / 8.0F, (z + 1) / 8.0F);
        rb.func_147805_b(sculpture, x, y, z);
      }
    }
    ((SculptureBlock) ModMinePainter.sculpture.block).setCurrentBlock(null, 0);
    sculpture.func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    rb.field_147845_a = null;
    tes.draw();
    tes.func_78373_b(offs[0], offs[1], offs[2]);
  }

  public void clear() {
    if (this.glDisplayList >= 0) {
      GL11.glDeleteLists(this.glDisplayList, 1);
    }
  }

  public boolean ready() {
    return this.glDisplayList >= 0;
  }

  private double[] getTesOffsets() {
    double[] off = new double[3];

    int count = 0;
    int xoff = 0;
    Field[] fields = Tessellator.class.getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      if (fields[i].getType() == Double.TYPE) {
        count++;
        if (count == 3) {
          xoff = i - 2;
        }
      } else {
        count = 0;
      }
    }
    off[0] = ((Double) ObfuscationReflectionHelper.getPrivateValue(Tessellator.class, Tessellator.instance, xoff)).doubleValue();
    off[1] = ((Double) ObfuscationReflectionHelper.getPrivateValue(Tessellator.class, Tessellator.instance, xoff + 1)).doubleValue();
    off[2] = ((Double) ObfuscationReflectionHelper.getPrivateValue(Tessellator.class, Tessellator.instance, xoff + 2)).doubleValue();

    return off;
  }

  public void initFromSculptureAndLight(Sculpture sculpture, int light) {
    update(BlockSlice.of(sculpture, light));
  }
}
