/*
 */
package two.davincing;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author Two
 */
public class DaVincingCreativeTab extends CreativeTabs {

  public DaVincingCreativeTab() {
    super(DaVincing.MOD_NAME);
  }

  @Override
  public Item getTabIconItem() {
    return ProxyBase.itemMixerbrush.getItem();
  }
}
