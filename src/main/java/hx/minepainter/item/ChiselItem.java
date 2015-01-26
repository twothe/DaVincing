package hx.minepainter.item;

import hx.minepainter.ModMinePainter;
import hx.minepainter.sculpture.Operations;
import hx.minepainter.sculpture.SculptureOperationMessage;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class ChiselItem
        extends Item {

  public ChiselItem() {
    setCreativeTab(ModMinePainter.tabMinePainter);
    setUnlocalizedName("chisel");
    setTextureName("minepainter:stone_chisel");
    setMaxStackSize(1);
    setMaxDamage(240);
  }

  public boolean func_77648_a(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs) {
    if (!w.isRemote) {
      return false;
    }
    int[] pos = Operations.raytrace(x, y, z, ep);

    int flags = getChiselFlags(ep);
    Block editBlock = getEditBlock(is);
    int editMeta = getEditMeta(is);
    if (!Operations.validOperation(w, x, y, z, pos, flags)) {
      return false;
    }
    if (MinecraftServer.getServer()== null) {
      boolean done = Operations.applyOperation(w, x, y, z, pos, flags, editBlock, editMeta);
      if (!done) {
        return false;
      }
    }
    ModMinePainter.network.sendToServer(new SculptureOperationMessage(pos, x, y, z, editBlock, editMeta, flags));

    w.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, getEditBlock(is).stepSound.soundName, 0.5F, 0.5F);

    return true;
  }

  public Block getEditBlock(ItemStack is) {
    return Blocks.air;
  }

  public int getEditMeta(ItemStack is) {
    return 0;
  }

  public int getChiselFlags(EntityPlayer ep) {
    return 16;
  }

  public static class Saw
          extends ChiselItem {

    public Saw() {
      setUnlocalizedName("saw");
      setTextureName("minepainter:diamond_chisel");
    }

    @Override
    public int getChiselFlags(EntityPlayer ep) {
      int axis = Operations.getLookingAxis(ep);
      switch (axis) {
        case 0:
          return 28;
        case 1:
          return 26;
        case 2:
          return 22;
      }
      return 0;
    }
  }

  public static class Barcutter
          extends ChiselItem {

    public Barcutter() {
      setUnlocalizedName("barcutter");
      setTextureName("minepainter:iron_chisel");
    }

    @Override
    public int getChiselFlags(EntityPlayer ep) {
      int axis = Operations.getLookingAxis(ep);
      switch (axis) {
        case 0:
          return 18;
        case 1:
          return 20;
        case 2:
          return 24;
      }
      return 0;
    }
  }
}
