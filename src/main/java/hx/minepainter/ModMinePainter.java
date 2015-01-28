package hx.minepainter;

import hx.minepainter.item.CopygunItem;
import hx.minepainter.item.DroppedSculptureItem;
import hx.minepainter.item.DroppedSculptureRenderer;
import hx.minepainter.item.CanvasItem;
import hx.minepainter.item.CanvasRenderer;
import hx.minepainter.item.ChiselItem;
import hx.minepainter.item.HingeItem;
import hx.minepainter.item.Palette;
import hx.minepainter.item.PieceItem;
import hx.minepainter.item.PieceItem.Bar;
import hx.minepainter.item.PieceRenderer;
import hx.minepainter.item.TransmuterItem;
import hx.minepainter.item.WrenchItem;
import hx.minepainter.painting.CommandImportPainting;
import hx.minepainter.painting.PaintTool;
import hx.minepainter.painting.PaintingBlock;
import hx.minepainter.painting.PaintingEntity;
import hx.minepainter.painting.PaintingOperationMessage;
import hx.minepainter.painting.PaintingRenderer;
import hx.minepainter.sculpture.SculptureBlock;
import hx.minepainter.sculpture.SculptureEntity;
import hx.minepainter.sculpture.SculptureEntityRenderer;
import hx.minepainter.sculpture.SculptureOperationMessage;
import hx.minepainter.sculpture.SculptureRender;
import hx.minepainter.sculpture.SculptureRenderCompiler;
import hx.minepainter.sculpture.SculptureRenderCuller;
import hx.utils.BlockLoader;
import hx.utils.Debug;
import hx.utils.ItemLoader;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

//TODO [DEFER] add a sculpture motor block thing
//TODO [DEFER] add a sculpture printer block
@Mod(modid = "minepainter", version = "0.3.0f1")
public class ModMinePainter {
	
	public static CreativeTabs tabMinePainter = new CreativeTabs("minepainter"){

		@Override public Item getTabIconItem() {
			return ModMinePainter.mixerbrush.item;
		}
		
	};
	
	public static BlockLoader<SculptureBlock> sculpture = 
			new BlockLoader(new SculptureBlock(), SculptureEntity.class);
	public static BlockLoader<PaintingBlock> painting = 
			new BlockLoader(new PaintingBlock(), PaintingEntity.class);
	
	public static ItemLoader<Item> handle = new ItemLoader(new Item().setUnlocalizedName("handle").setTextureName("minepainter:handle"));
	public static ItemLoader<ChiselItem> chisel = new ItemLoader(new ChiselItem());
	public static ItemLoader<ChiselItem> barcutter = new ItemLoader(new ChiselItem.Barcutter());
	public static ItemLoader<ChiselItem> saw = new ItemLoader(new ChiselItem.Saw());
	public static ItemLoader<PieceItem> piece = new ItemLoader(new PieceItem());
	public static ItemLoader<PieceItem> bar = new ItemLoader(new PieceItem.Bar());
	public static ItemLoader<PieceItem> cover = new ItemLoader(new PieceItem.Cover());
	public static ItemLoader<DroppedSculptureItem> droppedSculpture = new ItemLoader(new DroppedSculptureItem());
	public static ItemLoader<WrenchItem> wrench = new ItemLoader(new WrenchItem());
	public static ItemLoader<CopygunItem> copygun = new ItemLoader(new CopygunItem());
	public static ItemLoader<HingeItem> hinge = new ItemLoader(new HingeItem());
	public static ItemLoader<TransmuterItem> transmuter = new ItemLoader(new TransmuterItem());
	public static ItemLoader<PaintTool> minibrush = new ItemLoader(new PaintTool.Mini());
	public static ItemLoader<PaintTool> mixerbrush = new ItemLoader(new PaintTool.Mixer());
	public static ItemLoader<PaintTool> bucket = new ItemLoader(new PaintTool.Bucket());
	public static ItemLoader<PaintTool> eraser = new ItemLoader(new PaintTool.Eraser());
	public static ItemLoader<Palette> palette = new ItemLoader(new Palette());
	public static ItemLoader<CanvasItem> canvas = new ItemLoader(new CanvasItem());
	
	
	public static SimpleNetworkWrapper network;
	
	@EventHandler public void preInit(FMLPreInitializationEvent event){
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		if(event.getSide().isClient())
			SculptureRenderCompiler.CULL = config.getBoolean("greedy_culling", "RENDER", true, 
					"Greedily merge blocks for faster render. Sacrifices AO effect.", "config.greedy_culling");
		Crafting.CRAFTABLE_COPYGUN = config.getBoolean("craftable_copygun", "GAMEPLAY", true, "make copygun craftable");
		config.save();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e){
		sculpture.load();
		painting.load();
		
		handle.load();
		chisel.load();
		barcutter.load();
		saw.load();
		piece.load();
		bar.load();
		cover.load();
		droppedSculpture.load();
		wrench.load();
		copygun.load();
//		hinge.load();
		transmuter.load();
		minibrush.load();
		mixerbrush.load();
		bucket.load();
		eraser.load();
		palette.load();
		canvas.load();
		
		new Crafting().registerRecipes();
		
		MinecraftForge.EVENT_BUS.register(new hx.minepainter.EventHandler());
		network = NetworkRegistry.INSTANCE.newSimpleChannel("minepainter");
		network.registerMessage(SculptureOperationMessage.SculptureOperationHandler.class, 
				SculptureOperationMessage.class, 0, Side.SERVER);
		network.registerMessage(PaintingOperationMessage.PaintingOperationHandler.class, 
				PaintingOperationMessage.class, 1, Side.SERVER);
	}
	
	 @EventHandler
	  public void serverLoad(FMLServerStartingEvent event)
	  {
	    event.registerServerCommand(new CommandImportPainting());
	  }
	
	@SideOnly(Side.CLIENT)
	@EventHandler
	public void initClient(FMLInitializationEvent e){
//		sculpture.registerRendering(new SculptureRender(), null);
		sculpture.registerRendering(new SculptureRender(), new SculptureEntityRenderer());
		painting.registerRendering(null, new PaintingRenderer());
		
		piece.registerRendering(new PieceRenderer());
		bar.registerRendering(new PieceRenderer.Bar());
		cover.registerRendering(new PieceRenderer.Cover());
		droppedSculpture.registerRendering(new DroppedSculptureRenderer());
		canvas.registerRendering(new CanvasRenderer());
	}
}
