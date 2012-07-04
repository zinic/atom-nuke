package net.jps.nuke.atom.sax;

import org.apache.commons.lang3.StringEscapeUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * TODO: Ugh... make this pretty...
 *
 * @author zinic
 */
public class MixedContentHandler extends DelegatingHandler {

   private final StringBuilder contentBuilder;
   private int depth;

   public MixedContentHandler(StringBuilder contentBuilder, DelegatingHandler parent) {
      super(parent);

      this.contentBuilder = contentBuilder;
      depth = 0;
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

      for (int i = 0; i < attributes.getLength(); i++) {
         contentBuilder.append(" ");
         contentBuilder.append(asFullName(attributes.getQName(i), attributes.getLocalName(i)));
         contentBuilder.append('"');
         contentBuilder.append(StringEscapeUtils.escapeXml(attributes.getValue(i)));
         contentBuilder.append('"');
      }

      contentBuilder.append(">");
      depth++;
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException {
      if (depth > 0) {
         contentBuilder.append("</");
         contentBuilder.append(asFullName(qName, localName));
         contentBuilder.append(">");
      }

      if (--depth < 0) {
         releaseToParent().endElement(uri, localName, qName);
      }
   }

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException {
      contentBuilder.append(new String(ch, start, length).trim());
   }
}
