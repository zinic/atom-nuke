package org.atomnuke.util.config.update.listener;

import org.atomnuke.util.config.ConfigurationException;

/**
 *
 * @author zinic
 */
public interface ConfigurationListener<T> {

   void updated(T configuration) throws ConfigurationException;
}
