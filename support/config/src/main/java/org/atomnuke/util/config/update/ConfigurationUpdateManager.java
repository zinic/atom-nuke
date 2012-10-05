package org.atomnuke.util.config.update;

import org.atomnuke.util.config.io.ConfigurationManager;

/**
 *
 * @author zinic
 */
public interface ConfigurationUpdateManager {

   void update();

   ConfigurationContext get(String name);

   <T> ConfigurationContext<T> register(String name, ConfigurationManager<T> configurationManager);

   boolean unregister(String name);
}
