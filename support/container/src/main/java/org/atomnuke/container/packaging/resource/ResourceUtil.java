package org.atomnuke.container.packaging.resource;

import com.rackspace.papi.commons.util.StringUtilities;

/**
 *
 * @author zinic
 */
public final class ResourceUtil {

   private static final ResourceUtil RESOURCE_UTIL = new ResourceUtil();

   public static ResourceUtil instance() {
      return RESOURCE_UTIL;
   }

   private ResourceUtil() {
   }

   public String relativePathToClassPath(String unsanitizedPath) {
      if (unsanitizedPath == null) {
         throw new IllegalArgumentException("Path must not be null.");
      }

      return StringUtilities.trim(unsanitizedPath, "/").replace(".class", "").replace("/", ".");
   }

   public String relativePathToJavaPackage(String unsanitizedPath) {
      if (unsanitizedPath == null) {
         throw new IllegalArgumentException("Path must not be null.");
      }

      final String trimmedPath = StringUtilities.trim(unsanitizedPath, "/");
      final int lastIndexOfIFS = unsanitizedPath.lastIndexOf("/");
      final String sanitizedPath = (lastIndexOfIFS > 0 ? trimmedPath.substring(0, lastIndexOfIFS) : trimmedPath).replace("/", ".");

      return sanitizedPath;
   }
}
