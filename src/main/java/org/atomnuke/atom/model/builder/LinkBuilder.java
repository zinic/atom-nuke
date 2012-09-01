package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.builder.*;
import java.net.URI;
import org.atomnuke.atom.model.AtomCommonAtributes;
import org.atomnuke.atom.model.Link;
import org.atomnuke.atom.model.impl.AtomCommonAttributesImpl;
import org.atomnuke.atom.model.impl.LinkImpl;

/**
 *
 * @author zinic
 */
public class LinkBuilder extends AtomConstructBuilderImpl<LinkBuilder, Link, LinkImpl> {

   public LinkBuilder() {
      super(LinkBuilder.class, new LinkImpl());
   }

   public LinkBuilder setHref(String href) {
      construct().setHref(href);
      return this;
   }

   public LinkBuilder setRel(String rel) {
      construct().setRel(rel);
      return this;
   }

   public LinkBuilder setHreflang(String hreflang) {
      construct().setHreflang(hreflang);
      return this;
   }

   public LinkBuilder setTitle(String title) {
      construct().setTitle(title);
      return this;
   }

   public LinkBuilder setLength(Integer length) {
      construct().setLength(length);
      return this;
   }

   public LinkBuilder setType(String type) {
      construct().setType(type);
      return this;
   }
}
