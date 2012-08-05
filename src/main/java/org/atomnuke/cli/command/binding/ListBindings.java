package org.atomnuke.cli.command.binding;

import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.ConfigurationHandler;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.config.model.Binding;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.MessageResult;

/**
 *
 * @author zinic
 */
public class ListBindings extends AbstractNukeCommand {

   public ListBindings(ConfigurationReader configurationReader) {
      super(configurationReader);
   }

   @Override
   public String getCommandToken() {
      return "list";
   }

   @Override
   public String getCommandDescription() {
      return "Lists all registered bindings.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      final ConfigurationHandler cfgHandler = getConfigurationReader().readConfiguration();
      final StringBuilder output = new StringBuilder();

      for (Binding binding : cfgHandler.getBindings()) {
         output.append("Binding definition, ").append(binding.getId()).append(" binds reciever ").append(binding.getReceiver()).append(" to target ").append(binding.getTarget()).append("\n");
      }

      return new MessageResult(output.toString());
   }
}
