package hx.minepainter.item;

import hx.minepainter.sculpture.Operations;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PieceItem
        extends ChiselItem {

  public PieceItem() {
    setCreativeTab(null);
    setUnlocalizedName("sculpture_piece");
    setTextureName("");
    func_77627_a(true);
    setMaxStackSize(64);
    func_77656_e(0);
  }

  public void func_94581_a(IIconRegister r) {
  }

  @Override
  public Block getEditBlock(ItemStack is) {
    return Block.getBlockById(is.getItemDamage()>> 4 & 0xFFF);
  }

  @Override
  public int getEditMeta(ItemStack is) {
    return is.getItemDamage() & 0xF;
  }

  @Override
  public int getChiselFlags(EntityPlayer ep) {
    return 33;
  }

  public int getWorthPiece() {
    return 1;
  }

  public static class Bar
          extends PieceItem {

    @Override
    public int getChiselFlags(EntityPlayer ep) {
      int axis = Operations.getLookingAxis(ep);
      switch (axis) {
        case 0:
          return 35;
        case 1:
          return 37;
        case 2:
          return 41;
      }
      return 1;
    }

    @Override
    public int getWorthPiece() {
      return 8;
    }
  }

  public static class Cover
          extends PieceItem {

    @Override
    public int getChiselFlags(EntityPlayer ep) {
      int axis = Operations.getLookingAxis(ep);
      switch (axis) {
        case 0:
          return 45;
        case 1:
          return 43;
        case 2:
          return 39;
      }
      return 1;
    }

    @Override
    public int getWorthPiece() {
      return 64;
    }
  }
}
