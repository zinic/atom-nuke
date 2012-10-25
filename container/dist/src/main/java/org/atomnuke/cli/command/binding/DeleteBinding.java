package org.atomnuke.cli.command.binding;

import java.util.Iterator;
import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.model.Binding;
import org.atomnuke.util.cli.command.result.CommandFailure;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class DeleteBinding extends AbstractNukeCommand {

   private static final int BINDING_ID = 0;

   public DeleteBinding(CliConfigurationHandler configurationHandler) {
      super(configurationHandler);
   }

   @Override
   public String getCommandToken() {
      return "rm";
   }

   @Override
   public String getCommandDescription() {
      return "Removes a binding definition.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      if (arguments.length != 1) {
         return new CommandFailure("Deleting a binding defintiion requires one argument: <binding-id>");
      }

      final CliConfigurationHandler cfgHandler = getConfigHandler();

      for (Iterator<Binding> bindingItr = cfgHandler.getBindings().iterator(); bindingItr.hasNext();) {
         if (bindingItr.next().getId().equals(arguments[BINDING_ID])) {
            bindingItr.remove();
            cfgHandler.write();

            return new CommandSuccess();
         }
      }

      return new CommandFailure("No binding with an id matching, \"" + arguments[BINDING_ID] + "\" seems to exist.");
   }
}
