package org.atomnuke;

/**
 * @author zinic
 */
public interface NukeEnvironment {

   String fromEnv(String key, String defaultValue);

   String fromSystem(String key, String defaultValue);

   boolean debugDisabled();

   boolean debugEnabled();

   String configurationDirectory();

   String deploymentDirectory();

   String homeDirectory();

   String libraryDirectory();

   int numProcessors();
}
