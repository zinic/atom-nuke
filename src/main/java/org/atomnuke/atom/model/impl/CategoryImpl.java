package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.Category;

/**
 *
 * @author zinic
 */
public class CategoryImpl extends AtomCommonAttributesImpl implements Category {

   private String term, scheme, label;

   public void setTerm(String term) {
      this.term = term;
   }

   public void setScheme(String scheme) {
      this.scheme = scheme;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   @Override
   public String term() {
      return term;
   }

   @Override
   public String scheme() {
      return scheme;
   }

   @Override
   public String label() {
      return label;
   }
}
