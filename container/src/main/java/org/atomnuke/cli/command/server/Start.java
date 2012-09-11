package org.atomnuke.cli.command.server;

import org.atomnuke.Nuke;
import org.atomnuke.NukeEnv;
import org.atomnuke.bindings.loader.DirectoryLoaderManager;
import org.atomnuke.bindings.resolver.BindingResolver;
import org.atomnuke.bindings.resolver.BindingResolverImpl;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.cli.command.server.builder.ServerBuilder;
import org.atomnuke.config.ConfigurationHandler;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class Start extends AbstractNukeCommand {

   private final Logger LOG = LoggerFactory.getLogger(Start.class);

   public Start(ConfigurationReader configurationReader) {
      super(configurationReader);
   }

   @Override
   public String getCommandToken() {
      return "start";
   }

   @Override
   public String getCommandDescription() {
      return "Starts a new nuke instance.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      LOG.info("Starting Nuke container...");

      final BindingResolver bindingsResolver = BindingResolverImpl.defaultResolver();
      final DirectoryLoaderManager loaderManager = new DirectoryLoaderManager(NukeEnv.NUKE_LIB, bindingsResolver.registeredBindingContexts());

      loaderManager.load();

      final ConfigurationHandler cfgHandler = getConfigurationReader().readConfiguration();
      final ServerBuilder serverBuilder = new ServerBuilder(cfgHandler, bindingsResolver);
      final Nuke nuke = serverBuilder.build();

      nuke.start();

      return new CommandSuccess("Nuke container started.");
   }
}
