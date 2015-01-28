package hx.minepainter.sculpture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

@SideOnly(Side.CLIENT)
public class SculptureRenderBlocks extends RenderBlocks {

  private final double[] overrideBounds = new double[6];
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
        } else if ((theirs == Blocks.air) || (!theirs.isOpaqueCube())) {
          this.drawFace[i] = true;
        } else {
          this.drawFace[i] = false;
        }
      }
    }
  }

  @Override
  public void setRenderBoundsFromBlock(Block p_147775_1_) {
    super.setRenderBoundsFromBlock(p_147775_1_);
    render2override();
    renderFull();
  }

  private void override2render() {
    this.renderMinX = this.overrideBounds[0];
    this.renderMinY = this.overrideBounds[1];
    this.renderMinZ = this.overrideBounds[2];
    this.renderMaxX = this.overrideBounds[3];
    this.renderMaxY = this.overrideBounds[4];
    this.renderMaxZ = this.overrideBounds[5];
  }

  private void render2override() {
    this.overrideBounds[0] = this.renderMinX;
    this.overrideBounds[1] = this.renderMinY;
    this.overrideBounds[2] = this.renderMinZ;
    this.overrideBounds[3] = this.renderMaxX;
    this.overrideBounds[4] = this.renderMaxY;
    this.overrideBounds[5] = this.renderMaxZ;
  }

  private void renderFull() {
    this.renderMinX = 0.0D;
    this.renderMinY = 0.0D;
    this.renderMinZ = 0.0D;
    this.renderMaxX = 1.0D;
    this.renderMaxY = 1.0D;
    this.renderMaxZ = 1.0D;
  }

  @Override
  public void renderFaceYNeg(Block p_147768_1_, double p_147768_2_, double p_147768_4_, double p_147768_6_, IIcon p_147768_8_) {
    if (this.drawFace[0] == false) { // DOWN
      return;
    }
    override2render();
    super.renderFaceYNeg(p_147768_1_, p_147768_2_, p_147768_4_, p_147768_6_, p_147768_8_);
    renderFull();
  }

  @Override
  public void renderFaceYPos(Block p_147806_1_, double p_147806_2_, double p_147806_4_, double p_147806_6_, IIcon p_147806_8_) {
    if (this.drawFace[1] == false) { // UP
      return;
    }
    override2render();
    super.renderFaceYPos(p_147806_1_, p_147806_2_, p_147806_4_, p_147806_6_, p_147806_8_);
    renderFull();
  }

  @Override
  public void renderFaceZNeg(Block p_147761_1_, double p_147761_2_, double p_147761_4_, double p_147761_6_, IIcon p_147761_8_) {
    if (this.drawFace[2] == false) { // NORTH
      return;
    }
    override2render();
    super.renderFaceZNeg(p_147761_1_, p_147761_2_, p_147761_4_, p_147761_6_, p_147761_8_);
    renderFull();
  }

  @Override
  public void renderFaceZPos(Block p_147734_1_, double p_147734_2_, double p_147734_4_, double p_147734_6_, IIcon p_147734_8_) {
    if (this.drawFace[3] == false) { // SOUTH
      return;
    }
    override2render();
    super.renderFaceZPos(p_147734_1_, p_147734_2_, p_147734_4_, p_147734_6_, p_147734_8_);
    renderFull();
  }

  @Override
  public void renderFaceXNeg(Block p_147798_1_, double p_147798_2_, double p_147798_4_, double p_147798_6_, IIcon p_147798_8_) {
    if (this.drawFace[4] == false) { // WEST
      return;
    }
    override2render();
    super.renderFaceXNeg(p_147798_1_, p_147798_2_, p_147798_4_, p_147798_6_, p_147798_8_);
    renderFull();
  }

  @Override
  public void renderFaceXPos(Block p_147764_1_, double p_147764_2_, double p_147764_4_, double p_147764_6_, IIcon p_147764_8_) {
    if (this.drawFace[5] == false) { // EAST
      return;
    }
    override2render();
    super.renderFaceXPos(p_147764_1_, p_147764_2_, p_147764_4_, p_147764_6_, p_147764_8_);
    renderFull();
  }
}
