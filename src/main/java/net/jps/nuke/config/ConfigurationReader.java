package net.jps.nuke.config;

/**
 *
 * @author zinic
 */
public interface ConfigurationReader {

   ConfigurationHandler readConfiguration() throws ConfigurationException;
}
