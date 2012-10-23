package org.atomnuke.cli.command.binding;

import java.util.List;
import java.util.UUID;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.util.config.io.ConfigurationReader;
import org.atomnuke.config.model.Binding;
import org.atomnuke.util.cli.command.result.CommandFailure;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class AddBinding extends AbstractNukeCommand {

   private static final int TARGET_ID = 0, RECEIVER_ID = 1;

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
         return new CommandFailure("Creating a binding requires two arguments: <target-id> <receiver-id>");
      }

      final CliConfigurationHandler cfgHandler = getConfigHandler();

      if (cfgHandler.findSource(arguments[TARGET_ID]) == null && cfgHandler.findRelay(arguments[TARGET_ID]) == null) {
         return new CommandFailure("Unable to locate a source or relay with the id, \"" + arguments[TARGET_ID] + "\"");
      }

      if (cfgHandler.findSink(arguments[RECEIVER_ID]) == null && cfgHandler.findRelay(arguments[RECEIVER_ID]) == null && cfgHandler.findEventlet(arguments[RECEIVER_ID]) == null) {
         return new CommandFailure("Unable to locate a sink or eventlet with the id, \"" + arguments[RECEIVER_ID] + "\"");
      }

      for (Binding binding : cfgHandler.getBindings()) {
         if (binding.getTarget().equals(arguments[TARGET_ID]) && binding.getReceiver().equals(arguments[RECEIVER_ID])) {
            return new CommandFailure("Binding already exists as binding, \"" + binding.getId() + "\"");
         }
      }

      final Binding newBinding = new Binding();
      newBinding.setId(UUID.randomUUID().toString());
      newBinding.setTarget(arguments[TARGET_ID]);
      newBinding.setReceiver(arguments[RECEIVER_ID]);

      cfgHandler.getBindings().add(newBinding);
      cfgHandler.write();

      return new CommandSuccess();
   }
}
