package org.atomnuke.util.config.io.marshall.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map.Entry;
import java.util.Properties;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.marshall.ConfigurationMarshaller;

/**
 *
 * @author zinic
 */
public class PropertiesConfigMarshaller implements ConfigurationMarshaller<Properties> {

   private final Properties defaults;

   public PropertiesConfigMarshaller() {
      defaults = new Properties();
   }

   public PropertiesConfigMarshaller(Properties defaults) {
      this.defaults = new Properties(defaults);
   }

   @Override
   public void marshall(Properties propertiesToWrite, OutputStream out) throws ConfigurationException {
      for (Entry<Object, Object> defaultEntry : defaults.entrySet()) {
         if (!propertiesToWrite.containsKey(defaultEntry.getKey())) {
            propertiesToWrite.put(defaultEntry.getKey(), defaultEntry.getValue());
         }
      }

      try {
         propertiesToWrite.store(out, "");
      } catch (IOException ioe) {
         throw new ConfigurationException(ioe);
      }
   }

   @Override
   public Properties unmarhsall(InputStream in) throws ConfigurationException {
      final Properties newProps = new Properties(defaults);

      try {
         newProps.load(in);
      } catch (IOException ioe) {
         throw new ConfigurationException(ioe);
      }

      return newProps;
   }
}
