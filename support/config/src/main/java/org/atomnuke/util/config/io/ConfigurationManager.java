package org.atomnuke.util.config.io;

import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.lifecycle.Reclaimable;

/**
 *
 * @author zinic
 */
public interface ConfigurationManager<T> extends Reclaimable {

   void write(T value) throws ConfigurationException;

   T read() throws ConfigurationException;

   UpdateTag readUpdateTag() throws ConfigurationException;
}
