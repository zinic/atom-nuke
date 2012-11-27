package org.atomnuke;

import java.io.File;
import javax.xml.bind.JAXBException;
import org.atomnuke.atombus.config.model.ServerConfiguration;
import org.atomnuke.cli.command.Root;
import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.fallout.config.server.ServerConfigurationFileManager;
import org.atomnuke.util.cli.CommandDriver;
import org.atomnuke.util.cli.command.Command;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public final class NukeMain {

   private static final Logger LOG = LoggerFactory.getLogger(NukeMain.class);

   private final NukeEnvironment environment;

   private NukeMain(NukeEnvironment environment) {
      this.environment = environment;
   }

   public void init(String[] args) throws ConfigurationException, JAXBException {
      final File configurationLocation = new File(environment.configurationDirectory(), "nuke.cfg.xml");
      final ConfigurationManager<ServerConfiguration> cfgManager = new ServerConfigurationFileManager(configurationLocation);
      final ServerConfiguration previousCfg = cfgManager.read();

      final CliConfigurationHandler handler = new CliConfigurationHandler(cfgManager, previousCfg != null ? previousCfg : new ServerConfiguration());

      final Command rootCommand = new Root(handler);

      final CommandResult result = new CommandDriver(rootCommand, args).go();
      final String stringResult = result.getStringResult();

      if (stringResult != null && stringResult.length() > 0) {
         System.out.println(result.getStringResult());
      }

      if (result.shouldExit()) {
         System.exit(result.getStatusCode());
      }
   }

   public static void main(String[] args) {
      try {
         new NukeMain(StaticNukeEnvironment.get()).init(args);
      } catch (ConfigurationException ce) {
         LOG.error("Configuration error: " + ce.getMessage(), ce);
      } catch (JAXBException jaxbe) {
         LOG.error("JAXB Exception caught: " + jaxbe.getMessage(), jaxbe);
      }
   }
}
