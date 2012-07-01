package net.jps.nuke.atom.sax;

import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author zinic
 */
public abstract class ReaderAwareHandler extends DefaultHandler {

   private final XMLReader readerInstance;

   public ReaderAwareHandler(XMLReader readerInstance) {
      this.readerInstance = readerInstance;
   }

   protected XMLReader getReader() {
      return readerInstance;
   }

   protected void delegateTo(ContentHandler handler) {
      readerInstance.setContentHandler(handler);
   }
}
