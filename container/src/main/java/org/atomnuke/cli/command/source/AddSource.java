package org.atomnuke.cli.command.source;

import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.config.server.ServerConfigurationHandler;
import org.atomnuke.util.config.io.ConfigurationReader;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.config.model.PollingInterval;
import org.atomnuke.config.model.Source;
import org.atomnuke.config.model.TimeUnitType;
import org.atomnuke.util.cli.command.result.CommandFailure;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class AddSource extends AbstractNukeCommand {

   private static final int SOURCE_ID = 0, SOURCE_LANG = 1, SOURCE_REFERENCE = 2, POLLING_INTERVAL = 3, TIME_UNIT = 4;

   public AddSource(ServerConfigurationHandler configurationHandler) {
      super(configurationHandler);
   }

   @Override
   public String getCommandToken() {
      return "add";
   }

   @Override
   public String getCommandDescription() {
      return "Adds a new source definition.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      if (arguments.length < 3 || arguments.length > 5) {
         return new CommandFailure("Usage: <source-id> <language-type> <ref> [polling interval] [time-unit]");
      }

      final boolean hasPollingInterval = arguments.length > 3;
      final boolean hasTimeUnit = arguments.length > 4;

      final ServerConfigurationHandler cfgHandler = getConfigHandler();

      if (cfgHandler.findSource(arguments[SOURCE_ID]) != null) {
         return new CommandFailure("A source with the id \"" + arguments[SOURCE_ID] + "\" already exists.");
      }

      final LanguageType sinkLanguageType;

      try {
         sinkLanguageType = LanguageType.fromValue(arguments[SOURCE_LANG].toLowerCase());
      } catch (IllegalArgumentException iae) {
         return new CommandFailure("Language must be one of: java, javascript, python.");
      }

      final Source newSource = new Source();
      newSource.setId(arguments[SOURCE_ID]);
      newSource.setType(sinkLanguageType);
      newSource.setHref(arguments[SOURCE_REFERENCE]);

      final PollingInterval pollingInterval = new PollingInterval();

      if (hasPollingInterval) {
         pollingInterval.setValue(Long.parseLong(arguments[POLLING_INTERVAL]));

         if (hasTimeUnit) {
            try {
               pollingInterval.setUnit(TimeUnitType.fromValue(arguments[TIME_UNIT].toUpperCase()));
            } catch (IllegalArgumentException iae) {
               return new CommandFailure("Time unit must be one of: NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, OR DAYS.");
            }
         } else {
            pollingInterval.setUnit(TimeUnitType.MILLISECONDS);
         }
      } else {
         pollingInterval.setValue(1);
         pollingInterval.setUnit(TimeUnitType.SECONDS);
      }

      newSource.setPollingInterval(pollingInterval);

      cfgHandler.getSources().add(newSource);
      cfgHandler.write();

      return new CommandSuccess();
   }
}
