package org.atomnuke.cli.command.binding;

import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class BindingCommands extends AbstractCommandList {

   public BindingCommands(CliConfigurationHandler configurationHandler) {
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
