/*
 */
package two.davincing.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import two.davincing.DaVincing;
import two.davincing.InitializableModContent;

/**
 * @author Two
 */
public class ItemBase extends Item implements InitializableModContent {

  public ItemBase() {
    this.setCreativeTab(DaVincing.tabDaVincing);
  }

  public String getGameRegistryName() {
    return this.getClass().getSimpleName().replace("$", "_");
  }
  
  @Override
  public void initialize() {
    GameRegistry.registerItem(this, getGameRegistryName());
  }
}
