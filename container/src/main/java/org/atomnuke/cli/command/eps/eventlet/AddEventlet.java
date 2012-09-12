package org.atomnuke.cli.command.eps.eventlet;

import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.server.ServerConfigurationHandler;
import org.atomnuke.util.config.io.ConfigurationReader;
import org.atomnuke.config.model.Eventlet;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.util.cli.command.result.CommandFailure;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class AddEventlet extends AbstractNukeCommand {

   private static final int EVENTLET_ID = 0, EVENTLET_LANG = 1, EVENTLET_REFERENCE = 2;

   public AddEventlet(ServerConfigurationHandler configurationHandler) {
      super(configurationHandler);
   }

   @Override
   public String getCommandToken() {
      return "add";
   }

   @Override
   public String getCommandDescription() {
      return "Adds a new eventlet definition.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      if (arguments.length != 3) {
         return new CommandFailure("Adding an eventlet requires three arguments: <eventlet-id> <language> <ref>");
      }

      final ServerConfigurationHandler cfgHandler = getConfigHandler();

      if (cfgHandler.findEventlet(arguments[EVENTLET_ID]) != null) {
         return new CommandFailure("An eventlet with the id \"" + arguments[EVENTLET_ID] + "\" already exists.");
      }

      final LanguageType sinkLanguageType;

      try {
         sinkLanguageType = LanguageType.fromValue(arguments[EVENTLET_LANG]);
      } catch (IllegalArgumentException iae) {
         return new CommandFailure("Language \"" + arguments[EVENTLET_LANG] + "\" not valid. Language must be one of: java, javascript, python.");
      }

      if (sinkLanguageType == null) {
         return new CommandFailure("Language must be one of: java, javascript, python.");
      }

      final Eventlet newEventlet = new Eventlet();
      newEventlet.setId(arguments[EVENTLET_ID]);
      newEventlet.setType(sinkLanguageType);
      newEventlet.setHref(arguments[EVENTLET_REFERENCE]);

      cfgHandler.getEventlets().add(newEventlet);
      cfgHandler.write();

      return new CommandSuccess();
   }
}
