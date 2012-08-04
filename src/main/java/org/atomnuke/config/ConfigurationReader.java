package org.atomnuke.config;

/**
 *
 * @author zinic
 */
public interface ConfigurationReader {

   ConfigurationHandler readConfiguration() throws ConfigurationException;
}
