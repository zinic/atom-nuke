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

   public SourceBuilder setGenerator(Generator generator) {
      construct().setGenerator(generator);
      return this;
   }

   public SourceBuilder setIcon(Icon icon) {
      construct().setIcon(icon);
      return this;
   }

   public SourceBuilder setId(Id id) {
      construct().setId(id);
      return this;
   }

   public SourceBuilder setLogo(Logo logo) {
      construct().setLogo(logo);
      return this;
   }

   public SourceBuilder setRights(Rights rights) {
      construct().setRights(rights);
      return this;
   }

   public SourceBuilder setSubtitle(Subtitle subtitle) {
      construct().setSubtitle(subtitle);
      return this;
   }

   public SourceBuilder setTitle(Title title) {
      construct().setTitle(title);
      return this;
   }

   public SourceBuilder setUpdated(Updated updated) {
      construct().setUpdated(updated);
      return this;
   }
}
