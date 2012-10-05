package org.atomnuke.util.config.io.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.ConfigurationReader;
import org.atomnuke.util.config.io.UpdateTag;
import org.atomnuke.util.config.io.marshall.ConfigurationMarshaller;

/**
 *
 * @author zinic
 */
public class FileConfigurationReader<T> implements ConfigurationReader<T> {

   private final File file;

   public FileConfigurationReader(File file) {
      this.file = file;
   }

   @Override
   public UpdateTag readUpdateTag() throws ConfigurationException {
      return new FileUpdateTag(file);
   }

   @Override
   public T read(ConfigurationMarshaller<T> marshaller) throws ConfigurationException {
      try {
         return marshaller.unmarhsall(new FileInputStream(file));
      } catch (FileNotFoundException fnfe) {
         return null;
      }
   }
}
