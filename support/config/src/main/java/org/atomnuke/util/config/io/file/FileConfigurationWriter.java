package org.atomnuke.util.config.io.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
   public void write(T configuration, ConfigurationMarshaller<T> marshaller) throws ConfigurationException {
      try {
         final OutputStream out = new FileOutputStream(file);
         marshaller.marshall(configuration, out);

         out.close();
      } catch (FileNotFoundException fnfe) {
         throw new ConfigurationException("File not found: " + file.getAbsolutePath(), fnfe);
      } catch (IOException ioe) {
         throw new ConfigurationException("Unable to write configuration. Reason: " + ioe.getMessage(), ioe);
      }
   }
}
