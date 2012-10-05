package org.atomnuke.container.classloader.archive;

import java.util.regex.Pattern;

/**
 *
 * @author zinic
 */
public enum ResourceType {

   EAR(".*\\.ear"),
   JAVASCRIPT(".*\\.js"),
   PYTHON(".*\\.py"),
   JAR(".*\\.jar"),
   CLASS(".*\\.class"),
   RESOURCE(".*");

   public static ResourceType findResourceTypeForExtension(String extension) {
      for (ResourceType type : values()) {
         if (type.name().equalsIgnoreCase(extension)) {
            return type;
         }
      }

      return RESOURCE;
   }

   public static ResourceType findResourceTypeForName(String name) {
      for (ResourceType type : values()) {
         if (type.matchesExtensionFor(name)) {
            return type;
         }
      }

      return RESOURCE;
   }

   private final Pattern extensionPattern;

   private ResourceType(String extensionPattern) {
      this.extensionPattern = Pattern.compile(extensionPattern);
   }

   public boolean matchesExtensionFor(String name) {
      return extensionPattern.matcher(name).matches();
   }
}
