package org.atomnuke.cli.command.server;

import org.atomnuke.Nuke;
import org.atomnuke.bindings.resolver.BindingResolver;
import org.atomnuke.bindings.resolver.BindingResolverImpl;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.cli.command.server.builder.ServerBuilder;
import org.atomnuke.config.ConfigurationHandler;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class Start extends AbstractNukeCommand {

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
      final BindingResolver bindingsResolver = BindingResolverImpl.defaultResolver();
      
      final ConfigurationHandler cfgHandler = getConfigurationReader().readConfiguration();
      final ServerBuilder serverBuilder = new ServerBuilder(cfgHandler, bindingsResolver);
      final Nuke nuke = serverBuilder.build();
      
      nuke.start();
      
      return new CommandSuccess();
   }
}
