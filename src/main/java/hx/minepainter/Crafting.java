package hx.minepainter;

import cpw.mods.fml.common.registry.GameRegistry;
import hx.minepainter.item.PieceItem;
import hx.minepainter.painting.PaintTool.Bucket;
import hx.utils.ItemLoader;
import hx.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class Crafting {

  public void registerRecipes() {
    GameRegistry.addRecipe(new ItemStack(ModMinePainter.minibrush.item), new Object[]{"X  ", " Y ", "  Z", Character.valueOf('X'), new ItemStack(Blocks.field_150325_L), Character.valueOf('Y'), new ItemStack(Items.field_151055_y), Character.valueOf('Z'), new ItemStack(Items.field_151100_aR, 1, 1)});

    GameRegistry.addRecipe(new ItemStack(ModMinePainter.mixerbrush.item), new Object[]{"XX ", "XY ", "  Z", Character.valueOf('X'), new ItemStack(Blocks.field_150325_L), Character.valueOf('Y'), new ItemStack(Items.field_151055_y), Character.valueOf('Z'), new ItemStack(Items.field_151100_aR, 1, 1)});

    GameRegistry.addRecipe(new ItemStack(ModMinePainter.canvas.item), new Object[]{"XXX", "XXX", Character.valueOf('X'), new ItemStack(Blocks.field_150325_L)});

    GameRegistry.addRecipe(new ItemStack(ModMinePainter.chisel.item), new Object[]{"X ", " Y", Character.valueOf('X'), new ItemStack(Blocks.field_150347_e), Character.valueOf('Y'), new ItemStack(Items.field_151055_y)});

    GameRegistry.addRecipe(new ItemStack(ModMinePainter.barcutter.item), new Object[]{"X ", " Y", Character.valueOf('X'), new ItemStack(Items.field_151042_j), Character.valueOf('Y'), new ItemStack(ModMinePainter.chisel.item)});

    GameRegistry.addRecipe(new ItemStack(ModMinePainter.saw.item), new Object[]{"X ", " Y", Character.valueOf('X'), new ItemStack(Items.field_151045_i), Character.valueOf('Y'), new ItemStack(ModMinePainter.barcutter.item)});

    GameRegistry.addRecipe(new ItemStack(ModMinePainter.palette.item), new Object[]{"X", "Y", Character.valueOf('X'), new ItemStack(Blocks.field_150344_f), Character.valueOf('Y'), new ItemStack(ModMinePainter.chisel.item)});

    GameRegistry.addRecipe(new ItemStack(ModMinePainter.eraser.item), new Object[]{"XX ", "YY ", "ZZ ", Character.valueOf('X'), new ItemStack(Items.field_151123_aH), Character.valueOf('Y'), new ItemStack(Items.field_151121_aF), Character.valueOf('Z'), new ItemStack(Items.field_151100_aR, 1, 4)});

    GameRegistry.addRecipe(new ItemStack(ModMinePainter.wrench.item), new Object[]{"XX ", "YX ", " X ", Character.valueOf('X'), new ItemStack(Items.field_151042_j), Character.valueOf('Y'), new ItemStack(Items.field_151100_aR, 1, 1)});

    GameRegistry.addRecipe(this.scrap);

    GameRegistry.addRecipe(this.fillBucket);
  }

  private IRecipe scrap = new IRecipe() {
    public boolean func_77569_a(InventoryCrafting ic, World w) {
      Block block = null;
      int meta = 0;
      int count = 0;

      int size = ic.getSizeInventory();
      for (int i = 0; i < size; i++) {
        ItemStack is = ic.getStackInSlot(i);
        if ((is != null)
                && ((is.getItem()instanceof PieceItem))) {
          PieceItem pi = (PieceItem) Utils.getItem(is);
          if (block == null) {
            block = pi.getEditBlock(is);
            meta = pi.getEditMeta(is);
          }
          if (block != pi.getEditBlock(is)) {
            return false;
          }
          if (meta != pi.getEditMeta(is)) {
            return false;
          }
          count += pi.getWorthPiece();
        }
      }
      if (count == 0) {
        return false;
      }
      if ((count % 512 == 0) && (count / 512 <= 64)) {
        return true;
      }
      if ((count % 64 == 0) && (count / 64 <= 64)) {
        return true;
      }
      if ((count % 8 == 0) && (count / 8 <= 64)) {
        return true;
      }
      if (count <= 64) {
        return true;
      }
      return false;
    }

    public ItemStack func_77572_b(InventoryCrafting ic) {
      Block block = null;
      int meta = 0;
      int count = 0;

      int size = ic.getSizeInventory();
      for (int i = 0; i < size; i++) {
        ItemStack is = ic.getStackInSlot(i);
        if ((is != null)
                && ((is.getItem() instanceof PieceItem))) {
          PieceItem pi = (PieceItem) Utils.getItem(is);
          if (block == null) {
            block = pi.getEditBlock(is);
            meta = pi.getEditMeta(is);
          }
          count += pi.getWorthPiece();
        }
      }
      if ((count % 512 == 0) && (count / 512 <= 64)) {
        return new ItemStack(block, count / 512, meta);
      }
      if ((count % 64 == 0) && (count / 64 <= 64)) {
        return new ItemStack(ModMinePainter.cover.item, count / 64, (Block.getIdFromBlock(block) << 4) + meta);
      }
      if ((count % 8 == 0) && (count / 8 <= 64)) {
        return new ItemStack(ModMinePainter.bar.item, count / 8, (Block.getIdFromBlock(block) << 4) + meta);
      }
      return new ItemStack(ModMinePainter.piece.item, count, (Block.getIdFromBlock(block) << 4) + meta);
    }

    public int func_77570_a() {
      return 0;
    }

    public ItemStack func_77571_b() {
      return null;
    }
  };
  private IRecipe fillBucket = new IRecipe() {
    public int func_77570_a() {
      return 0;
    }

    public ItemStack func_77571_b() {
      return null;
    }

    public boolean func_77569_a(InventoryCrafting ic, World w) {
      ItemStack bucket = null;
      ItemStack dye = null;

      int size = ic.getSizeInventory();
      for (int i = 0; i < size; i++) {
        ItemStack is = ic.getStackInSlot(i);
        if (is != null) {
          if (((is.getItem() instanceof PaintTool.Bucket)) || (is.getItem() == Items.field_151131_as)) {
            if (bucket != null) {
              return false;
            }
            bucket = is;
          } else if (((is.getItem() instanceof ItemDye)) || (is.getItem() == Items.field_151123_aH)) {
            if (dye != null) {
              return false;
            }
            dye = is;
          } else {
            return false;
          }
        }
      }
      return (bucket != null) && (dye != null);
    }

    public ItemStack func_77572_b(InventoryCrafting ic) {
      ItemStack bucket = null;
      ItemStack dye = null;

      int size = ic.getSizeInventory();
      for (int i = 0; i < size; i++) {
        ItemStack is = ic.getStackInSlot(i);
        if (is != null) {
          if (((is.getItem() instanceof PaintTool.Bucket)) || (is.getItem() == Items.field_151131_as)) {
            if (bucket != null) {
              return null;
            }
            bucket = is;
          } else if (((is.getItem() instanceof ItemDye)) || (is.getItem() == Items.field_151123_aH)) {
            if (dye != null) {
              return null;
            }
            dye = is;
          }
        }
      }
      ItemStack newbucket = new ItemStack(ModMinePainter.bucket.item);
      newbucket.func_77964_b(dye.getItem() == Items.field_151123_aH ? 16 : dye.getItemDamage());
      return newbucket;
    }
  };
}
