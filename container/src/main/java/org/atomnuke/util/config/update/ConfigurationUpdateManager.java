package org.atomnuke.util.config.update;

import org.atomnuke.util.config.io.ConfigurationManager;
import org.atomnuke.util.config.io.marshall.ConfigurationMarshaller;

/**
 *
 * @author zinic
 */
public interface ConfigurationUpdateManager<T> {

   ConfigurationContext<T> get(String name);

   ConfigurationContext<T> register(String name, ConfigurationManager<T> configurationManager);

   boolean unregister(String name);
}
