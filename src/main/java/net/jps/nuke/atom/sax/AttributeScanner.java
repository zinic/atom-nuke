package net.jps.nuke.atom.sax;

/**
 *
 * @author zinic
 */
public interface AttributeScanner {

   void attribute(String localName, String qname, String value);
}
