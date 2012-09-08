package org.atomnuke.atom.model.builder;

import java.util.LinkedList;
import java.util.List;
import org.atomnuke.atom.model.Author;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Contributor;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.atom.model.Generator;
import org.atomnuke.atom.model.Icon;
import org.atomnuke.atom.model.Id;
import org.atomnuke.atom.model.Link;
import org.atomnuke.atom.model.Logo;
import org.atomnuke.atom.model.Rights;
import org.atomnuke.atom.model.Subtitle;
import org.atomnuke.atom.model.Title;
import org.atomnuke.atom.model.Updated;

import static org.atomnuke.util.CollectionUtil.*;

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

      setLists();
   }

   public FeedBuilder(Feed copyConstruct) {
      super(FeedBuilder.class, new FeedImpl(), copyConstruct);

      authors = copyAuthors(copyConstruct.authors());
      contributors = copyContributors(copyConstruct.contributors());
      categories = copyCategories(copyConstruct.categories());
      links = copyLinks(copyConstruct.links());
      entries = copyEntries(copyConstruct.entries());

      if (copyConstruct != null) {
         if (copyConstruct.generator() != null) {
            setGenerator(new GeneratorBuilder(copyConstruct.generator()).build());
         }

         if (copyConstruct.icon() != null) {
            setIcon(new IconBuilder(copyConstruct.icon()).build());
         }

         if (copyConstruct.id() != null) {
            setId(new IdBuilder(copyConstruct.id()).build());
         }

         if (copyConstruct.logo() != null) {
            setLogo(new LogoBuilder(copyConstruct.logo()).build());
         }

         if (copyConstruct.rights() != null) {
            setRights(new RightsBuilder(copyConstruct.rights()).build());
         }

         if (copyConstruct.subtitle() != null) {
            setSubtitle(new SubtitleBuilder(copyConstruct.subtitle()).build());
         }

         if (copyConstruct.title() != null) {
            setTitle(new TitleBuilder(copyConstruct.title()).build());
         }

         if (copyConstruct.updated() != null) {
            setUpdated(new UpdatedBuilder(copyConstruct.updated()).build());
         }
      }

      setLists();
   }

   private void setLists() {
      construct().setAuthors(authors);
      construct().setContributors(contributors);
      construct().setCategories(categories);
      construct().setLinks(links);
      construct().setEntries(entries);
   }

   public final FeedBuilder addAuthor(Author author) {
      authors.add(author);
      return this;
   }

   public final FeedBuilder addContributor(Contributor contributor) {
      contributors.add(contributor);
      return this;
   }

   public final FeedBuilder addCategory(Category category) {
      categories.add(category);
      return this;
   }

   public final FeedBuilder addLink(Link link) {
      links.add(link);
      return this;
   }

   public final FeedBuilder addEntry(Entry entry) {
      entries.add(entry);
      return this;
   }

   public final FeedBuilder setGenerator(Generator generator) {
      construct().setGenerator(generator);
      return this;
   }

   public final FeedBuilder setIcon(Icon icon) {
      construct().setIcon(icon);
      return this;
   }

   public final FeedBuilder setId(Id id) {
      construct().setId(id);
      return this;
   }

   public final FeedBuilder setLogo(Logo logo) {
      construct().setLogo(logo);
      return this;
   }

   public final FeedBuilder setRights(Rights rights) {
      construct().setRights(rights);
      return this;
   }

   public final FeedBuilder setSubtitle(Subtitle subtitle) {
      construct().setSubtitle(subtitle);
      return this;
   }

   public final FeedBuilder setTitle(Title title) {
      construct().setTitle(title);
      return this;
   }

   public final FeedBuilder setUpdated(Updated updated) {
      construct().setUpdated(updated);
      return this;
   }
}
