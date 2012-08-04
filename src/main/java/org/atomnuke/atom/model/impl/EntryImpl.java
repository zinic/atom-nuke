package org.atomnuke.atom.model.impl;

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

/**
 *
 * @author zinic
 */
public abstract class EntryImpl extends AtomCommonAttributesImpl implements Entry {

   protected List<Author> authors;
   protected List<Contributor> contributors;
   protected List<Category> categories;
   protected List<Link> links;
   protected Id id;
   protected Rights rights;
   protected Title title;
   protected Updated updated;
   protected Content content;
   protected Summary summary;
   protected Published published;
   protected Source source;

   public List<Author> authors() {
      return authors;
   }

   public List<Category> categories() {
      return categories;
   }

   public Content content() {
      return content;
   }

   public List<Contributor> contributors() {
      return contributors;
   }

   public Id id() {
      return id;
   }

   public List<Link> links() {
      return links;
   }

   public Published published() {
      return published;
   }

   public Rights rights() {
      return rights;
   }

   public Source source() {
      return source;
   }

   public Summary summary() {
      return summary;
   }

   public Title title() {
      return title;
   }

   public Updated updated() {
      return updated;
   }
}
