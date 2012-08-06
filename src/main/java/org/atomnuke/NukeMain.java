package org.atomnuke;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.atomnuke.cli.command.Root;
import org.atomnuke.config.FileConfigurationManager;
import org.atomnuke.config.model.ObjectFactory;
import org.atomnuke.util.cli.CommandDriver;
import org.atomnuke.util.cli.command.Command;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public final class NukeMain {

   private static final Logger LOG = LoggerFactory.getLogger(NukeMain.class);
   private static final String NUKE_HOME = fromEnv("NUKE_HOME", System.getProperty("user.home") + File.separator + ".nuke");
   private static final String CONFIG_NAME = fromEnv("NUKE_CONFIG", File.separator + "nuke.cfg.xml");

   public static String fromEnv(String key, String defaultValue) {
      final String envValue = System.getenv().get(key);

      return envValue != null ? envValue : defaultValue;
   }

   private NukeMain() {
   }

   public static void main(String[] args) throws Exception {
      try {
         final JAXBContext jaxbc = JAXBContext.newInstance(ObjectFactory.class);
         final Command rootCommand = new Root(new FileConfigurationManager(jaxbc.createMarshaller(), jaxbc.createUnmarshaller(), new ObjectFactory(), new File(NUKE_HOME), CONFIG_NAME));

         final CommandResult result = new CommandDriver(rootCommand, args).go();
         final String stringResult = result.getStringResult();

         if (stringResult != null && stringResult.length() > 0) {
            LOG.info(result.getStringResult());
         }

         if (result.shouldExit()) {
            System.exit(result.getStatusCode());
         }
      } catch (JAXBException jaxbe) {
         LOG.error(jaxbe.getMessage(), jaxbe);
         System.exit(1000);
      }
   }
}
