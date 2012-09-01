package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.Link;

/**
 *
 * @author zinic
 */
public class LinkImpl extends AtomCommonAttributesImpl implements Link {

   private String href, rel, type, hreflang, title;
   private Integer length;

   public void setHref(String href) {
      this.href = href;
   }

   public void setRel(String rel) {
      this.rel = rel;
   }

   public void setType(String type) {
      this.type = type;
   }

   public void setHreflang(String hreflang) {
      this.hreflang = hreflang;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void setLength(Integer length) {
      this.length = length;
   }

   @Override
   public String href() {
      return href;
   }

   @Override
   public String rel() {
      return rel;
   }

   @Override
   public String type() {
      return type;
   }

   @Override
   public String hreflang() {
      return hreflang;
   }

   @Override
   public String title() {
      return title;
   }

   @Override
   public Integer length() {
      return length;
   }
}
