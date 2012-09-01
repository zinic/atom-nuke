package org.atomnuke.atom.model.builder;

import java.util.LinkedList;
import java.util.List;
import org.atomnuke.atom.model.Author;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Content;
import org.atomnuke.atom.model.Contributor;
import org.atomnuke.atom.model.Entry;
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
public class EntryBuilder extends AtomConstructBuilderImpl<EntryBuilder, Entry, EntryImpl> {

   private List<Author> authors;
   private List<Contributor> contributors;
   private List<Category> categories;
   private List<Link> links;

   public EntryBuilder() {
      super(EntryBuilder.class, new EntryImpl());

      authors = new LinkedList<Author>();
      contributors = new LinkedList<Contributor>();
      categories = new LinkedList<Category>();
      links = new LinkedList<Link>();

      construct().setAuthors(authors);
      construct().setContributors(contributors);
      construct().setCategories(categories);
      construct().setLinks(links);
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
      construct().setId(id);
      return this;
   }

   public EntryBuilder setRights(Rights rights) {
      construct().setRights(rights);
      return this;
   }

   public EntryBuilder setTitle(Title title) {
      construct().setTitle(title);
      return this;
   }

   public EntryBuilder setUpdated(Updated updated) {
      construct().setUpdated(updated);
      return this;
   }

   public EntryBuilder setContent(Content content) {
      construct().setContent(content);
      return this;
   }

   public EntryBuilder setSummary(Summary summary) {
      construct().setSummary(summary);
      return this;
   }

   public EntryBuilder setPublished(Published published) {
      construct().setPublished(published);
      return this;
   }

   public EntryBuilder setSource(Source source) {
      construct().setSource(source);
      return this;
   }
}
