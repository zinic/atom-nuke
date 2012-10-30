package org.atomnuke.control.service.config;

import java.util.LinkedList;
import java.util.Queue;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.ConfigurationManager;
import org.atomnuke.util.config.io.UpdateTag;

/**
 *
 * @author zinic
 */
public class ServerCfgManager implements ConfigurationManager<ServerConfiguration> {

   private final Queue<ServerConfiguration> modificationSteps;
   private ServerConfiguration latestVersion;

   public ServerCfgManager(ServerConfiguration initialVersion) {
      this.latestVersion = initialVersion;

      modificationSteps = new LinkedList<ServerConfiguration>();
   }

   @Override
   public synchronized void write(ServerConfiguration value) throws ConfigurationException {
      modificationSteps.add(value);
   }

   @Override
   public synchronized ServerConfiguration read() throws ConfigurationException {
      return latestVersion;
   }

   @Override
   public UpdateTag readUpdateTag() throws ConfigurationException {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void destroy() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }
}
