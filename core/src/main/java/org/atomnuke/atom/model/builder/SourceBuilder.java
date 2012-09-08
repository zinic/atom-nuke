package org.atomnuke.atom.model.builder;

import java.util.LinkedList;
import java.util.List;
import org.atomnuke.atom.model.Author;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Generator;
import org.atomnuke.atom.model.Id;
import org.atomnuke.atom.model.Icon;
import org.atomnuke.atom.model.Link;
import org.atomnuke.atom.model.Logo;
import org.atomnuke.atom.model.Rights;
import org.atomnuke.atom.model.Source;
import org.atomnuke.atom.model.Subtitle;
import org.atomnuke.atom.model.Title;
import org.atomnuke.atom.model.Updated;
import org.atomnuke.atom.model.impl.SourceImpl;
import static org.atomnuke.util.CollectionUtil.*;

/**
 *
 * @author zinic
 */
public class SourceBuilder extends AtomConstructBuilderImpl<SourceBuilder, Source, SourceImpl> {

   private final List<Author> authors;
   private final List<Category> categories;
   private final List<Link> links;

   public SourceBuilder() {
      super(SourceBuilder.class, new SourceImpl());

      authors = new LinkedList<Author>();
      categories = new LinkedList<Category>();
      links = new LinkedList<Link>();

      setLists();
   }

   public SourceBuilder(Source copyConstruct) {
      super(SourceBuilder.class, new SourceImpl(), copyConstruct);

      authors = copyAuthors(copyConstruct.authors());
      categories = copyCategories(copyConstruct.categories());
      links = copyLinks(copyConstruct.links());

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
      construct().setCategories(categories);
      construct().setLinks(links);
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

   public final SourceBuilder setGenerator(Generator generator) {
      construct().setGenerator(generator);
      return this;
   }

   public final SourceBuilder setIcon(Icon icon) {
      construct().setIcon(icon);
      return this;
   }

   public final SourceBuilder setId(Id id) {
      construct().setId(id);
      return this;
   }

   public final SourceBuilder setLogo(Logo logo) {
      construct().setLogo(logo);
      return this;
   }

   public final SourceBuilder setRights(Rights rights) {
      construct().setRights(rights);
      return this;
   }

   public final SourceBuilder setSubtitle(Subtitle subtitle) {
      construct().setSubtitle(subtitle);
      return this;
   }

   public final SourceBuilder setTitle(Title title) {
      construct().setTitle(title);
      return this;
   }

   public final SourceBuilder setUpdated(Updated updated) {
      construct().setUpdated(updated);
      return this;
   }
}
