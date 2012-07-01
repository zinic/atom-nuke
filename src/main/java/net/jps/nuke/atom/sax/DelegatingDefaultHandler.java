package net.jps.nuke.atom.sax;

import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author zinic
 */
public abstract class DelegatingDefaultHandler extends DefaultHandler {

   private final ContentHandler delegateHandler;
   private final XMLReader reader;

   public DelegatingDefaultHandler(ContentHandler delegateHandler, XMLReader reader) {
      this.delegateHandler = delegateHandler;
      this.reader = reader;
   }

   protected ContentHandler getDelegate() {
      return delegateHandler;
   }
   
   protected void releaseToDelegate() {
      reader.setContentHandler(delegateHandler);
   }
}
