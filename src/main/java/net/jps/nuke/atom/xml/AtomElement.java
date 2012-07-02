package net.jps.nuke.atom.xml;

/**
 *
 * @author zinic
 */
public enum AtomElement {

   // States, yo
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
   
   public static final AtomElement[] ROOT_ELEMENTS = new AtomElement[]{
      FEED,
      ENTRY
   };
   
   public static final AtomElement[] ENTRY_ELEMENTS = new AtomElement[]{
      SOURCE,
      AUTHOR,
      CONTRIBUTOR,
      CONTENT,
      CATEGORY,
      LINK,
      ID,
      NAME,
      EMAIL,
      URI,
      PUBLISHED,
      UPDATED,
      RIGHTS,
      TITLE,
      SUMMARY
   };
   
   public static final AtomElement[] FEED_ELEMENTS = new AtomElement[]{
      ENTRY,
      AUTHOR,
      CONTRIBUTOR,
      GENERATOR,
      CATEGORY,
      LINK,
      ID,
      ICON,
      LOGO,
      NAME,
      EMAIL,
      URI,
      UPDATED,
      RIGHTS,
      TITLE,
      SUBTITLE,
      SUMMARY
   };
   
   public static final AtomElement[] SOURCE_ELEMENTS = new AtomElement[]{
      AUTHOR,
      GENERATOR,
      CATEGORY,
      LINK,
      ID,
      ICON,
      LOGO,
      NAME,
      EMAIL,
      URI,
      UPDATED,
      RIGHTS,
      TITLE,
      SUBTITLE,
      SUMMARY
   };

   public static AtomElement findIgnoreCase(String st, AtomElement[] elements) {
      for (int i = 0; i < elements.length; i++) {
         if (elements[i].name().equalsIgnoreCase(st)) {
            return elements[i];
         }
      }

      return null;
   }
}
