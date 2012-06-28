package net.jps.nuke.atom.stax;

/**
 *
 * @author zinic
 */
public enum AtomElement {

   FEED,
   ENTRY,
   CONTENT,
   AUTHOR,
   CATEGORY,
   CONTRIBUTOR,
   GENERATOR,
   ICON,
   ID,
   LINK,
   LOGO,
   PUBLISHED,
   RIGHTS,
   SOURCE,
   SUBTITLE,
   SUMMARY,
   TITLE,
   UPDATED,
   NAME,
   EMAIL,
   URI;

   public static AtomElement findIgnoreCase(String st) {

      for (AtomElement element : values()) {
         if (element.name().equalsIgnoreCase(st)) {
            return element;
         }
      }

      return null;
   }
}
