package org.atomnuke.cli.command.binding;

import org.atomnuke.config.server.ServerConfigurationHandler;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class BindingCommands extends AbstractCommandList {

   public BindingCommands(ServerConfigurationHandler configurationHandler) {
      super(new AddBinding(configurationHandler), new DeleteBinding(configurationHandler), new ListBindings(configurationHandler));
   }

   @Override
   public String getCommandToken() {
      return "bindings";
   }

   @Override
   public String getCommandDescription() {
      return "Binding commands.";
   }
}
