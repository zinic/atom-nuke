package org.atomnuke.container.context;

import org.atomnuke.Nuke;
import org.atomnuke.bindings.BindingContextManager;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.config.server.ServerConfigurationHandler;
import org.atomnuke.container.ConfigurationProcessor;
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
   private final BindingContextManager bindingResolver;
   private final ServiceManager services;
   private final Nuke nukeReference;
   private final Tasker tasker;

   public ContextManager(ServiceManager services, BindingContextManager bindingResolver, Nuke nukeReference, Tasker tasker) {
      this.services = services;
      this.tasker = tasker;
      this.bindingResolver = bindingResolver;
      this.nukeReference = nukeReference;

      containerContext = new ContainerContext();
   }

   @Override
   public void updated(ServerConfiguration configuration) throws ConfigurationException {
      new ConfigurationProcessor(services, tasker, containerContext, new ServerConfigurationHandler(configuration), bindingResolver).merge(nukeReference);
   }
}
