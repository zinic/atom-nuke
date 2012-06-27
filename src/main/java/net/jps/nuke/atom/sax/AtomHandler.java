package net.jps.nuke.atom.sax;

import java.net.URI;
import java.net.URISyntaxException;
import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.ParserResultImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author zinic
 */
public class AtomHandler extends DefaultHandler {

   private static final String FEED_LOCAL_NAME = "feed";
   private static final String ID_LOCAL_NAME = "id";
   private static final String TITLE_LOCAL_NAME = "title";
   private static final String LINK_LOCAL_NAME = "link";

   private final ParserResultImpl result;

   public AtomHandler() {
      result = new ParserResultImpl();
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      if (LINK_LOCAL_NAME.equalsIgnoreCase(localName)) {
         // it's a link - let's investigate this!
         final String relation = attributes.getValue("rel");
         final String href = attributes.getValue("href");

         if ("previous".equalsIgnoreCase(relation) && href != null) {
            try {
               result.setNextLocation(new URI(href));
            } catch (URISyntaxException urise) {
            }
         }
      }
   }

   public ParserResult getResult() {
      return result;
   }
}
