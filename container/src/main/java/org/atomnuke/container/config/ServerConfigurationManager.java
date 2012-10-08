package org.atomnuke.container.config;

import java.io.File;
import javax.xml.bind.JAXBException;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.util.config.io.file.FileConfigurationManager;

/**
 *
 * @author zinic
 */
public class ServerConfigurationManager extends FileConfigurationManager<ServerConfiguration> {

   public ServerConfigurationManager(File file) throws JAXBException {
      super(new ServerConfigurationMarhsaller(), file);
   }
}
