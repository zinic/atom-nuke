package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Link;
import org.atomnuke.atom.model.impl.LinkImpl;

/**
 *
 * @author zinic
 */
public class LinkBuilder extends AtomConstructBuilderImpl<LinkBuilder, Link, LinkImpl> {

   public LinkBuilder() {
      super(LinkBuilder.class, new LinkImpl());
   }

   public LinkBuilder(Link copyConstruct) {
      super(LinkBuilder.class, new LinkImpl(), copyConstruct);

      setHref(copyConstruct.href());
      setHreflang(copyConstruct.hreflang());
      setLength(copyConstruct.length());
      setRel(copyConstruct.rel());
      setTitle(copyConstruct.title());
      setType(copyConstruct.type());
   }

   public final LinkBuilder setHref(String href) {
      construct().setHref(href);
      return this;
   }

   public final LinkBuilder setRel(String rel) {
      construct().setRel(rel);
      return this;
   }

   public final LinkBuilder setHreflang(String hreflang) {
      construct().setHreflang(hreflang);
      return this;
   }

   public final LinkBuilder setTitle(String title) {
      construct().setTitle(title);
      return this;
   }

   public final LinkBuilder setLength(Integer length) {
      construct().setLength(length);
      return this;
   }

   public final LinkBuilder setType(String type) {
      construct().setType(type);
      return this;
   }
}
