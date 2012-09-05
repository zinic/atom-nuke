package org.atomnuke.atom.model;

/**
 *
 * @author zinic
 */
public enum Type {

   TEXT,
   HTML,
   XHTML,
   OTHER;

   public static Type find(String st) {
      for (Type type : values()) {
         if (type.cannonicalName().equalsIgnoreCase(st)) {
            return type;
         }
      }

      return OTHER;
   }

   private String cannonicalName;

   private Type() {
      this.cannonicalName = name();
   }

   public String cannonicalName() {
      return cannonicalName;
   }
}
