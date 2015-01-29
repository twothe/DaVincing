package two.davincing;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import two.davincing.item.CanvasItem;
import two.davincing.item.CanvasRenderer;
import two.davincing.item.ChiselItem;
import two.davincing.item.CopygunItem;
import two.davincing.item.DroppedSculptureItem;
import two.davincing.item.DroppedSculptureRenderer;
import two.davincing.item.HingeItem;
import two.davincing.item.Palette;
import two.davincing.item.PieceItem;
import two.davincing.item.PieceRenderer;
import two.davincing.item.TransmuterItem;
import two.davincing.item.WrenchItem;
import two.davincing.painting.CommandImportPainting;
import two.davincing.painting.PaintTool;
import two.davincing.painting.PaintingBlock;
import two.davincing.painting.PaintingEntity;
import two.davincing.painting.PaintingOperationMessage;
import two.davincing.painting.PaintingRenderer;
import two.davincing.sculpture.SculptureBlock;
import two.davincing.sculpture.SculptureEntity;
import two.davincing.sculpture.SculptureEntityRenderer;
import two.davincing.sculpture.SculptureOperationMessage;
import two.davincing.sculpture.SculptureRender;
import two.davincing.sculpture.SculptureRenderCompiler;
import two.davincing.utils.BlockLoader;
import two.davincing.utils.ItemLoader;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;
import two.davincing.painting.PaintingCache;
import two.davincing.utils.PriorityThreadFactory;

//TODO [DEFER] add a sculpture motor block thing
//TODO [DEFER] add a sculpture printer block
@Mod(modid = "davincing", version = "1710.1.2")
public class DaVincing {

  public static final Logger log = LogManager.getLogger(DaVincing.class.getSimpleName(), new StringFormatterMessageFactory());
  public static ScheduledExecutorService backgroundTasks = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(), new PriorityThreadFactory(DaVincing.class.getSimpleName() + " Worker", Thread.MIN_PRIORITY, true));

  public static final CreativeTabs tabDaVincing = new CreativeTabs("davincing") {

    @Override
    public Item getTabIconItem() {
      return DaVincing.mixerbrush.getItem();
    }

  };

  public static final BlockLoader<SculptureBlock> sculpture = new BlockLoader(new SculptureBlock(), SculptureEntity.class);
  public static final BlockLoader<PaintingBlock> painting = new BlockLoader(new PaintingBlock(), PaintingEntity.class);

  public static final ItemLoader<Item> handle = new ItemLoader(new Item().setUnlocalizedName("handle").setTextureName("minepainter:handle"));
  public static final ItemLoader<ChiselItem> chisel = new ItemLoader(new ChiselItem());
  public static final ItemLoader<ChiselItem> barcutter = new ItemLoader(new ChiselItem.Barcutter());
  public static final ItemLoader<ChiselItem> saw = new ItemLoader(new ChiselItem.Saw());
  public static final ItemLoader<PieceItem> piece = new ItemLoader(new PieceItem());
  public static final ItemLoader<PieceItem> bar = new ItemLoader(new PieceItem.Bar());
  public static final ItemLoader<PieceItem> cover = new ItemLoader(new PieceItem.Cover());
  public static final ItemLoader<DroppedSculptureItem> droppedSculpture = new ItemLoader(new DroppedSculptureItem());
  public static final ItemLoader<WrenchItem> wrench = new ItemLoader(new WrenchItem());
  public static final ItemLoader<CopygunItem> copygun = new ItemLoader(new CopygunItem());
  public static final ItemLoader<HingeItem> hinge = new ItemLoader(new HingeItem());
  public static final ItemLoader<TransmuterItem> transmuter = new ItemLoader(new TransmuterItem());
  public static final ItemLoader<PaintTool> minibrush = new ItemLoader(new PaintTool.Mini());
  public static final ItemLoader<PaintTool> mixerbrush = new ItemLoader(new PaintTool.Mixer());
  public static final ItemLoader<PaintTool> bucket = new ItemLoader(new PaintTool.Bucket());
  public static final ItemLoader<PaintTool> eraser = new ItemLoader(new PaintTool.Eraser());
  public static final ItemLoader<Palette> palette = new ItemLoader(new Palette());
  public static final ItemLoader<CanvasItem> canvas = new ItemLoader(new CanvasItem());

  public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel("minepainter");
  static final DroppedSculptureRenderer droppedSculptureRenderer = new DroppedSculptureRenderer();

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    config.load();
    if (event.getSide().isClient()) {
      SculptureRenderCompiler.CULL = config.getBoolean("greedy_culling", "RENDER", true,
              "Greedily merge blocks for faster render. Sacrifices AO effect.", "config.greedy_culling");
    }
    Crafting.CRAFTABLE_COPYGUN = config.getBoolean("craftable_copygun", "GAMEPLAY", true, "make copygun craftable");
    config.save();
  }

  @EventHandler
  public void init(FMLInitializationEvent e) {
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

    MinecraftForge.EVENT_BUS.register(new two.davincing.EventHandler());
    network.registerMessage(SculptureOperationMessage.SculptureOperationHandler.class,
            SculptureOperationMessage.class, 0, Side.SERVER);
    network.registerMessage(PaintingOperationMessage.PaintingOperationHandler.class,
            PaintingOperationMessage.class, 1, Side.SERVER);
  }

  @EventHandler
  public void serverLoad(FMLServerStartingEvent event) {
    event.registerServerCommand(new CommandImportPainting());
  }

  @EventHandler
  public void serverStopped(FMLServerStoppedEvent event) {
    PaintingCache.clear();
    droppedSculptureRenderer.clear();
  }

  @SideOnly(Side.CLIENT)
  @EventHandler
  public void initClient(FMLInitializationEvent e) {
//		sculpture.registerRendering(new SculptureRender(), null);
    sculpture.registerRendering(new SculptureRender(), new SculptureEntityRenderer());
    painting.registerRendering(null, new PaintingRenderer());

    piece.registerRendering(new PieceRenderer());
    bar.registerRendering(new PieceRenderer.Bar());
    cover.registerRendering(new PieceRenderer.Cover());
    droppedSculpture.registerRendering(droppedSculptureRenderer);
    canvas.registerRendering(new CanvasRenderer());
  }
}
