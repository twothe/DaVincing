/*   1:    */ package hx.minepainter;
/*   2:    */ 
/*   3:    */ import cpw.mods.fml.common.Mod;
/*   4:    */ import cpw.mods.fml.common.Mod.EventHandler;
/*   5:    */ import cpw.mods.fml.common.event.FMLInitializationEvent;
/*   6:    */ import cpw.mods.fml.common.eventhandler.EventBus;
/*   7:    */ import cpw.mods.fml.common.network.NetworkRegistry;
/*   8:    */ import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
/*   9:    */ import cpw.mods.fml.relauncher.Side;
/*  10:    */ import cpw.mods.fml.relauncher.SideOnly;
/*  11:    */ import hx.minepainter.item.CanvasItem;
/*  12:    */ import hx.minepainter.item.CanvasRenderer;
/*  13:    */ import hx.minepainter.item.ChiselItem;
/*  14:    */ import hx.minepainter.item.ChiselItem.Barcutter;
/*  15:    */ import hx.minepainter.item.ChiselItem.Saw;
/*  16:    */ import hx.minepainter.item.DroppedSculptureItem;
/*  17:    */ import hx.minepainter.item.DroppedSculptureRenderer;
/*  18:    */ import hx.minepainter.item.Palette;
/*  19:    */ import hx.minepainter.item.PieceItem;
/*  20:    */ import hx.minepainter.item.PieceItem.Bar;
/*  21:    */ import hx.minepainter.item.PieceItem.Cover;
/*  22:    */ import hx.minepainter.item.PieceRenderer;
/*  23:    */ import hx.minepainter.item.PieceRenderer.Bar;
/*  24:    */ import hx.minepainter.item.PieceRenderer.Cover;
/*  25:    */ import hx.minepainter.item.WrenchItem;
/*  26:    */ import hx.minepainter.painting.PaintTool;
/*  27:    */ import hx.minepainter.painting.PaintTool.Bucket;
/*  28:    */ import hx.minepainter.painting.PaintTool.Eraser;
/*  29:    */ import hx.minepainter.painting.PaintTool.Mini;
/*  30:    */ import hx.minepainter.painting.PaintTool.Mixer;
/*  31:    */ import hx.minepainter.painting.PaintingBlock;
/*  32:    */ import hx.minepainter.painting.PaintingEntity;
/*  33:    */ import hx.minepainter.painting.PaintingOperationMessage;
/*  34:    */ import hx.minepainter.painting.PaintingOperationMessage.PaintingOperationHandler;
/*  35:    */ import hx.minepainter.painting.PaintingRenderer;
/*  36:    */ import hx.minepainter.sculpture.SculptureBlock;
/*  37:    */ import hx.minepainter.sculpture.SculptureEntity;
/*  38:    */ import hx.minepainter.sculpture.SculptureEntityRenderer;
/*  39:    */ import hx.minepainter.sculpture.SculptureOperationMessage;
/*  40:    */ import hx.minepainter.sculpture.SculptureOperationMessage.SculptureOperationHandler;
/*  41:    */ import hx.minepainter.sculpture.SculptureRender;
/*  42:    */ import hx.utils.BlockLoader;
/*  43:    */ import hx.utils.ItemLoader;
/*  44:    */ import net.minecraft.creativetab.CreativeTabs;
/*  45:    */ import net.minecraft.item.Item;
/*  46:    */ import net.minecraftforge.common.MinecraftForge;
/*  47:    */ 
/*  48:    */ @Mod(modid="minepainter", version="0.2.6")
/*  49:    */ public class ModMinePainter
/*  50:    */ {
/*  51: 47 */   public static CreativeTabs tabMinePainter = new CreativeTabs("minepainter")
/*  52:    */   {
/*  53:    */     public Item func_78016_d()
/*  54:    */     {
/*  55: 50 */       return ModMinePainter.mixerbrush.item;
/*  56:    */     }
/*  57:    */   };
/*  58: 55 */   public static BlockLoader<SculptureBlock> sculpture = new BlockLoader(new SculptureBlock(), SculptureEntity.class);
/*  59: 57 */   public static BlockLoader<PaintingBlock> painting = new BlockLoader(new PaintingBlock(), PaintingEntity.class);
/*  60: 60 */   public static ItemLoader<ChiselItem> chisel = new ItemLoader(new ChiselItem());
/*  61: 61 */   public static ItemLoader<ChiselItem> barcutter = new ItemLoader(new ChiselItem.Barcutter());
/*  62: 62 */   public static ItemLoader<ChiselItem> saw = new ItemLoader(new ChiselItem.Saw());
/*  63: 63 */   public static ItemLoader<PieceItem> piece = new ItemLoader(new PieceItem());
/*  64: 64 */   public static ItemLoader<PieceItem> bar = new ItemLoader(new PieceItem.Bar());
/*  65: 65 */   public static ItemLoader<PieceItem> cover = new ItemLoader(new PieceItem.Cover());
/*  66: 66 */   public static ItemLoader<DroppedSculptureItem> droppedSculpture = new ItemLoader(new DroppedSculptureItem());
/*  67: 67 */   public static ItemLoader<WrenchItem> wrench = new ItemLoader(new WrenchItem());
/*  68: 68 */   public static ItemLoader<PaintTool> minibrush = new ItemLoader(new PaintTool.Mini());
/*  69: 69 */   public static ItemLoader<PaintTool> mixerbrush = new ItemLoader(new PaintTool.Mixer());
/*  70: 70 */   public static ItemLoader<PaintTool> bucket = new ItemLoader(new PaintTool.Bucket());
/*  71: 71 */   public static ItemLoader<PaintTool> eraser = new ItemLoader(new PaintTool.Eraser());
/*  72: 72 */   public static ItemLoader<Palette> palette = new ItemLoader(new Palette());
/*  73: 73 */   public static ItemLoader<CanvasItem> canvas = new ItemLoader(new CanvasItem());
/*  74:    */   public static SimpleNetworkWrapper network;
/*  75:    */   
/*  76:    */   @Mod.EventHandler
/*  77:    */   public void preInit(FMLInitializationEvent e)
/*  78:    */   {
/*  79: 80 */     sculpture.load();
/*  80: 81 */     painting.load();
/*  81:    */     
/*  82: 83 */     chisel.load();
/*  83: 84 */     barcutter.load();
/*  84: 85 */     saw.load();
/*  85: 86 */     piece.load();
/*  86: 87 */     bar.load();
/*  87: 88 */     cover.load();
/*  88: 89 */     droppedSculpture.load();
/*  89: 90 */     wrench.load();
/*  90: 91 */     minibrush.load();
/*  91: 92 */     mixerbrush.load();
/*  92: 93 */     bucket.load();
/*  93: 94 */     eraser.load();
/*  94: 95 */     palette.load();
/*  95: 96 */     canvas.load();
/*  96:    */     
/*  97: 98 */     new Crafting().registerRecipes();
/*  98:    */     
/*  99:100 */     MinecraftForge.EVENT_BUS.register(new EventHandler());
/* 100:101 */     network = NetworkRegistry.INSTANCE.newSimpleChannel("minepainter");
/* 101:102 */     network.registerMessage(SculptureOperationMessage.SculptureOperationHandler.class, SculptureOperationMessage.class, 0, Side.SERVER);
/* 102:    */     
/* 103:104 */     network.registerMessage(PaintingOperationMessage.PaintingOperationHandler.class, PaintingOperationMessage.class, 1, Side.SERVER);
/* 104:    */   }
/* 105:    */   
/* 106:    */   @SideOnly(Side.CLIENT)
/* 107:    */   @Mod.EventHandler
/* 108:    */   public void preInitClient(FMLInitializationEvent e)
/* 109:    */   {
/* 110:112 */     sculpture.registerRendering(new SculptureRender(), new SculptureEntityRenderer());
/* 111:113 */     painting.registerRendering(null, new PaintingRenderer());
/* 112:    */     
/* 113:115 */     piece.registerRendering(new PieceRenderer());
/* 114:116 */     bar.registerRendering(new PieceRenderer.Bar());
/* 115:117 */     cover.registerRendering(new PieceRenderer.Cover());
/* 116:118 */     droppedSculpture.registerRendering(new DroppedSculptureRenderer());
/* 117:119 */     canvas.registerRendering(new CanvasRenderer());
/* 118:    */   }
/* 119:    */ }


/* Location:           C:\Work\Java\Projekte\mc-forge-1710-minepainter\minepainter-0.2.6.jar
 * Qualified Name:     hx.minepainter.ModMinePainter
 * JD-Core Version:    0.7.0.1
 */