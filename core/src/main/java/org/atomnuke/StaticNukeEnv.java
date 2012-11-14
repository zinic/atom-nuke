package org.atomnuke;

import java.io.File;

/**
 * This is a static utility class for all of the Nuke environment variables and
 * their associated defaults.
 *
 * @author zinic
 */
public final class StaticNukeEnv implements NukeEnvironment {

   private static final StaticNukeEnv INSTANCE = new StaticNukeEnv();

   /**
    * Gets the static instance of the nuke environment.
    *
    * @return
    */
   public static NukeEnvironment get() {
      return INSTANCE;
   }
   private final String homeDirectory, deploymentDirectory, libraryDirectory, configurationLocation;
   private final boolean debugEnabled;
   private final int numProcessors;

   public StaticNukeEnv() {
      homeDirectory = fromEnv("NUKE_HOME", fromSystem("user.home", "") + File.separator + ".nuke");
      deploymentDirectory = fromEnv("NUKE_DEPLOY", homeDirectory + File.separator + "deployed");
      libraryDirectory = fromEnv("NUKE_LIB", homeDirectory + File.separator + "lib");
      configurationLocation = fromEnv("NUKE_CONFIG", homeDirectory + File.separator + "nuke.cfg.xml");
      numProcessors = Runtime.getRuntime().availableProcessors();
      debugEnabled = Boolean.parseBoolean(fromEnv("DEBUG", "false"));
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
   public String configurationLocation() {
      return configurationLocation;
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
