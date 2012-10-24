package org.atomnuke;

import java.io.File;

/**
 * This is a static utility class for all of the Nuke environment variables and
 * their associated defaults.
 *
 * @author zinic
 */
public final class NukeEnv {

   public static final String NUKE_HOME = fromEnv("NUKE_HOME", System.getProperty("user.home") + File.separator + ".nuke");
   public static final String NUKE_DEPLOY = fromEnv("NUKE_DEPLOY", NUKE_HOME + File.separator + "deployed");
   public static final String NUKE_LIB = fromEnv("NUKE_LIB", NUKE_HOME + File.separator + "lib");
   public static final String CONFIG_LOCATION = fromEnv("NUKE_CONFIG", NUKE_HOME + File.separator + "nuke.cfg.xml");

   /**
    * Private helper method for getting a value from the environment. If the
    * value is null the default is returned.
    *
    * @param key environment variable name
    * @param defaultValue default value for the variable
    * @return the value of the environment variable
    */
   private static String fromEnv(String key, String defaultValue) {
      final String envValue = System.getenv().get(key);

      return envValue != null ? envValue : defaultValue;
   }
}
