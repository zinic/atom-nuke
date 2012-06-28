package net.jps.nuke.atom.model.builder;

import java.util.Collections;
import java.util.LinkedList;
import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Content;
import net.jps.nuke.atom.model.Contributor;
import net.jps.nuke.atom.model.Entry;
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

   public EntryBuilder newBuilder() {
      final EntryBuilder builder = new EntryBuilder();

      builder.authors = new LinkedList<Author>();
      builder.contributors = new LinkedList<Contributor>();
      builder.categories = new LinkedList<Category>();
      builder.links = new LinkedList<Link>();
      builder.entries = new LinkedList<Entry>();

      return builder;
   }

   protected EntryBuilder() {
   }

   public Entry build() {
      authors = Collections.unmodifiableList(authors);
      contributors = Collections.unmodifiableList(contributors);
      categories = Collections.unmodifiableList(categories);
      links = Collections.unmodifiableList(links);
      entries = Collections.unmodifiableList(entries);


      return this;
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

   public void addEntry(Entry entry) {
      entries.add(entry);
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
}
