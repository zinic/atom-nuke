package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.Link;

/**
 *
 * @author zinic
 */
public abstract class LinkImpl extends AtomCommonAttributesImpl implements Link {

   protected String href;
   protected String rel;
   protected String type;
   protected String hreflang;
   protected String title;
   protected Integer length;

   public String href() {
      return href;
   }

   public String rel() {
      return rel;
   }

   public String type() {
      return type;
   }

   public String hreflang() {
      return hreflang;
   }

   public String title() {
      return title;
   }

   public Integer length() {
      return length;
   }
}
