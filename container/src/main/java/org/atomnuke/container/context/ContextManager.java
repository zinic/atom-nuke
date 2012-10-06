package org.atomnuke.container.context;

import java.util.Collection;
import java.util.List;
import org.atomnuke.Nuke;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.config.server.ServerConfigurationHandler;
import org.atomnuke.container.ConfigurationProcessor;
import org.atomnuke.container.packaging.PackageContext;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.task.Tasker;
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
   private final Tasker tasker;

   public ContextManager(ServiceManager services, Collection<PackageContext> packages, Nuke nukeReference, Tasker tasker) {
      this.services = services;
      this.tasker = tasker;
      this.packages = packages;
      this.nukeReference = nukeReference;

      containerContext = new ContainerContext();
   }

   @Override
   public void updated(ServerConfiguration configuration) throws ConfigurationException {
      new ConfigurationProcessor(services, tasker, containerContext, new ServerConfigurationHandler(configuration), packages).merge(nukeReference);
   }
}
