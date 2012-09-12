package org.atomnuke.util.config.io;

import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.marshall.ConfigurationMarshaller;

/**
 *
 * @author zinic
 */
public interface ConfigurationWriter<T> {

   void write(T configuration, ConfigurationMarshaller<T> marhsaller) throws ConfigurationException;
}
