package org.atomnuke.util.config.io;

import org.atomnuke.util.config.ConfigurationException;

/**
 *
 * @author zinic
 */
public interface ConfigurationManager<T> {

   void write(T value) throws ConfigurationException;

   T read() throws ConfigurationException;

   ConfigurationWriter<T> writer();

   ConfigurationReader<T> reader();
}
