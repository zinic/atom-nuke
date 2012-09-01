package org.atomnuke.atom.model.builder;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.atom.model.Author;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Contributor;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
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
public class FeedBuilder extends AtomConstructBuilderImpl<FeedBuilder, Feed, FeedImpl> {

   private List<Author> authors;
   private List<Contributor> contributors;
   private List<Category> categories;
   private List<Link> links;
   private List<Entry> entries;

   public FeedBuilder() {
      super(FeedBuilder.class, new FeedImpl());

      authors = new LinkedList<Author>();
      contributors = new LinkedList<Contributor>();
      categories = new LinkedList<Category>();
      links = new LinkedList<Link>();
      entries = new LinkedList<Entry>();

      construct().setAuthors(authors);
      construct().setContributors(contributors);
      construct().setCategories(categories);
      construct().setLinks(links);
      construct().setEntries(entries);
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
      construct().setGenerator(generator);
      return this;
   }

   public FeedBuilder setIcon(Icon icon) {
      construct().setIcon(icon);
      return this;
   }

   public FeedBuilder setId(Id id) {
      construct().setId(id);
      return this;
   }

   public FeedBuilder setLogo(Logo logo) {
      construct().setLogo(logo);
      return this;
   }

   public FeedBuilder setRights(Rights rights) {
      construct().setRights(rights);
      return this;
   }

   public FeedBuilder setSubtitle(Subtitle subtitle) {
      construct().setSubtitle(subtitle);
      return this;
   }

   public FeedBuilder setTitle(Title title) {
      construct().setTitle(title);
      return this;
   }

   public FeedBuilder setUpdated(Updated updated) {
      construct().setUpdated(updated);
      return this;
   }
}
