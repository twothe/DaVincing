/*
 */
package two.davincing;

import java.io.File;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

/**
 * @author Two
 */
public class Config {

  protected static final String CATEGORY_ALLOWED_RECIPES = "Allowed Recipes";
  protected static final String CATEGORY_VARIOUS_SETTINGS = "Settings";
  //--- Class ------------------------------------------------------------------
  protected Configuration configuration;

  protected Config() {
  }

  protected void initialize(final File configFile) {
    configuration = new Configuration(configFile);
  }

  protected void load() {
    configuration.load();
  }

  protected void save() {
    configuration.save();
  }

  public boolean isCraftingEnabled(final String key) {
    return isCraftingEnabled(key, true);
  }

  public boolean isCraftingEnabled(final String key, final boolean defaultValue) {
    final Property property = configuration.get(CATEGORY_ALLOWED_RECIPES, key, defaultValue);
    return property.getBoolean(defaultValue);
  }

  public int getMiscInteger(final String key, final int defaultValue) {
    final Property property = configuration.get(CATEGORY_VARIOUS_SETTINGS, key, defaultValue);
    return property.getInt(defaultValue);
  }

  public double getMiscDouble(final String key, final double defaultValue) {
    final Property property = configuration.get(CATEGORY_VARIOUS_SETTINGS, key, defaultValue);
    return property.getDouble(defaultValue);
  }

  public boolean getMiscBoolean(final String key, final boolean defaultValue) {
    final Property property = configuration.get(CATEGORY_VARIOUS_SETTINGS, key, defaultValue);
    return property.getBoolean(defaultValue);
  }

  public Configuration getConfiguration() {
    return configuration;
  }

}
