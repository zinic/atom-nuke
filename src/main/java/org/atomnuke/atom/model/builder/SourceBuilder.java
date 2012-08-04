package org.atomnuke.atom.model.builder;

import java.net.URI;
import java.util.LinkedList;
import org.atomnuke.atom.model.Author;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Generator;
import org.atomnuke.atom.model.Id;
import org.atomnuke.atom.model.Icon;
import org.atomnuke.atom.model.Link;
import org.atomnuke.atom.model.Logo;
import org.atomnuke.atom.model.Rights;
import org.atomnuke.atom.model.Subtitle;
import org.atomnuke.atom.model.Title;
import org.atomnuke.atom.model.Updated;
import org.atomnuke.atom.model.impl.SourceImpl;

/**
 *
 * @author zinic
 */
public class SourceBuilder extends SourceImpl {

   public SourceBuilder() {
      authors = new LinkedList<Author>();
      categories = new LinkedList<Category>();
      links = new LinkedList<Link>();
   }

   public SourceBuilder addAuthor(Author author) {
      authors.add(author);
      return this;
   }

   public SourceBuilder addCategory(Category category) {
      categories.add(category);
      return this;
   }

   public SourceBuilder addLink(Link link) {
      links.add(link);
      return this;
   }

   public SourceBuilder setGenerator(Generator generator) {
      this.generator = generator;
      return this;
   }

   public SourceBuilder setIcon(Icon icon) {
      this.icon = icon;
      return this;
   }

   public SourceBuilder setId(Id id) {
      this.id = id;
      return this;
   }

   public SourceBuilder setLogo(Logo logo) {
      this.logo = logo;
      return this;
   }

   public SourceBuilder setRights(Rights rights) {
      this.rights = rights;
      return this;
   }

   public SourceBuilder setSubtitle(Subtitle subtitle) {
      this.subtitle = subtitle;
      return this;
   }

   public SourceBuilder setTitle(Title title) {
      this.title = title;
      return this;
   }

   public SourceBuilder setUpdated(Updated updated) {
      this.updated = updated;
      return this;
   }

   public SourceBuilder setBase(URI base) {
      this.base = base;
      return this;
   }

   public SourceBuilder setLang(String lang) {
      this.lang = lang;
      return this;
   }
}
