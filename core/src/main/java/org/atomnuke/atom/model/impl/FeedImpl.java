package org.atomnuke.atom.model.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
import org.atomnuke.util.CollectionUtil;

import static org.atomnuke.util.CollectionUtil.*;

/**
 *
 * @author zinic
 */
public class FeedImpl extends AtomCommonAttributesImpl implements Feed {

   private List<Author> authors;
   private List<Contributor> contributors;
   private List<Category> categories;
   private List<Link> links;
   private List<Entry> entries;
   private Generator generator;
   private Icon icon;
   private Id id;
   private Logo logo;
   private Rights rights;
   private Subtitle subtitle;
   private Title title;
   private Updated updated;

   public void setAuthors(List<Author> authors) {
      this.authors = authors;
   }

   public void setContributors(List<Contributor> contributors) {
      this.contributors = contributors;
   }

   public void setCategories(List<Category> categories) {
      this.categories = categories;
   }

   public void setLinks(List<Link> links) {
      this.links = links;
   }

   public void setEntries(List<Entry> entries) {
      this.entries = entries;
   }

   public void setGenerator(Generator generator) {
      this.generator = generator;
   }

   public void setIcon(Icon icon) {
      this.icon = icon;
   }

   public void setId(Id id) {
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

   @Override
   public List<Author> authors() {
      return authors;
   }

   @Override
   public List<Contributor> contributors() {
      return contributors;
   }

   @Override
   public List<Category> categories() {
      return categories;
   }

   @Override
   public List<Link> links() {
      return links;
   }

   @Override
   public List<Entry> entries() {
      return entries;
   }

   @Override
   public Generator generator() {
      return generator;
   }

   @Override
   public Icon icon() {
      return icon;
   }

   @Override
   public Id id() {
      return id;
   }

   @Override
   public Logo logo() {
      return logo;
   }

   @Override
   public Rights rights() {
      return rights;
   }

   @Override
   public Subtitle subtitle() {
      return subtitle;
   }

   @Override
   public Title title() {
      return title;
   }

   @Override
   public Updated updated() {
      return updated;
   }

   @Override
   public int hashCode() {
      final AtomicInteger hashCode = new AtomicInteger(929);

      hashCode.addAndGet(599 * hashCode.get() + (this.id != null ? this.id.hashCode() : 0));
      hashCode.addAndGet(599 * hashCode.get() + (this.rights != null ? this.rights.hashCode() : 0));
      hashCode.addAndGet(599 * hashCode.get() + (this.title != null ? this.title.hashCode() : 0));
      hashCode.addAndGet(599 * hashCode.get() + (this.updated != null ? this.updated.hashCode() : 0));
      hashCode.addAndGet(599 * hashCode.get() + (this.generator != null ? this.generator.hashCode() : 0));
      hashCode.addAndGet(599 * hashCode.get() + (this.icon != null ? this.icon.hashCode() : 0));
      hashCode.addAndGet(599 * hashCode.get() + (this.subtitle != null ? this.subtitle.hashCode() : 0));
      hashCode.addAndGet(599 * hashCode.get() + (this.logo != null ? this.logo.hashCode() : 0));

      forCollection(authors).each(new Delegate<Author>() {
         @Override
         public void element(Author element) {
            hashCode.addAndGet(element.hashCode());
         }
      });

      forCollection(categories).each(new Delegate<Category>() {
         @Override
         public void element(Category element) {
            hashCode.addAndGet(element.hashCode());
         }
      });

      forCollection(contributors).each(new Delegate<Contributor>() {
         @Override
         public void element(Contributor element) {
            hashCode.addAndGet(element.hashCode());
         }
      });

      forCollection(links).each(new Delegate<Link>() {
         @Override
         public void element(Link element) {
            hashCode.addAndGet(element.hashCode());
         }
      });

      forCollection(entries).each(new Delegate<Entry>() {
         @Override
         public void element(Entry element) {
            hashCode.addAndGet(element.hashCode());
         }
      });

      return hashCode.get() + super.hashCode();
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }

      return hashCode() == obj.hashCode();
   }
}
