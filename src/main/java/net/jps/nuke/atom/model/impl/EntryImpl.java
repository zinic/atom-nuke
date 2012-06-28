package net.jps.nuke.atom.model.impl;

import java.util.List;
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

/**
 *
 * @author zinic
 */
public abstract class EntryImpl extends AtomCommonAttributesImpl implements Entry {

   protected List<Author> authors;
   protected List<Contributor> contributors;
   protected List<Category> categories;
   protected List<Link> links;
   protected List<Entry> entries;
   protected ID id;
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

   public ID id() {
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
