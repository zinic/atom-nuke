package net.jps.nuke.config;

import net.jps.nuke.config.model.ServerConfiguration;

/**
 *
 * @author zinic
 */
public interface ConfigurationWriter {

   void write(ServerConfiguration configuration) throws ConfigurationException;
}
