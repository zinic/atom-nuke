package org.atomnuke.util.config.io.marshall;

import java.io.InputStream;
import java.io.OutputStream;
import org.atomnuke.util.config.ConfigurationException;

/**
 *
 * @author zinic
 */
public interface ConfigurationMarshaller<T> {

   void marshall(T obj, OutputStream out) throws ConfigurationException;

   T unmarhsall(InputStream in) throws ConfigurationException;
}
