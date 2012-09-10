package org.atomnuke.atom.sax;

import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @deprecated org.atomnuke.atom.io replaces this package
 *
 * @author zinic
 */

@Deprecated
public abstract class DelegatingHandler extends DefaultHandler {

   private final DelegatingHandler parentHandler;
   private final XMLReader readerInstance;

   public DelegatingHandler(XMLReader readerInstance) {
      this(null, readerInstance);
   }

   public DelegatingHandler(DelegatingHandler parentHandler) {
      this(parentHandler, parentHandler.reader());
   }

   private DelegatingHandler(DelegatingHandler parentHandler, XMLReader readerInstance) {
      this.parentHandler = parentHandler;
      this.readerInstance = readerInstance;
   }

   protected XMLReader reader() {
      return readerInstance;
   }

   public boolean hasParent() {
      return parentHandler != null;
   }

   public void delegateTo(ContentHandler handler) {
      readerInstance.setContentHandler(handler);
   }

   public DelegatingHandler releaseToParent() {
      delegateTo(parentHandler);
      return parentHandler;
   }
}
