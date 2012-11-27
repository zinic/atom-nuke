package org.atomnuke;

import java.io.File;

/**
 * This is a static utility class for all of the Nuke environment variables and
 * their associated defaults.
 *
 * @author zinic
 */
public final class StaticNukeEnvironment implements NukeEnvironment {

   private static final StaticNukeEnvironment INSTANCE = new StaticNukeEnvironment();

   /**
    * Gets the static instance of the nuke environment.
    *
    * @return
    */
   public static NukeEnvironment get() {
      return INSTANCE;
   }

   private final String homeDirectory, deploymentDirectory, libraryDirectory, configurationDirectory;
   private final boolean debugEnabled;
   private final int numProcessors;

   public StaticNukeEnvironment() {
      // Important directories
      homeDirectory = fromEnv("NUKE_HOME", fromSystem("user.home", "") + File.separator + ".nuke");
      deploymentDirectory = fromEnv("NUKE_DEPLOY_DIR", homeDirectory + File.separator + "deployed");
      libraryDirectory = fromEnv("NUKE_LIB_DIR", homeDirectory + File.separator + "lib");
      configurationDirectory = fromEnv("NUKE_CONFIG_DIR", homeDirectory + File.separator + "etc");

      // Global debug setting
      debugEnabled = Boolean.parseBoolean(fromEnv("DEBUG", "false"));

      // Number of available processors
      numProcessors = Runtime.getRuntime().availableProcessors();
   }

   /**
    * Helper method for getting a value from the environment. If the value is
    * null the default is returned.
    *
    * @param key environment variable name
    * @param defaultValue default value for the variable
    * @return the value of the environment variable
    */
   @Override
   public String fromEnv(String key, String defaultValue) {
      final String envValue = System.getenv().get(key);

      return envValue != null ? envValue : defaultValue;
   }

   /**
    * Helper method for getting a value from the system properties. If the value
    * is null the default is returned.
    *
    * @param key system property name
    * @param defaultValue default value for the variable
    * @return the value of the environment variable
    */
   @Override
   public String fromSystem(String key, String defaultValue) {
      final String sysValue = System.getProperty(key);

      return sysValue != null ? sysValue : defaultValue;
   }

   @Override
   public String configurationDirectory() {
      return configurationDirectory;
   }

   @Override
   public String homeDirectory() {
      return homeDirectory;
   }

   @Override
   public String deploymentDirectory() {
      return deploymentDirectory;
   }

   @Override
   public String libraryDirectory() {
      return libraryDirectory;
   }

   @Override
   public boolean debugEnabled() {
      return debugEnabled;
   }

   @Override
   public boolean debugDisabled() {
      return !debugEnabled;
   }

   @Override
   public int numProcessors() {
      return numProcessors;
   }
}
