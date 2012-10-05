package org.atomnuke.util.config.io.file;

import java.io.File;
import org.atomnuke.util.config.io.ConfigurationManagerImpl;
import org.atomnuke.util.config.io.marshall.ConfigurationMarshaller;

/**
 *
 * @author zinic
 */
public class FileConfigurationManager<T> extends ConfigurationManagerImpl<T> {

   public FileConfigurationManager(ConfigurationMarshaller<T> marshaller, File file) {
      super(marshaller, new FileConfigurationWriter<T>(file), new FileConfigurationReader<T>(file));
   }
}
