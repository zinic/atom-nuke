package org.atomnuke.container.context;

import org.atomnuke.Nuke;
import org.atomnuke.bindings.resolver.BindingResolver;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.config.server.ServerConfigurationHandler;
import org.atomnuke.container.ConfigurationProcessor;
import org.atomnuke.task.Tasker;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.update.listener.ConfigurationListener;

/**
 *
 * @author zinic
 */
public class ContextManager implements ConfigurationListener<ServerConfiguration> {

   private final ContainerContext containerContext;
   private final BindingResolver bindingResolver;
   private final Nuke nukeReference;
   private final Tasker tasker;

   public ContextManager(BindingResolver bindingResolver, Nuke nukeReference, Tasker tasker) {
      this.tasker = tasker;
      this.bindingResolver = bindingResolver;
      this.nukeReference = nukeReference;

      containerContext = new ContainerContext();
   }

   @Override
   public void updated(ServerConfiguration configuration) throws ConfigurationException {
      new ConfigurationProcessor(tasker, containerContext, new ServerConfigurationHandler(configuration), bindingResolver).merge(nukeReference);
   }
}
