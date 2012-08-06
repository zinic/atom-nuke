package org.atomnuke.atom.model.builder;

import java.net.URI;
import org.atomnuke.atom.model.impl.LinkImpl;

/**
 *
 * @author zinic
 */
public class LinkBuilder extends LinkImpl {

   public LinkBuilder() {
   }

   public String getHref() {
      return href;
   }

   public String getRel() {
      return rel;
   }

   public String getType() {
      return type;
   }

   public String getHreflang() {
      return hreflang;
   }

   public String getTitle() {
      return title;
   }

   public Integer getLength() {
      return length;
   }

   public URI getBase() {
      return base;
   }

   public String getLang() {
      return lang;
   }

   public LinkBuilder setHref(String href) {
      this.href = href;
      return this;
   }

   public LinkBuilder setRel(String rel) {
      this.rel = rel;
      return this;
   }

   public LinkBuilder setHreflang(String hreflang) {
      this.hreflang = hreflang;
      return this;
   }

   public LinkBuilder setTitle(String title) {
      this.title = title;
      return this;
   }

   public LinkBuilder setLength(Integer length) {
      this.length = length;
      return this;
   }

   public LinkBuilder setType(String type) {
      this.type = type;
      return this;
   }

   public LinkBuilder setBase(URI base) {
      this.base = base;
      return this;
   }

   public LinkBuilder setLang(String lang) {
      this.lang = lang;
      return this;
   }
}
