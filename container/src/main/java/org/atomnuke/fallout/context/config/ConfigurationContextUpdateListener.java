package org.atomnuke.fallout.context.config;

import java.util.Collection;
import org.atomnuke.Nuke;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.fallout.config.server.ServerConfigurationHandler;
import org.atomnuke.container.packaging.PackageContext;
import org.atomnuke.fallout.context.FalloutContext;
import org.atomnuke.fallout.context.FalloutContextImpl;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.update.listener.ConfigurationListener;

/**
 *
 * @author zinic
 */
public class ConfigurationContextUpdateListener implements ConfigurationListener<ServerConfiguration> {

   private final Collection<PackageContext> packages;
   private final FalloutContext containerContext;

   public ConfigurationContextUpdateListener(ServiceManager services, Collection<PackageContext> packages, Nuke nukeReference) {
      this.packages = packages;

      containerContext = new FalloutContextImpl(nukeReference.atomTasker(), services);
   }

   @Override
   public synchronized void updated(ServerConfiguration configuration) throws ConfigurationException {
      new ConfigurationProcessor(containerContext, new ServerConfigurationHandler(configuration), packages).merge();
   }
}
