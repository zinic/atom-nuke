package org.atomnuke.atom.xml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author zinic
 */
public final class AtomNamespaceContext implements NamespaceContext {

   public static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";
   public static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";
   public static final String XML_PREFIX = "xml";
   public static final String ATOM_PREFIX = "atom";
   private static final AtomNamespaceContext INSTANCE = new AtomNamespaceContext();

   public static NamespaceContext instance() {
      return INSTANCE;
   }
   private final Map<String, String> uriToPrefixMap;
   private final Map<String, String> prefixToUriMap;

   private AtomNamespaceContext() {
      uriToPrefixMap = new HashMap<String, String>();
      prefixToUriMap = new HashMap<String, String>();

      uriToPrefixMap.put(ATOM_NAMESPACE, ATOM_PREFIX);
      prefixToUriMap.put(ATOM_PREFIX, ATOM_NAMESPACE);

      uriToPrefixMap.put(XML_NAMESPACE, XML_PREFIX);
      prefixToUriMap.put(XML_PREFIX, XML_NAMESPACE);
   }

   public String getNamespaceURI(String prefix) {
      return prefixToUriMap.get(prefix);
   }

   public String getPrefix(String namespaceURI) {
      return uriToPrefixMap.get(namespaceURI);
   }

   public Iterator getPrefixes(String namespaceURI) {
      return Collections.EMPTY_LIST.iterator();
   }
}
