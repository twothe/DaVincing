/*
 */
package two.davincing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import two.davincing.item.CanvasItem;
import two.davincing.item.ChiselItem;
import two.davincing.item.CopygunItem;
import two.davincing.item.ItemHandle;
import two.davincing.item.Palette;
import two.davincing.item.PieceItem;
import two.davincing.painting.PaintTool;
import two.davincing.painting.PaintingBlock;
import two.davincing.painting.PaintingEntity;
import two.davincing.sculpture.DroppedSculptureItem;
import two.davincing.sculpture.SculptureBlock;
import two.davincing.sculpture.SculptureEntity;
import two.davincing.utils.BlockLoader;

/**
 * This class will always be loaded, whether this is running as client or server.
 * All mod content that should/must be available for both sides goes here.
 *
 * @author Two
 */
public class ProxyBase {

  public static final BlockLoader<SculptureBlock> blockSculpture = new BlockLoader(new SculptureBlock(), SculptureEntity.class);
  public static final BlockLoader<PaintingBlock> blockPainting = new BlockLoader(new PaintingBlock(), PaintingEntity.class);

  public static ItemHandle itemHandle;
  public static ChiselItem itemChisel;
  public static ChiselItem itemBarcutter;
  public static ChiselItem itemSaw;
  public static PaintTool itemEraser;
  public static PaintTool itemBucket;
  public static PaintTool itemMixerbrush;
  public static PaintTool itemMinibrush;
  public static CopygunItem itemCopygun;
  public static Palette itemPalette;
  /* Items with special renderer */
  public static PieceItem itemBar;
  public static DroppedSculptureItem itemDroppedSculpture;
  public static PieceItem itemCover;
  public static CanvasItem itemCanvas;
  public static PieceItem itemPiece;
  /* Initialization list for content that needs post-initialization. */
  protected LinkedList<InitializableModContent> pendingInitialization = new LinkedList<InitializableModContent>();
  /* Global Config vars */
  public boolean doAmbientOcclusion; // whether or not to do ambient occlusion during rendering
  public final ArrayList<Block> blockBlacklist = new ArrayList<Block>();

  public ProxyBase() {
  }

  protected void loadGlobalConfigValues() {
    doAmbientOcclusion = DaVincing.config.getMiscBoolean("Render with Ambient Occlusion", false);
    final List<Block> defaultBlackList = Arrays.asList(new Block[]{
      Blocks.bedrock, Blocks.cactus, Blocks.glass, Blocks.grass, Blocks.leaves, Blocks.stained_glass
    });

    blockBlacklist.clear();
    blockBlacklist.addAll(DaVincing.config.getMiscBlocks("Chisel blacklist", defaultBlackList, "A list of blocks that cannot be chiseled. Note that all blocks with a tile entity or blocks that are not full blocks cannot be chiseled in general."));
  }

  protected void registerBlocks() {
    blockSculpture.load();
    blockPainting.load();
  }

  protected void registerItems() {
    itemHandle = new ItemHandle();
    pendingInitialization.add(itemHandle);
    itemChisel = new ChiselItem();
    pendingInitialization.add(itemChisel);
    itemBarcutter = new ChiselItem.Barcutter();
    pendingInitialization.add(itemBarcutter);
    itemSaw = new ChiselItem.Saw();
    pendingInitialization.add(itemSaw);
    itemCopygun = new CopygunItem();
    pendingInitialization.add(itemCopygun);
    itemMinibrush = new PaintTool.Mini();
    pendingInitialization.add(itemMinibrush);
    itemMixerbrush = new PaintTool.Mixer();
    pendingInitialization.add(itemMixerbrush);
    itemBucket = new PaintTool.Bucket();
    pendingInitialization.add(itemBucket);
    itemEraser = new PaintTool.Eraser();
    pendingInitialization.add(itemEraser);
    itemPalette = new Palette();
    pendingInitialization.add(itemPalette);

    itemCanvas = new CanvasItem();
    pendingInitialization.add(itemCanvas);
    itemPiece = new PieceItem();
    pendingInitialization.add(itemPiece);
    itemBar = new PieceItem.Bar();
    pendingInitialization.add(itemBar);
    itemCover = new PieceItem.Cover();
    pendingInitialization.add(itemCover);
    itemDroppedSculpture = new DroppedSculptureItem();
    pendingInitialization.add(itemDroppedSculpture);
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
