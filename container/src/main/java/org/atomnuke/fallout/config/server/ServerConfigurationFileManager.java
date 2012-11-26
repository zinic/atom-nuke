package org.atomnuke.fallout.config.server;

import java.io.File;
import javax.xml.bind.JAXBException;
import org.atomnuke.atombus.config.model.ServerConfiguration;
import org.atomnuke.util.config.io.file.FileConfigurationManager;

/**
 *
 * @author zinic
 */
public class ServerConfigurationFileManager extends FileConfigurationManager<ServerConfiguration> {

   public ServerConfigurationFileManager(File file) throws JAXBException {
      super(new ServerConfigurationMarhsaller(), file);
   }
}
