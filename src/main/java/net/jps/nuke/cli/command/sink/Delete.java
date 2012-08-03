package net.jps.nuke.cli.command.sink;

import net.jps.nuke.cli.command.AbstractNukeCommand;
import net.jps.nuke.config.ConfigurationHandler;
import net.jps.nuke.config.ConfigurationReader;
import net.jps.nuke.config.model.LanguageType;
import net.jps.nuke.config.model.Sink;
import net.jps.nuke.config.model.Sinks;
import net.jps.nuke.util.cli.command.result.CommandFailure;
import net.jps.nuke.util.cli.command.result.CommandResult;
import net.jps.nuke.util.cli.command.result.SuccessResult;

/**
 *
 * @author zinic
 */
public class Delete extends AbstractNukeCommand {

   private static final int SINK_ID = 0, SINK_LANGUAGE = 1, SINK_REFERENCE = 2;

   public Delete(ConfigurationReader configurationReader) {
      super(configurationReader);
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

      final ConfigurationHandler cfgHandler = getConfigurationReader().readConfiguration();

      if (cfgHandler != null) {
         if (cfgHandler.getConfiguration().getSinks() == null) {
            cfgHandler.getConfiguration().setSinks(new Sinks());
         }

         for (Sink sink : cfgHandler.getConfiguration().getSinks().getSink()) {
            if (sink.getId().equals(arguments[SINK_ID])) {
               return new CommandFailure("A sink with the id \"" + arguments[SINK_ID] + "\" already exists.");
            }
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

         cfgHandler.getConfiguration().getSinks().getSink().add(newSink);
         cfgHandler.write();
      }

      return new SuccessResult();
   }
}
