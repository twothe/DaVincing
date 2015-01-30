/*
 */
package two.davincing;

import java.util.LinkedList;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import two.davincing.item.CanvasItem;
import two.davincing.item.ChiselItem;
import two.davincing.item.CopygunItem;
import two.davincing.item.DroppedSculptureItem;
import two.davincing.item.HingeItem;
import two.davincing.item.Palette;
import two.davincing.item.PieceItem;
import two.davincing.item.TransmuterItem;
import two.davincing.item.WrenchItem;
import two.davincing.painting.PaintTool;
import two.davincing.painting.PaintingBlock;
import two.davincing.painting.PaintingEntity;
import two.davincing.sculpture.SculptureBlock;
import two.davincing.sculpture.SculptureEntity;
import two.davincing.utils.BlockLoader;
import two.davincing.utils.ItemLoader;

/**
 * This class will always be loaded, whether this is running as client or server.
 * All mod content that should/must be available for both sides goes here.
 * 
 * @author Two
 */
public class ProxyBase {

  public static final BlockLoader<SculptureBlock> blockSculpture = new BlockLoader(new SculptureBlock(), SculptureEntity.class);
  public static final BlockLoader<PaintingBlock> blockPainting = new BlockLoader(new PaintingBlock(), PaintingEntity.class);

  public static final ItemLoader<PaintTool> itemEraser = new ItemLoader(new PaintTool.Eraser());
  public static final ItemLoader<ChiselItem> itemChisel = new ItemLoader(new ChiselItem());
  public static final ItemLoader<PaintTool> itemBucket = new ItemLoader(new PaintTool.Bucket());
  public static final ItemLoader<ChiselItem> itemBarcutter = new ItemLoader(new ChiselItem.Barcutter());
  public static final ItemLoader<Item> itemHandle = new ItemLoader(new Item().setUnlocalizedName("handle").setTextureName("minepainter:handle"));
  public static final ItemLoader<TransmuterItem> itemTransmuter = new ItemLoader(new TransmuterItem());
  public static final ItemLoader<CanvasItem> itemCanvas = new ItemLoader(new CanvasItem());
  public static final ItemLoader<PieceItem> itemPiece = new ItemLoader(new PieceItem());
  public static final ItemLoader<CopygunItem> itemCopygun = new ItemLoader(new CopygunItem());
  public static final ItemLoader<PieceItem> itemBar = new ItemLoader(new PieceItem.Bar());
  public static final ItemLoader<ChiselItem> itemSaw = new ItemLoader(new ChiselItem.Saw());
  public static final ItemLoader<PaintTool> itemMixerbrush = new ItemLoader(new PaintTool.Mixer());
  public static final ItemLoader<Palette> itemPalette = new ItemLoader(new Palette());
  public static final ItemLoader<PaintTool> itemMinibrush = new ItemLoader(new PaintTool.Mini());
  public static final ItemLoader<HingeItem> itemHinge = new ItemLoader(new HingeItem());
  public static final ItemLoader<DroppedSculptureItem> itemDroppedSculpture = new ItemLoader(new DroppedSculptureItem());
  public static final ItemLoader<WrenchItem> itemWrench = new ItemLoader(new WrenchItem());
  public static final ItemLoader<PieceItem> itemCover = new ItemLoader(new PieceItem.Cover());
  /* Initialization list for content that needs post-initialization. */
  protected LinkedList<InitializableModContent> pendingInitialization = new LinkedList<InitializableModContent>();
  /* Global Config vars */
  public boolean doAmbientOcclusion; // whether or not to do ambient occlusion during rendering

  public ProxyBase() {
  }

  protected void loadGlobalConfigValues() {
    doAmbientOcclusion = DaVincing.config.getMiscBoolean("Render with Ambient Occlusion", false);
  }

  protected void registerBlocks() {
    blockSculpture.load();
    blockPainting.load();
  }

  protected void registerItems() {
    itemHandle.load();
    itemChisel.load();
    itemBarcutter.load();
    itemSaw.load();
    itemPiece.load();
    itemBar.load();
    itemCover.load();
    itemDroppedSculpture.load();
    itemWrench.load();
    itemCopygun.load();
//		hinge.load();
    itemTransmuter.load();
    itemMinibrush.load();
    itemMixerbrush.load();
    itemBucket.load();
    itemEraser.load();
    itemPalette.load();
    itemCanvas.load();
  }

  protected void registerRenderers() {
  }

  public void onPreInit() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  public void onInit() {
    loadGlobalConfigValues();
    registerBlocks();
    registerItems();
    registerRenderers();

    InitializableModContent content;
    while ((content = pendingInitialization.poll()) != null) {
      content.initialize();
    }
  }

  public void onPostInit() {
  }
}
