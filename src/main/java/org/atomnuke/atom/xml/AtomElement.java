package org.atomnuke.atom.xml;

/**
 *
 * @author zinic
 */
public enum AtomElement {

   // States, yo
   FEED("feed"),
   ENTRY("entry"),
   CONTENT("content"),
   AUTHOR("author"),
   CATEGORY("category"),
   CONTRIBUTOR("contributor"),
   GENERATOR("generator"),
   ICON("icon"),
   ID("id"),
   LINK("link"),
   LOGO("logo"),
   PUBLISHED("published"),
   RIGHTS("rights"),
   SOURCE("source"),
   SUBTITLE("subtitle"),
   SUMMARY("summary"),
   TITLE("title"),
   UPDATED("updated"),
   NAME("name"),
   EMAIL("email"),
   URI("uri");
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

   public static AtomElement find(String elementName, AtomElement[] elements) {
      for (int i = 0; i < elements.length; i++) {
         if (elements[i].elementName().equals(elementName)) {
            return elements[i];
         }
      }

      return null;
   }
   private final String elementName;

   private AtomElement(String elementName) {
      this.elementName = elementName;
   }

   public String elementName() {
      return elementName;
   }
}
