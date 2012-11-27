package org.atomnuke.cli.command.binding;

import java.util.UUID;
import org.atomnuke.atombus.config.model.Binding;
import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.util.cli.command.result.CommandFailure;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class AddBinding extends AbstractNukeCommand {

   private static final int SOURCE_ID = 0, SINK_ID = 1;

   public AddBinding(CliConfigurationHandler configurationHandler) {
      super(configurationHandler);
   }

   @Override
   public String getCommandToken() {
      return "add";
   }

   @Override
   public String getCommandDescription() {
      return "Creates a binding between a target and a reciever.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      if (arguments.length != 2) {
         return new CommandFailure("Creating a binding requires two arguments: <source-id> <sink-id>");
      }

      final CliConfigurationHandler cfgHandler = getConfigHandler();

      if (cfgHandler.findMessageSource(arguments[SOURCE_ID]) == null) {
         return new CommandFailure("Unable to locate a source with the id, \"" + arguments[SOURCE_ID] + "\" to bind to.");
      }

      if (cfgHandler.findMessageActor(arguments[SINK_ID]) == null) {
         return new CommandFailure("Unable to locate a message actor with the id, \"" + arguments[SINK_ID] + "\" for binding.");
      }

      for (Binding binding : cfgHandler.getBindings()) {
         if (binding.getSourceActor().equals(arguments[SOURCE_ID]) && binding.getSinkActor().equals(arguments[SINK_ID])) {
            return new CommandFailure("Binding already exists as binding, \"" + binding.getId() + "\"");
         }
      }

      final Binding newBinding = new Binding();
      newBinding.setId(UUID.randomUUID().toString());
      newBinding.setSourceActor(arguments[SOURCE_ID]);
      newBinding.setSinkActor(arguments[SINK_ID]);

      cfgHandler.getBindings().add(newBinding);
      cfgHandler.write();

      return new CommandSuccess();
   }
}
