package net.jps.nuke.atom.xml;

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
   
   /*
    *          case SOURCE:
            startSource(attributeScannerDriver);
            break;

         case AUTHOR:
         case CONTRIBUTOR:
            startPersonConstruct(currentElement, attributeScannerDriver);
            break;

         case CONTENT:
            startContent(currentElement, attributeScannerDriver);
            break;

         case CATEGORY:
            startCategory(attributeScannerDriver);
            break;

         case LINK:
            startLink(attributeScannerDriver);
            break;

         case GENERATOR:
            startGenerator(attributeScannerDriver);
            break;

         case ID:
            startLangAwareTextElement(currentElement, attributeScannerDriver);
            break;

         case NAME:
         case EMAIL:
         case URI:
            startFieldContentElement(currentElement);
            break;

         case PUBLISHED:
         case UPDATED:
            startDateConstruct(currentElement, attributeScannerDriver);
            break;

         case RIGHTS:
         case TITLE:
         case SUMMARY:
            startTextConstruct(currentElement, attributeScannerDriver);
            break;
    */
   
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

   public static AtomElement findIgnoreCase(String st) {

      for (AtomElement element : values()) {
         if (element.name().equalsIgnoreCase(st)) {
            return element;
         }
      }

      return null;
   }
}
