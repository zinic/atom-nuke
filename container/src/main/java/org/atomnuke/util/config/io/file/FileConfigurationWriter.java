package org.atomnuke.util.config.io.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.ConfigurationWriter;
import org.atomnuke.util.config.io.marshall.ConfigurationMarshaller;

/**
 *
 * @author zinic
 */
public class FileConfigurationWriter<T> implements ConfigurationWriter<T> {

   private final File file;

   public FileConfigurationWriter(File file) {
      this.file = file;
   }

   @Override
   public void write(T configuration, ConfigurationMarshaller<T> marhsaller) throws ConfigurationException {
      try {
         marhsaller.unmarhsall(new FileInputStream(file));
      } catch (FileNotFoundException fnfe) {
         throw new ConfigurationException("File not found: " + file.getAbsolutePath(), fnfe);
      }
   }
}
