package org.atomnuke.config;

import org.atomnuke.config.model.ServerConfiguration;

/**
 *
 * @author zinic
 */
public interface ConfigurationWriter {

   void write(ServerConfiguration configuration) throws ConfigurationException;
}
