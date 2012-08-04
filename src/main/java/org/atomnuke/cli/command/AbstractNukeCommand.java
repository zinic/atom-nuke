package org.atomnuke.cli.command;

import java.util.List;
import org.atomnuke.config.ConfigurationException;
import org.atomnuke.config.ConfigurationHandler;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.config.model.Sink;
import org.atomnuke.config.model.Sinks;
import org.atomnuke.config.model.Source;
import org.atomnuke.config.model.Sources;
import org.atomnuke.util.cli.command.AbstractCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public abstract class AbstractNukeCommand extends AbstractCommand {

   private final ConfigurationReader configurationReader;
   private final Logger logger;

   public AbstractNukeCommand(ConfigurationReader configurationReader) {
      this.configurationReader = configurationReader;
      this.logger = logger();
   }

   private Logger logger() {
      return LoggerFactory.getLogger(getClass());
   }

   protected Logger log() {
      return logger;
   }

   protected final ConfigurationReader getConfigurationReader() {
      return configurationReader;
   }

   protected List<Source> getSources(ConfigurationHandler cfgHandler) throws ConfigurationException {
      if (cfgHandler.getConfiguration().getSources() == null) {
         cfgHandler.getConfiguration().setSources(new Sources());
         cfgHandler.write();
      }

      return cfgHandler.getConfiguration().getSources().getSource();
   }

   protected List<Sink> getSinks(ConfigurationHandler cfgHandler) throws ConfigurationException {
      if (cfgHandler.getConfiguration().getSinks() == null) {
         cfgHandler.getConfiguration().setSinks(new Sinks());
         cfgHandler.write();
      }

      return cfgHandler.getConfiguration().getSinks().getSink();
   }

   protected Source findSource(String id) throws ConfigurationException {
      final ConfigurationHandler cfgHandler = getConfigurationReader().readConfiguration();

      if (cfgHandler != null) {
         if (cfgHandler.getConfiguration().getSources() == null) {
            cfgHandler.getConfiguration().setSources(new Sources());
            cfgHandler.write();
         }

         for (Source source : cfgHandler.getConfiguration().getSources().getSource()) {
            if (source.getId().equals(id)) {
               return source;
            }
         }
      }

      return null;
   }

   protected Sink findSink(String id) throws ConfigurationException {
      final ConfigurationHandler cfgHandler = getConfigurationReader().readConfiguration();

      if (cfgHandler != null) {
         if (cfgHandler.getConfiguration().getSinks() == null) {
            cfgHandler.getConfiguration().setSinks(new Sinks());
            cfgHandler.write();
         }

         for (Sink sink : cfgHandler.getConfiguration().getSinks().getSink()) {
            if (sink.getId().equals(id)) {
               return sink;
            }
         }
      }

      return null;
   }
}
