package org.atomnuke.cli.command.binding;

import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.util.cli.command.AbstractCommandList;

/**
 *
 * @author zinic
 */
public class BindingCommands extends AbstractCommandList {

   public BindingCommands(ConfigurationReader configurationReader) {
      super(new AddBinding(configurationReader), new DeleteBinding(configurationReader), new ListBindings(configurationReader));
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
