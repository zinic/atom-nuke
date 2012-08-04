package org.atomnuke.atom.model;

/**
 *
 * @author zinic
 */
public enum Type {

   TEXT,
   HTML,
   XHTML,
   INVALID;

   public static Type find(String st) {
      for (Type type : values()) {
         if (type.getFormattedName().equals(st)) {
            return type;
         }
      }

      return INVALID;
   }
   private String formattedName;

   private Type() {
      formattedName = name().toLowerCase();
   }

   public String getFormattedName() {
      return formattedName;
   }
}
