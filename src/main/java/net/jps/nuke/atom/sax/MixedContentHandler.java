package net.jps.nuke.atom.sax;

import org.apache.commons.lang3.StringEscapeUtils;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 *
 * @author zinic
 */
public class MixedContentHandler extends DelegatingDefaultHandler {

   private final StringBuilder contentBuilder;
   private int depth;

   public MixedContentHandler(StringBuilder contentBuilder, ContentHandler delegateHandler, XMLReader reader) {
      super(delegateHandler, reader);

      this.contentBuilder = contentBuilder;
      depth = 0;
   }

   public StringBuilder getContentBuilder() {
      return contentBuilder;
   }

   private static String asFullName(String qName, String localName) {
      final StringBuilder name = new StringBuilder();

      if (qName != null && !"".equals(qName)) {
         name.append(qName);
      }

      if (localName != null && !"".equals(localName)) {
         if (name.length() > 0) {
            name.append(":");
         }

         name.append(localName);
      }

      return name.toString();
   }

   @Override
   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      contentBuilder.append("<");
      contentBuilder.append(asFullName(qName, localName));

      new AttributeScannerDriver(attributes).scan(new AttributeScanner() {
         public void attribute(String attributeLocalName, String attributeQName, String attributeValue) {
            contentBuilder.append(" ");
            contentBuilder.append(asFullName(attributeQName, attributeLocalName));
            contentBuilder.append('"');
            contentBuilder.append(StringEscapeUtils.escapeXml(attributeValue));
            contentBuilder.append('"');
         }
      });

      contentBuilder.append(">");
      depth++;
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      if (--depth <= 0) {
         getDelegate().endElement(uri, localName, qName);
         releaseToDelegate();
      } else {
         contentBuilder.append("</");
         contentBuilder.append(asFullName(qName, localName));
         contentBuilder.append(">");
      }
   }

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      contentBuilder.append(new String(ch, start, length).trim());
   }
}
