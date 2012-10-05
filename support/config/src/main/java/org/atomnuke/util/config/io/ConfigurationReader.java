package org.atomnuke.util.config.io;

import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.marshall.ConfigurationMarshaller;

/**
 *
 * @author zinic
 */
public interface ConfigurationReader<T> {

   UpdateTag readUpdateTag() throws ConfigurationException;

   T read(ConfigurationMarshaller<T> marshaller) throws ConfigurationException;
}
