package net.jps.nuke.atom.sax.attribute;

import org.xml.sax.Attributes;

/**
 *
 * @author zinic
 */
public class AttributeScannerDriver {
   
   private final Attributes attributes;
   
   public AttributeScannerDriver(Attributes attributes) {
      this.attributes = attributes;
   }
   
   public void scan(AttributeScanner scanner) {
      for (int attributeIndex = 0; attributeIndex < attributes.getLength(); attributeIndex++) {
         scanner.attribute(attributes.getLocalName(attributeIndex), attributes.getQName(attributeIndex), attributes.getValue(attributeIndex));
      }
   }
}
