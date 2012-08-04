package org.atomnuke.atom.model.builder;

import java.net.URI;
import java.util.LinkedList;
import org.atomnuke.atom.model.Author;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Contributor;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Generator;
import org.atomnuke.atom.model.Id;
import org.atomnuke.atom.model.Icon;
import org.atomnuke.atom.model.Link;
import org.atomnuke.atom.model.Logo;
import org.atomnuke.atom.model.Rights;
import org.atomnuke.atom.model.Subtitle;
import org.atomnuke.atom.model.Title;
import org.atomnuke.atom.model.Updated;
import org.atomnuke.atom.model.impl.FeedImpl;

/**
 *
 * @author zinic
 */
public class FeedBuilder extends FeedImpl {

   public FeedBuilder() {
      authors = new LinkedList<Author>();
      contributors = new LinkedList<Contributor>();
      categories = new LinkedList<Category>();
      links = new LinkedList<Link>();
      entries = new LinkedList<Entry>();
   }

   public FeedBuilder addAuthor(Author author) {
      authors.add(author);
      return this;
   }

   public FeedBuilder addContributor(Contributor contributor) {
      contributors.add(contributor);
      return this;
   }

   public FeedBuilder addCategory(Category category) {
      categories.add(category);
      return this;
   }

   public FeedBuilder addLink(Link link) {
      links.add(link);
      return this;
   }

   public FeedBuilder addEntry(Entry entry) {
      entries.add(entry);
      return this;
   }

   public FeedBuilder setGenerator(Generator generator) {
      this.generator = generator;
      return this;
   }

   public FeedBuilder setIcon(Icon icon) {
      this.icon = icon;
      return this;
   }

   public FeedBuilder setId(Id id) {
      this.id = id;
      return this;
   }

   public FeedBuilder setLogo(Logo logo) {
      this.logo = logo;
      return this;
   }

   public FeedBuilder setRights(Rights rights) {
      this.rights = rights;
      return this;
   }

   public FeedBuilder setSubtitle(Subtitle subtitle) {
      this.subtitle = subtitle;
      return this;
   }

   public FeedBuilder setTitle(Title title) {
      this.title = title;
      return this;
   }

   public FeedBuilder setUpdated(Updated updated) {
      this.updated = updated;
      return this;
   }

   public FeedBuilder setBase(URI base) {
      this.base = base;
      return this;
   }

   public FeedBuilder setLang(String lang) {
      this.lang = lang;
      return this;
   }
}
