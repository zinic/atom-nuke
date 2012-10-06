package org.atomnuke.container.packaging.archive;

import java.util.regex.Pattern;

/**
 *
 * Not so sure about this one...
 *
 * @author zinic
 */
public enum ResourceType {

   EAR("ear", ".*\\.ear"),
   ZIP("zip", ".*\\.zip"),
   JAVASCRIPT("js", ".*\\.js"),
   PYTHON("py", ".*\\.py"),
   JAR("jar", ".*\\.jar"),
   CLASS("class", ".*\\.class"),
   RESOURCE("", ".*");


   private static final ResourceType[] SEARCH_EXCLUDE = new ResourceType[]{RESOURCE};

   public static boolean isExcludeType(ResourceType type) {
      for (ResourceType exclude : SEARCH_EXCLUDE) {
         if (exclude == type) {
            return true;
         }
      }

      return false;
   }

   public static ResourceType findResourceTypeForExtension(String extension) {
      for (ResourceType type : values()) {
         if (!isExcludeType(type) && type.extension().equalsIgnoreCase(extension)) {
            return type;
         }
      }

      return RESOURCE;
   }

   public static ResourceType findResourceTypeForName(String name) {
      for (ResourceType type : values()) {
         if (!isExcludeType(type) && type.matchesExtensionFor(name)) {
            return type;
         }
      }

      return RESOURCE;
   }

   private final Pattern extensionPattern;
   private final String extension;

   private ResourceType(String extension, String extensionPattern) {
      this.extension = extension;
      this.extensionPattern = Pattern.compile(extensionPattern);
   }

   public String extension() {
      return extension;
   }

   public boolean matchesExtensionFor(String name) {
      return extensionPattern.matcher(name).matches();
   }
}
