package two.davincing;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import net.minecraft.creativetab.CreativeTabs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;
import two.davincing.network.PaintingOperationHandler;
import two.davincing.network.PaintingOperationMessage;
import two.davincing.painting.CommandImportPainting;
import two.davincing.renderer.PaintingCache;
import two.davincing.sculpture.SculptureOperationMessage;
import two.davincing.utils.PriorityThreadFactory;

@Mod(modid = DaVincing.MOD_ID, name = DaVincing.MOD_NAME, version = DaVincing.MOD_VERSION)
public class DaVincing {

  /* Global logger that uses string format type logging */
  public static final Logger log = LogManager.getLogger(DaVincing.class.getSimpleName(), new StringFormatterMessageFactory());
  /* Task scheduler for background task. Will run at lowest thread priority to not interfere with FPS/ticks */
  public static final ScheduledExecutorService backgroundTasks = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(), new PriorityThreadFactory(DaVincing.class.getSimpleName() + " Worker", Thread.MIN_PRIORITY, true));

  public static final String MOD_NAME = "DaVincing";
  public static final String MOD_ID = "davincing";
  public static final String MOD_VERSION = "1710.1.3"; // Make sure to keep this version in sync with the build.gradle file!

  @Mod.Instance(MOD_ID)
  public static DaVincing instance; // this will point to the actual instance of this class once running

  @SidedProxy(clientSide = "two.davincing.ProxyClient", serverSide = "two.davincing.ProxyServer")
  public static ProxyBase proxy; // will load the appropriate proxy class depending whether this is running on a client or on a server

  public static final Config config = new Config();
  public static final CreativeTabs tabDaVincing = new DaVincingCreativeTab();
  public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel("minepainter");

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    config.initialize(event.getSuggestedConfigurationFile());
    proxy.onPreInit();
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent e) {
    config.load();
    proxy.onInit();

    Crafting.instance.registerRecipes();

    network.registerMessage(SculptureOperationMessage.SculptureOperationHandler.class, SculptureOperationMessage.class, 0, Side.SERVER);
    network.registerMessage(PaintingOperationHandler.class, PaintingOperationMessage.class, 1, Side.SERVER);
    config.save();
  }

  @Mod.EventHandler
  public void postInit(final FMLPostInitializationEvent event) {
    proxy.onPostInit();
  }

  @Mod.EventHandler
  public void serverLoad(final FMLServerStartingEvent event) {
    event.registerServerCommand(new CommandImportPainting());
  }

  @Mod.EventHandler
  public void serverStopped(final FMLServerStoppedEvent event) {
    PaintingCache.clear();
  }
}
