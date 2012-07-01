package net.jps.nuke.atom.model;

/**
 *
 * @author zinic
 */
public enum Type {

   TEXT,
   HTML,
   XHTML,
   INVALID;

   public static Type findIgnoreCase(String st) {
      for (Type type : values()) {
         if (type.name().equalsIgnoreCase(st)) {
            return type;
         }
      }

      return INVALID;
   }
}
