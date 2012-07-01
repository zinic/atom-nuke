package net.jps.nuke.atom.model.builder;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Generator;
import net.jps.nuke.atom.model.ID;
import net.jps.nuke.atom.model.Icon;
import net.jps.nuke.atom.model.Link;
import net.jps.nuke.atom.model.Logo;
import net.jps.nuke.atom.model.Rights;
import net.jps.nuke.atom.model.Subtitle;
import net.jps.nuke.atom.model.Title;
import net.jps.nuke.atom.model.Updated;
import net.jps.nuke.atom.model.impl.SourceImpl;

/**
 *
 * @author zinic
 */
public class SourceBuilder extends SourceImpl {

   public static SourceBuilder newBuilder() {
      final SourceBuilder builder = new SourceBuilder();

      builder.authors = new LinkedList<Author>();
      builder.categories = new LinkedList<Category>();
      builder.links = new LinkedList<Link>();

      return builder;
   }

   protected SourceBuilder() {
   }

   public SourceBuilder build() {
      authors = Collections.unmodifiableList(authors);
      categories = Collections.unmodifiableList(categories);
      links = Collections.unmodifiableList(links);

      return this;
   }

   public void addAuthor(Author author) {
      authors.add(author);
   }

   public void addCategory(Category category) {
      categories.add(category);
   }

   public void addLink(Link link) {
      links.add(link);
   }

   public void setGenerator(Generator generator) {
      this.generator = generator;
   }

   public void setIcon(Icon icon) {
      this.icon = icon;
   }

   public void setId(ID id) {
      this.id = id;
   }

   public void setLogo(Logo logo) {
      this.logo = logo;
   }

   public void setRights(Rights rights) {
      this.rights = rights;
   }

   public void setSubtitle(Subtitle subtitle) {
      this.subtitle = subtitle;
   }

   public void setTitle(Title title) {
      this.title = title;
   }

   public void setUpdated(Updated updated) {
      this.updated = updated;
   }

   public void setBase(URI base) {
      this.base = base;
   }

   public void setLang(String lang) {
      this.lang = lang;
   }
}
