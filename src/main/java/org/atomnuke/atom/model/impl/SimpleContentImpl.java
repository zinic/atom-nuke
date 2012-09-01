package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.SimpleContent;

/**
 *
 * @author zinic
 */
public class SimpleContentImpl extends AtomCommonAttributesImpl implements SimpleContent {

   private String value;

   public void setValue(String value) {
      this.value = value;
   }

   @Override
   public String toString() {
      return value;
   }
}
