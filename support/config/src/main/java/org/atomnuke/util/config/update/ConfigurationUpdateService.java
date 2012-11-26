package org.atomnuke.util.config.update;

import org.atomnuke.util.config.io.ConfigurationManager;
import org.atomnuke.lifecycle.Reclaimable;

/**
 *
 * @author zinic
 */
public interface ConfigurationUpdateService extends Reclaimable {

   void update();

   ConfigurationContext get(String name);

   <T> ConfigurationContext<T> register(String name, ConfigurationManager<T> configurationManager);
}
