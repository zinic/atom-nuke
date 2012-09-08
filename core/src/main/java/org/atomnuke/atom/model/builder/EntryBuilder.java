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

import static org.atomnuke.util.CollectionUtil.*;

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

      setLists();
   }

   public EntryBuilder(Entry copyConstruct) {
      super(EntryBuilder.class, new EntryImpl(), copyConstruct);

      authors = copyAuthors(copyConstruct.authors());
      contributors = copyContributors(copyConstruct.contributors());
      categories = copyCategories(copyConstruct.categories());
      links = copyLinks(copyConstruct.links());

      if (copyConstruct.content() != null) {
         setContent(new ContentBuilder(copyConstruct.content()).build());
      }

      if (copyConstruct.id() != null) {
         setId(new IdBuilder(copyConstruct.id()).build());
      }

      if (copyConstruct.published() != null) {
         setPublished(new PublishedBuilder(copyConstruct.published()).build());
      }

      if (copyConstruct.rights() != null) {
         setRights(new RightsBuilder(copyConstruct.rights()).build());
      }

      if (copyConstruct.source() != null) {
         setSource(new SourceBuilder(copyConstruct.source()).build());
      }

      if (copyConstruct.summary() != null) {
         setSummary(new SummaryBuilder(copyConstruct.summary()).build());
      }

      if (copyConstruct.title() != null) {
         setTitle(new TitleBuilder(copyConstruct.title()).build());
      }

      if (copyConstruct.updated() != null) {
         setUpdated(new UpdatedBuilder(copyConstruct.updated()).build());
      }

      setLists();
   }

   private void setLists() {
      construct().setAuthors(authors);
      construct().setContributors(contributors);
      construct().setCategories(categories);
      construct().setLinks(links);
   }

   public final EntryBuilder addAuthor(Author author) {
      authors.add(author);
      return this;
   }

   public final EntryBuilder addContributor(Contributor contributor) {
      contributors.add(contributor);
      return this;
   }

   public final EntryBuilder addCategory(Category category) {
      categories.add(category);
      return this;
   }

   public final EntryBuilder addLink(Link link) {
      links.add(link);
      return this;
   }

   public final EntryBuilder setId(Id id) {
      construct().setId(id);
      return this;
   }

   public final EntryBuilder setRights(Rights rights) {
      construct().setRights(rights);
      return this;
   }

   public final EntryBuilder setTitle(Title title) {
      construct().setTitle(title);
      return this;
   }

   public final EntryBuilder setUpdated(Updated updated) {
      construct().setUpdated(updated);
      return this;
   }

   public final EntryBuilder setContent(Content content) {
      construct().setContent(content);
      return this;
   }

   public final EntryBuilder setSummary(Summary summary) {
      construct().setSummary(summary);
      return this;
   }

   public final EntryBuilder setPublished(Published published) {
      construct().setPublished(published);
      return this;
   }

   public final EntryBuilder setSource(Source source) {
      construct().setSource(source);
      return this;
   }
}
