package org.atomnuke.atom.io.reader.impl.sax;

import org.atomnuke.atom.xml.AtomElement;

/**
 *
 * @author zinic
 */
public class HandlerContext<T> {

   private final AtomElement elementDef;
   private final T builder;

   public HandlerContext(AtomElement elementDef, T builder) {
      this.elementDef = elementDef;
      this.builder = builder;
   }

   public AtomElement getElementDef() {
      return elementDef;
   }

   public T builder() {
      return builder;
   }
}
