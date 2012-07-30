package net.jps.nuke.atom.model.builder;

import java.net.URI;
import java.util.LinkedList;
import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Content;
import net.jps.nuke.atom.model.Contributor;
import net.jps.nuke.atom.model.ID;
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

   public void addAuthor(Author author) {
      authors.add(author);
   }

   public void addContributor(Contributor contributor) {
      contributors.add(contributor);
   }

   public void addCategory(Category category) {
      categories.add(category);
   }

   public void addLink(Link link) {
      links.add(link);
   }

   public void setId(ID id) {
      this.id = id;
   }

   public void setRights(Rights rights) {
      this.rights = rights;
   }

   public void setTitle(Title title) {
      this.title = title;
   }

   public void setUpdated(Updated updated) {
      this.updated = updated;
   }

   public void setContent(Content content) {
      this.content = content;
   }

   public void setSummary(Summary summary) {
      this.summary = summary;
   }

   public void setPublished(Published published) {
      this.published = published;
   }

   public void setSource(Source source) {
      this.source = source;
   }

   public void setBase(URI base) {
      this.base = base;
   }

   public void setLang(String lang) {
      this.lang = lang;
   }
}
