package org.atomnuke.control.service.config;

import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.util.config.io.ConfigurationManager;
import org.atomnuke.util.config.io.UpdateTag;

/**
 *
 * @author zinic
 */
public class ServerCfgUpdateManager implements ConfigurationManager<ServerConfiguration> {

   private final AtomicLongUpdateTag updateTag;
   private ServerConfiguration configuration;

   public ServerCfgUpdateManager(ServerConfiguration defaultConfiguration) {
      updateTag = new AtomicLongUpdateTag();

      this.configuration = defaultConfiguration;
   }

   @Override
   public synchronized void write(ServerConfiguration value) {
      // We've been updated
      updateTag.updated();

      // Update the configuration object reference
      configuration = value;
   }

   @Override
   public synchronized ServerConfiguration read() {
      return configuration;
   }

   @Override
   public UpdateTag readUpdateTag() {
      return updateTag;
   }

   @Override
   public void destroy() {
   }
}
