package hx.utils;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class ItemLoader<T extends Item> {

  public T item;

  public ItemLoader(T item) {
    this.item = item;
  }

  public void load(String name) {
    GameRegistry.registerItem(item, name);
  }

  public void load() {
    load(item.getClass().getSimpleName().replace("$", "_"));
  }

  @SideOnly(Side.CLIENT)
  public void registerRendering(IItemRenderer renderer) {
    MinecraftForgeClient.registerItemRenderer(item, renderer);
  }
}
