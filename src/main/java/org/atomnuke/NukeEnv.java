package org.atomnuke;

import java.io.File;

/**
 *
 * @author zinic
 */
public final class NukeEnv {

   public static final String NUKE_HOME = fromEnv("NUKE_HOME", System.getProperty("user.home") + File.separator + ".nuke");
   public static final String NUKE_LIB = fromEnv("NUKE_LIB", NUKE_HOME + File.separator + "lib");
   public static final String CONFIG_NAME = fromEnv("NUKE_CONFIG", File.separator + "nuke.cfg.xml");

   private static String fromEnv(String key, String defaultValue) {
      final String envValue = System.getenv().get(key);

      return envValue != null ? envValue : defaultValue;
   }
}
