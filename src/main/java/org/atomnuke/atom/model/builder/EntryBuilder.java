package org.atomnuke.atom.model.builder;

import java.net.URI;
import java.util.LinkedList;
import org.atomnuke.atom.model.Author;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Content;
import org.atomnuke.atom.model.Contributor;
import org.atomnuke.atom.model.Id;
import org.atomnuke.atom.model.Link;
import org.atomnuke.atom.model.Published;
import org.atomnuke.atom.model.Rights;
import org.atomnuke.atom.model.Source;
import org.atomnuke.atom.model.Summary;
import org.atomnuke.atom.model.Title;
import org.atomnuke.atom.model.Updated;
import org.atomnuke.atom.model.impl.EntryImpl;

/**
 *
 * @author zinic
 */
public class EntryBuilder extends EntryImpl {

   public EntryBuilder() {
      authors = new LinkedList<Author>();
      contributors = new LinkedList<Contributor>();
      categories = new LinkedList<Category>();
      links = new LinkedList<Link>();
   }

   public EntryBuilder addAuthor(Author author) {
      authors.add(author);
      return this;
   }

   public EntryBuilder addContributor(Contributor contributor) {
      contributors.add(contributor);
      return this;
   }

   public EntryBuilder addCategory(Category category) {
      categories.add(category);
      return this;
   }

   public EntryBuilder addLink(Link link) {
      links.add(link);
      return this;
   }

   public EntryBuilder setId(Id id) {
      this.id = id;
      return this;
   }

   public EntryBuilder setRights(Rights rights) {
      this.rights = rights;
      return this;
   }

   public EntryBuilder setTitle(Title title) {
      this.title = title;
      return this;
   }

   public EntryBuilder setUpdated(Updated updated) {
      this.updated = updated;
      return this;
   }

   public EntryBuilder setContent(Content content) {
      this.content = content;
      return this;
   }

   public EntryBuilder setSummary(Summary summary) {
      this.summary = summary;
      return this;
   }

   public EntryBuilder setPublished(Published published) {
      this.published = published;
      return this;
   }

   public EntryBuilder setSource(Source source) {
      this.source = source;
      return this;
   }

   public EntryBuilder setBase(URI base) {
      this.base = base;
      return this;
   }

   public EntryBuilder setLang(String lang) {
      this.lang = lang;
      return this;
   }
}
