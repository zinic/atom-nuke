package org.atomnuke.cli.command.source;

import org.atomnuke.atombus.config.model.MessageSource;
import org.atomnuke.atombus.config.model.PollingInterval;
import org.atomnuke.atombus.config.model.TimeUnitType;
import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.util.cli.command.result.CommandFailure;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class AddSource extends AbstractNukeCommand {

   private static final int SOURCE_ID = 0, POLLING_INTERVAL = 2, TIME_UNIT = 3;

   public AddSource(CliConfigurationHandler configurationHandler) {
      super(configurationHandler);
   }

   @Override
   public String getCommandToken() {
      return "add";
   }

   @Override
   public String getCommandDescription() {
      return "Adds a new message source definition for polling.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      if (arguments.length < 1 || arguments.length > 3) {
         return new CommandFailure("Usage: <source-id> [polling interval] [time-unit]");
      }

      final boolean hasPollingInterval = arguments.length > 3;
      final boolean hasTimeUnit = arguments.length > 4;

      final CliConfigurationHandler cfgHandler = getConfigHandler();

      if (cfgHandler.findMessageSource(arguments[SOURCE_ID]) != null) {
         return new CommandFailure("A message source with the id \"" + arguments[SOURCE_ID] + "\" already exists.");
      }

      final MessageSource newSource = new MessageSource();
      newSource.setActorRef(arguments[SOURCE_ID]);

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

      cfgHandler.getMessageSources().add(newSource);
      cfgHandler.write();

      return new CommandSuccess();
   }
}
