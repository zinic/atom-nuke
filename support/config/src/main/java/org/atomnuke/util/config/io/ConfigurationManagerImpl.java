package org.atomnuke.util.config.io;

import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.marshall.ConfigurationMarshaller;

/**
 *
 * @author zinic
 */
public class ConfigurationManagerImpl<T> implements ConfigurationManager<T> {

   private final ConfigurationMarshaller<T> marshaller;
   private final ConfigurationWriter<T> writer;
   private final ConfigurationReader<T> reader;

   public ConfigurationManagerImpl(ConfigurationMarshaller<T> marshaller, ConfigurationWriter<T> writer, ConfigurationReader<T> reader) {
      this.marshaller = marshaller;
      this.writer = writer;
      this.reader = reader;
   }

   @Override
   public void destroy() {
   }

   @Override
   public void write(T value) throws ConfigurationException {
      writer.write(value, marshaller);
   }

   @Override
   public T read() throws ConfigurationException {
      return reader.read(marshaller);
   }

   @Override
   public final ConfigurationWriter<T> writer() {
      return writer;
   }

   @Override
   public final ConfigurationReader<T> reader() {
      return reader;
   }
}
