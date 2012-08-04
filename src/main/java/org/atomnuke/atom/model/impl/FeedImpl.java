package org.atomnuke.atom.model.impl;

import java.util.List;
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

/**
 *
 * @author zinic
 */
public abstract class FeedImpl extends AtomCommonAttributesImpl implements Feed {

   protected List<Author> authors;
   protected List<Contributor> contributors;
   protected List<Category> categories;
   protected List<Link> links;
   protected List<Entry> entries;
   protected Generator generator;
   protected Icon icon;
   protected Id id;
   protected Logo logo;
   protected Rights rights;
   protected Subtitle subtitle;
   protected Title title;
   protected Updated updated;

   public List<Author> authors() {
      return authors;
   }

   public List<Contributor> contributors() {
      return contributors;
   }

   public List<Category> categories() {
      return categories;
   }

   public List<Link> links() {
      return links;
   }

   public List<Entry> entries() {
      return entries;
   }

   public Generator generator() {
      return generator;
   }

   public Icon icon() {
      return icon;
   }

   public Id id() {
      return id;
   }

   public Logo logo() {
      return logo;
   }

   public Rights rights() {
      return rights;
   }

   public Subtitle subtitle() {
      return subtitle;
   }

   public Title title() {
      return title;
   }

   public Updated updated() {
      return updated;
   }
}
