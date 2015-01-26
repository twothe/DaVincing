package hx.minepainter.sculpture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

@SideOnly(Side.CLIENT)
public class SculptureRenderBlocks
        extends RenderBlocks {

  private double[] overrideBounds = new double[6];
  public boolean[] drawFace = new boolean[6];

  public SculptureRenderBlocks() {
    for (int i = 0; i < 6; i++) {
      this.drawFace[i] = true;
    }
  }

  public void cull(Sculpture sculpture, int x, int y, int z) {
    Block ours = sculpture.getBlockAt(x, y, z, null);
    for (int i = 0; i < 6; i++) {
      ForgeDirection dir = ForgeDirection.getOrientation(i);
      int _x = x + dir.offsetX;
      int _y = y + dir.offsetY;
      int _z = z + dir.offsetZ;
      if (!Sculpture.contains(_x, _y, _z)) {
        this.drawFace[i] = true;
      } else {
        Block theirs = sculpture.getBlockAt(_x, _y, _z, null);
        if (theirs == ours) {
          this.drawFace[i] = false;
        } else if ((theirs == Blocks.air) || (!theirs.func_149662_c())) {
          this.drawFace[i] = true;
        } else {
          this.drawFace[i] = false;
        }
      }
    }
  }

  public void func_147775_a(Block p_147775_1_) {
    super.func_147775_a(p_147775_1_);
    render2override();
    renderFull();
  }

  private void override2render() {
    this.field_147859_h = this.overrideBounds[0];
    this.field_147855_j = this.overrideBounds[1];
    this.field_147851_l = this.overrideBounds[2];
    this.field_147861_i = this.overrideBounds[3];
    this.field_147857_k = this.overrideBounds[4];
    this.field_147853_m = this.overrideBounds[5];
  }

  private void render2override() {
    this.overrideBounds[0] = this.field_147859_h;
    this.overrideBounds[1] = this.field_147855_j;
    this.overrideBounds[2] = this.field_147851_l;
    this.overrideBounds[3] = this.field_147861_i;
    this.overrideBounds[4] = this.field_147857_k;
    this.overrideBounds[5] = this.field_147853_m;
  }

  private void renderFull() {
    this.field_147859_h = 0.0D;
    this.field_147855_j = 0.0D;
    this.field_147851_l = 0.0D;
    this.field_147861_i = 1.0D;
    this.field_147857_k = 1.0D;
    this.field_147853_m = 1.0D;
  }

  public void func_147768_a(Block p_147768_1_, double p_147768_2_, double p_147768_4_, double p_147768_6_, IIcon p_147768_8_) {
    if (this.drawFace[0] == 0) {
      return;
    }
    override2render();
    super.func_147768_a(p_147768_1_, p_147768_2_, p_147768_4_, p_147768_6_, p_147768_8_);
    renderFull();
  }

  public void func_147806_b(Block p_147806_1_, double p_147806_2_, double p_147806_4_, double p_147806_6_, IIcon p_147806_8_) {
    if (this.drawFace[1] == 0) {
      return;
    }
    override2render();
    super.func_147806_b(p_147806_1_, p_147806_2_, p_147806_4_, p_147806_6_, p_147806_8_);
    renderFull();
  }

  public void func_147761_c(Block p_147761_1_, double p_147761_2_, double p_147761_4_, double p_147761_6_, IIcon p_147761_8_) {
    if (this.drawFace[2] == 0) {
      return;
    }
    override2render();
    super.func_147761_c(p_147761_1_, p_147761_2_, p_147761_4_, p_147761_6_, p_147761_8_);
    renderFull();
  }

  public void func_147734_d(Block p_147734_1_, double p_147734_2_, double p_147734_4_, double p_147734_6_, IIcon p_147734_8_) {
    if (this.drawFace[3] == 0) {
      return;
    }
    override2render();
    super.func_147734_d(p_147734_1_, p_147734_2_, p_147734_4_, p_147734_6_, p_147734_8_);
    renderFull();
  }

  public void func_147798_e(Block p_147798_1_, double p_147798_2_, double p_147798_4_, double p_147798_6_, IIcon p_147798_8_) {
    if (this.drawFace[4] == 0) {
      return;
    }
    override2render();
    super.func_147798_e(p_147798_1_, p_147798_2_, p_147798_4_, p_147798_6_, p_147798_8_);
    renderFull();
  }

  public void func_147764_f(Block p_147764_1_, double p_147764_2_, double p_147764_4_, double p_147764_6_, IIcon p_147764_8_) {
    if (this.drawFace[5] == 0) {
      return;
    }
    override2render();
    super.func_147764_f(p_147764_1_, p_147764_2_, p_147764_4_, p_147764_6_, p_147764_8_);
    renderFull();
  }
}
