package org.atomnuke.cli.command.binding;

import org.atomnuke.atombus.config.model.Binding;
import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.util.cli.command.AbstractCommandList;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.MessageResult;

/**
 *
 * @author zinic
 */
public class BindingCommands extends AbstractCommandList {

   public BindingCommands(CliConfigurationHandler configurationHandler) {
      super(configurationHandler, new AddBinding(configurationHandler), new DeleteBinding(configurationHandler));
   }

   @Override
   public CommandResult perform() throws Exception {
      final CliConfigurationHandler cfgHandler = getConfigHandler();
      final StringBuilder output = new StringBuilder();

      for (Binding binding : cfgHandler.getBindings()) {
         output.append(binding.getId()).append(" binds source \"").append(binding.getSourceActor()).append("\" to sink \"").append(binding.getSinkActor()).append("\"\n");
      }

      return new MessageResult(output.toString());
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
