package two.davincing;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import two.davincing.item.PieceItem;
import two.davincing.painting.PaintTool;

public class Crafting {

  public static final Crafting instance = new Crafting();

  public void registerRecipes() {

    if (DaVincing.config.isCraftingEnabled("MiniBrush")) {
      GameRegistry.addRecipe(new ItemStack(ProxyBase.itemMinibrush),
              "X  ", " Y ", "  Z",
              'X', new ItemStack(Blocks.wool),
              'Y', new ItemStack(Items.stick),
              'Z', new ItemStack(Items.dye, 1, 1));
    }

    if (DaVincing.config.isCraftingEnabled("MixerBrush")) {
      GameRegistry.addRecipe(new ItemStack(ProxyBase.itemMixerbrush),
              "XX ", "XY ", "  Z",
              'X', new ItemStack(Blocks.wool),
              'Y', new ItemStack(Items.stick),
              'Z', new ItemStack(Items.dye, 1, 1));
    }

    if (DaVincing.config.isCraftingEnabled("Canvas")) {
      GameRegistry.addRecipe(new ItemStack(ProxyBase.itemCanvas),
              "XXX", "XXX",
              'X', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE));
    }

    if (DaVincing.config.isCraftingEnabled("Handle")) {
      GameRegistry.addShapelessRecipe(new ItemStack(ProxyBase.itemHandle),
              Items.leather, Items.leather, Items.stick);
    }

    if (DaVincing.config.isCraftingEnabled("Chisel")) {
      GameRegistry.addRecipe(new ItemStack(ProxyBase.itemChisel),
              "X ", " Y",
              'X', new ItemStack(Items.diamond),
              'Y', new ItemStack(ProxyBase.itemHandle));
    }

    if (DaVincing.config.isCraftingEnabled("Barcutter")) {
      GameRegistry.addRecipe(new ItemStack(ProxyBase.itemBarcutter),
              "XYX", " Z ", " Z ",
              'X', new ItemStack(ProxyBase.itemHandle),
              'Y', new ItemStack(Items.diamond),
              'Z', new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
    }

    if (DaVincing.config.isCraftingEnabled("Saw")) {
      GameRegistry.addRecipe(new ItemStack(ProxyBase.itemSaw),
              "XXY", "ZZ ",
              'X', new ItemStack(Items.stick),
              'Y', new ItemStack(ProxyBase.itemHandle),
              'Z', new ItemStack(Items.diamond));
      GameRegistry.addRecipe(new ItemStack(ProxyBase.itemSaw),
              "XZ", "XZ", "Y ",
              'X', new ItemStack(Items.stick),
              'Y', new ItemStack(ProxyBase.itemHandle),
              'Z', new ItemStack(Items.diamond));
    }

    if (DaVincing.config.isCraftingEnabled("Palette")) {
      GameRegistry.addShapelessRecipe(new ItemStack(ProxyBase.itemPalette),
              new ItemStack(Blocks.planks, OreDictionary.WILDCARD_VALUE),
              new ItemStack(ProxyBase.itemChisel));
    }
    if (DaVincing.config.isCraftingEnabled("Eraser")) {
      GameRegistry.addRecipe(new ItemStack(ProxyBase.itemEraser),
              "XX ", "YY ", "ZZ ",
              'X', new ItemStack(Items.slime_ball),
              'Y', new ItemStack(Items.paper),
              'Z', new ItemStack(Items.dye, 1, 4));
    }

    if (DaVincing.config.isCraftingEnabled("Copygun", false)) {
      GameRegistry.addRecipe(new ItemStack(ProxyBase.itemCopygun),
              "XXX", "YYX", " YX",
              'X', new ItemStack(Items.iron_ingot),
              'Y', new ItemStack(Items.gold_ingot));
    }
    
    RecipeSorter.register(fillBucket.getClass().getName(), fillBucket.getClass(), RecipeSorter.Category.SHAPELESS, "");
    GameRegistry.addRecipe(fillBucket);
  }

  static final IRecipe fillBucket = new IRecipe() {
    @Override
    public int getRecipeSize() {
      return 0;
    }

    @Override
    public ItemStack getRecipeOutput() {
      return null;
    }

    @Override
    public boolean matches(InventoryCrafting ic, World w) {
      ItemStack bucket = null;
      ItemStack dye = null;

      int size = ic.getSizeInventory();
      for (int i = 0; i < size; i++) {
        ItemStack is = ic.getStackInSlot(i);
        if (is == null) {
          continue;
        }
        if (is.getItem() instanceof PaintTool.Bucket || is.getItem() == Items.water_bucket) {
          if (bucket != null) {
            return false;
          }
          bucket = is;
          continue;
        }
        if (is.getItem() instanceof ItemDye || is.getItem() == Items.slime_ball) {
          if (dye != null) {
            return false;
          }
          dye = is;
          continue;
        }
        return false;
      }
      return bucket != null && dye != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting ic) {
      ItemStack bucket = null;
      ItemStack dye = null;

      int size = ic.getSizeInventory();
      for (int i = 0; i < size; i++) {
        ItemStack is = ic.getStackInSlot(i);
        if (is == null) {
          continue;
        }
        if (is.getItem() instanceof PaintTool.Bucket || is.getItem() == Items.water_bucket) {
          if (bucket != null) {
            return null;
          }
          bucket = is;
          continue;
        }
        if (is.getItem() instanceof ItemDye || is.getItem() == Items.slime_ball) {
          if (dye != null) {
            return null;
          }
          dye = is;
          continue;
        }
      }
      ItemStack newbucket = new ItemStack(ProxyBase.itemBucket);
      newbucket.setItemDamage(dye.getItem() == Items.slime_ball ? 16 : dye.getItemDamage());
      return newbucket;
    }
  };
}
