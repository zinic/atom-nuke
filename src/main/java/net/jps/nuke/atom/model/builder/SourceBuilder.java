package net.jps.nuke.atom.model.builder;

import java.net.URI;
import java.util.LinkedList;
import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Generator;
import net.jps.nuke.atom.model.Id;
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

   public SourceBuilder() {
      authors = new LinkedList<Author>();
      categories = new LinkedList<Category>();
      links = new LinkedList<Link>();
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
      this.generator = generator;
      return this;
   }

   public SourceBuilder setIcon(Icon icon) {
      this.icon = icon;
      return this;
   }

   public SourceBuilder setId(Id id) {
      this.id = id;
      return this;
   }

   public SourceBuilder setLogo(Logo logo) {
      this.logo = logo;
      return this;
   }

   public SourceBuilder setRights(Rights rights) {
      this.rights = rights;
      return this;
   }

   public SourceBuilder setSubtitle(Subtitle subtitle) {
      this.subtitle = subtitle;
      return this;
   }

   public SourceBuilder setTitle(Title title) {
      this.title = title;
      return this;
   }

   public SourceBuilder setUpdated(Updated updated) {
      this.updated = updated;
      return this;
   }

   public SourceBuilder setBase(URI base) {
      this.base = base;
      return this;
   }

   public SourceBuilder setLang(String lang) {
      this.lang = lang;
      return this;
   }
}
