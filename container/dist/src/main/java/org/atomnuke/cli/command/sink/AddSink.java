package org.atomnuke.cli.command.sink;

import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.config.model.Sink;
import org.atomnuke.util.cli.command.result.CommandFailure;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class AddSink extends AbstractNukeCommand {

   private static final int SINK_ID = 0, SINK_LANGUAGE = 1, SINK_REFERENCE = 2;

   public AddSink(CliConfigurationHandler configurationHandler) {
      super(configurationHandler);
   }

   @Override
   public String getCommandToken() {
      return "add";
   }

   @Override
   public String getCommandDescription() {
      return "Adds a new sink definition.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      if (arguments.length != 3) {
         return new CommandFailure("Adding a sink requires three arguments: <sink-id> <language> <ref>");
      }

      final CliConfigurationHandler cfgHandler = getConfigHandler();

      if (cfgHandler.findSink(arguments[SINK_ID]) != null) {
         return new CommandFailure("A sink with the id \"" + arguments[SINK_ID] + "\" already exists.");
      }

      final LanguageType sinkLanguageType;

      try {
         sinkLanguageType = LanguageType.fromValue(arguments[SINK_LANGUAGE]);
      } catch (IllegalArgumentException iae) {
         return new CommandFailure("Language \"" + arguments[SINK_LANGUAGE] + "\" not valid. Language must be one of: java, javascript, python.");
      }

      if (sinkLanguageType == null) {
         return new CommandFailure("Language must be one of: java, javascript, python.");
      }

      final Sink newSink = new Sink();
      newSink.setId(arguments[SINK_ID]);
      newSink.setType(sinkLanguageType);
      newSink.setHref(arguments[SINK_REFERENCE]);

      cfgHandler.getSinks().add(newSink);
      cfgHandler.write();

      return new CommandSuccess();
   }
}
