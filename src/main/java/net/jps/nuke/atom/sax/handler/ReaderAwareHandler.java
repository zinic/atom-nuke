package net.jps.nuke.atom.sax.handler;

import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author zinic
 */
public abstract class ReaderAwareHandler extends DefaultHandler {

   private final ContentHandler parentHandler;
   private final XMLReader readerInstance;

   public ReaderAwareHandler(XMLReader readerInstance) {
      this.readerInstance = readerInstance;
      parentHandler = null;
   }

   public ReaderAwareHandler(ReaderAwareHandler parentHandler) {
      this.parentHandler = parentHandler;
      this.readerInstance = parentHandler.getReader();
   }

   protected boolean hasParent() {
      return parentHandler != null;
   }
   
   protected XMLReader getReader() {
      return readerInstance;
   }
   
   protected void delegateTo(ContentHandler handler) {
      readerInstance.setContentHandler(handler);
   }

   protected ContentHandler releaseToParent() {
      delegateTo(parentHandler);
      return parentHandler;
   }
}
