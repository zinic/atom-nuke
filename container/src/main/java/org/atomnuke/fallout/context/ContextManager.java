package org.atomnuke.fallout.context;

import java.util.Collection;
import org.atomnuke.Nuke;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.fallout.config.server.ServerConfigurationHandler;
import org.atomnuke.fallout.config.ConfigurationProcessor;
import org.atomnuke.container.packaging.PackageContext;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.update.listener.ConfigurationListener;

/**
 *
 * @author zinic
 */
public class ContextManager implements ConfigurationListener<ServerConfiguration> {

   private final ContainerContext containerContext;
   private final Collection<PackageContext> packages;
   private final ServiceManager services;
   private final Nuke nukeReference;

   public ContextManager(ServiceManager services, Collection<PackageContext> packages, Nuke nukeReference) {
      this.services = services;
      this.packages = packages;
      this.nukeReference = nukeReference;

      containerContext = new ContainerContext();
   }

   @Override
   public void updated(ServerConfiguration configuration) throws ConfigurationException {
      new ConfigurationProcessor(nukeReference.atomTasker(), services, containerContext, new ServerConfigurationHandler(configuration), packages).merge(nukeReference);
   }
}
