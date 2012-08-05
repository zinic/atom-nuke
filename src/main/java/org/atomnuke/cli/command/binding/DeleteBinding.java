package org.atomnuke.cli.command.binding;

import java.util.Iterator;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.ConfigurationHandler;
import org.atomnuke.config.ConfigurationReader;
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

   public DeleteBinding(ConfigurationReader configurationReader) {
      super(configurationReader);
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

      final ConfigurationHandler cfgHandler = getConfigurationReader().readConfiguration();

      for (Iterator<Binding> bindingItr = getBindings(cfgHandler).iterator(); bindingItr.hasNext();) {
         if (bindingItr.next().getId().equals(arguments[BINDING_ID])) {
            bindingItr.remove();
            cfgHandler.write();

            return new CommandSuccess();
         }
      }

      return new CommandFailure("No binding with an id matching, \"" + arguments[BINDING_ID] + "\" seems to exist.");
   }
}
