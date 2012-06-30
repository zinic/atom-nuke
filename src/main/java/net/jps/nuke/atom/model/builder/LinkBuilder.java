package net.jps.nuke.atom.model.builder;

import java.net.URI;
import net.jps.nuke.atom.model.impl.LinkImpl;

/**
 *
 * @author zinic
 */
public class LinkBuilder extends LinkImpl {

   public static LinkBuilder newBuilder() {
      return new LinkBuilder();
   }

   protected LinkBuilder() {
   }

   public LinkBuilder build() {
      return this;
   }

   public void setHref(String href) {
      this.href = href;
   }

   public void setRel(String rel) {
      this.rel = rel;
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

   public void setType(String type) {
      this.type = type;
   }

   public void setBase(URI base) {
      this.base = base;
   }

   public void setLang(String lang) {
      this.lang = lang;
   }
}
