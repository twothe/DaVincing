package hx.minepainter;

import hx.minepainter.item.PieceItem;
import hx.minepainter.painting.PaintTool;
import hx.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class Crafting {
	public static boolean CRAFTABLE_COPYGUN = false;

	public void registerRecipes(){
		
		GameRegistry.addRecipe(new ItemStack(ModMinePainter.minibrush.item),
                "X  ", " Y ", "  Z",
                'X', new ItemStack(Blocks.wool),
                'Y', new ItemStack(Items.stick),
                'Z', new ItemStack(Items.dye, 1, 1));
		
		GameRegistry.addRecipe(new ItemStack(ModMinePainter.mixerbrush.item),
                "XX ", "XY ", "  Z",
                'X', new ItemStack(Blocks.wool),
                'Y', new ItemStack(Items.stick),
                'Z', new ItemStack(Items.dye, 1, 1));
		
		GameRegistry.addRecipe(new ItemStack(ModMinePainter.canvas.item),
                "XXX", "XXX",
                'X', new ItemStack(Blocks.wool,1,OreDictionary.WILDCARD_VALUE));
		
		GameRegistry.addShapelessRecipe(new ItemStack(ModMinePainter.handle.item),
				Items.leather, Items.leather, Items.stick);
		
		GameRegistry.addRecipe(new ItemStack(ModMinePainter.chisel.item),
                "X ", " Y",
                'X', new ItemStack(Items.diamond),
                'Y', new ItemStack(ModMinePainter.handle.item));
		
		GameRegistry.addRecipe(new ItemStack(ModMinePainter.barcutter.item),
                "XYX", " Z "," Z ",
                'X', new ItemStack(ModMinePainter.handle.item),
                'Y', new ItemStack(Items.diamond),
                'Z', new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE));
		
		GameRegistry.addRecipe(new ItemStack(ModMinePainter.saw.item),
                "XXY", "ZZ ",
                'X', new ItemStack(Items.stick),
                'Y', new ItemStack(ModMinePainter.handle.item),
                'Z', new ItemStack(Items.diamond));
		GameRegistry.addRecipe(new ItemStack(ModMinePainter.saw.item),
                "XZ", "XZ","Y ",
                'X', new ItemStack(Items.stick),
                'Y', new ItemStack(ModMinePainter.handle.item),
                'Z', new ItemStack(Items.diamond));
		
		GameRegistry.addShapelessRecipe(new ItemStack(ModMinePainter.palette.item),
				new ItemStack(Blocks.planks,OreDictionary.WILDCARD_VALUE),
				new ItemStack(ModMinePainter.chisel.item));
		
		GameRegistry.addRecipe(new ItemStack(ModMinePainter.eraser.item),
				"XX ", "YY ", "ZZ ",
				'X', new ItemStack(Items.slime_ball),
				'Y', new ItemStack(Items.paper),
				'Z', new ItemStack(Items.dye, 1, 4));
		
		GameRegistry.addRecipe(new ItemStack(ModMinePainter.wrench.item),
				"XX ","YX ", " X ",
				'X', new ItemStack(Items.iron_ingot),
				'Y', new ItemStack(Items.dye, 1, 1));
		
		if(CRAFTABLE_COPYGUN)
		GameRegistry.addRecipe(new ItemStack(ModMinePainter.copygun.item),
				"XXX","YYX"," YX",
				'X', new ItemStack(Items.iron_ingot),
				'Y', new ItemStack(Items.gold_ingot));
		
		GameRegistry.addRecipe(scrap);
		
		GameRegistry.addRecipe(fillBucket);
	}
	
	private IRecipe scrap = new IRecipe(){

		@Override
		public boolean matches(InventoryCrafting ic, World w) {
			Block block = null;
			int meta = 0;
			int count = 0;
			
			int size = ic.getSizeInventory();
			for(int i = 0 ; i < size; i ++){
				ItemStack is = ic.getStackInSlot(i);
				if(is == null)continue;
				if(is.getItem() instanceof PieceItem){
					PieceItem pi = Utils.getItem(is);
					if(block == null){
						block = pi.getEditBlock(is);
						meta = pi.getEditMeta(is);
					}
					if(block != pi.getEditBlock(is))return false;
					if(meta != pi.getEditMeta(is))return false;
					
					count += pi.getWorthPiece();
				}
			}
			
			if(count == 0)return false;
			if(count % 512 == 0 && count/512 <= 64)return true;
			if(count % 64 == 0 && count/64 <= 64)return true;
			if(count % 8 == 0 && count/8 <= 64)return true;
			if(count <= 64)return true;
			
			return false;
		}

		@Override
		public ItemStack getCraftingResult(InventoryCrafting ic) {
			Block block = null;
			int meta = 0;
			int count = 0;
			
			int size = ic.getSizeInventory();
			for(int i = 0 ; i < size; i ++){
				ItemStack is = ic.getStackInSlot(i);
				if(is == null)continue;
				if(is.getItem() instanceof PieceItem){
					PieceItem pi = Utils.getItem(is);
					if(block == null){
						block = pi.getEditBlock(is);
						meta = pi.getEditMeta(is);
					}
					
					count += pi.getWorthPiece();
				}
			}
			
			if(count % 512 == 0 && count/512 <= 64){
				return new ItemStack(block,count/512,meta);
			}
			if(count % 64 == 0 && count/64 <= 64){
				return new ItemStack(ModMinePainter.cover.item, count/64, (Block.getIdFromBlock(block) << 4) + meta);
			}
			if(count % 8 == 0 && count/8 <= 64){
				return new ItemStack(ModMinePainter.bar.item, count/8, (Block.getIdFromBlock(block) << 4) + meta);
			}
			return new ItemStack(ModMinePainter.piece.item, count, (Block.getIdFromBlock(block) << 4) + meta);
		}

		@Override
		public int getRecipeSize() {
			return 0;
		}

		@Override
		public ItemStack getRecipeOutput() {
			return null;
		}
	};
	
	private IRecipe fillBucket = new IRecipe(){
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
			for(int i = 0 ; i < size; i ++){
				ItemStack is = ic.getStackInSlot(i);
				if(is == null)continue;
				if(is.getItem() instanceof PaintTool.Bucket || is.getItem() == Items.water_bucket){
					if(bucket != null)return false;
					bucket = is;
					continue;
				}
				if(is.getItem() instanceof ItemDye || is.getItem() == Items.slime_ball){
					if(dye != null)return false;
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
			for(int i = 0 ; i < size; i ++){
				ItemStack is = ic.getStackInSlot(i);
				if(is == null)continue;
				if(is.getItem() instanceof PaintTool.Bucket || is.getItem() == Items.water_bucket){
					if(bucket != null)return null;
					bucket = is;
					continue;
				}
				if(is.getItem() instanceof ItemDye || is.getItem() == Items.slime_ball){
					if(dye != null)return null;
					dye = is;
					continue;
				}
			}
			ItemStack newbucket = new ItemStack(ModMinePainter.bucket.item);
			newbucket.setItemDamage(dye.getItem() == Items.slime_ball ? 16 : dye.getItemDamage());
			return newbucket;
		}
	};
}
