package net.jps.nuke.atom.model.builder;

import java.net.URI;
import java.util.LinkedList;
import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Content;
import net.jps.nuke.atom.model.Contributor;
import net.jps.nuke.atom.model.Id;
import net.jps.nuke.atom.model.Link;
import net.jps.nuke.atom.model.Published;
import net.jps.nuke.atom.model.Rights;
import net.jps.nuke.atom.model.Source;
import net.jps.nuke.atom.model.Summary;
import net.jps.nuke.atom.model.Title;
import net.jps.nuke.atom.model.Updated;
import net.jps.nuke.atom.model.impl.EntryImpl;

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
