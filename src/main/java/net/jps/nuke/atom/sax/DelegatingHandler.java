package net.jps.nuke.atom.sax;

import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author zinic
 */
public abstract class DelegatingHandler extends DefaultHandler {

   private final ContentHandler parentHandler;
   private final XMLReader readerInstance;

   public DelegatingHandler(XMLReader readerInstance) {
      this.readerInstance = readerInstance;
      parentHandler = null;
   }

   public DelegatingHandler(DelegatingHandler parentHandler) {
      this.parentHandler = parentHandler;
      this.readerInstance = parentHandler.getReader();
   }

   protected XMLReader getReader() {
      return readerInstance;
   }

   public boolean hasParent() {
      return parentHandler != null;
   }

   public void delegateTo(ContentHandler handler) {
      readerInstance.setContentHandler(handler);
   }

   public ContentHandler releaseToParent() {
      delegateTo(parentHandler);
      return parentHandler;
   }
}
