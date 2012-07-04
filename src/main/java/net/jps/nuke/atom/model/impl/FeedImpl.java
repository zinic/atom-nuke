package net.jps.nuke.atom.model.impl;

import java.util.List;
import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Contributor;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.atom.model.Generator;
import net.jps.nuke.atom.model.ID;
import net.jps.nuke.atom.model.Icon;
import net.jps.nuke.atom.model.Link;
import net.jps.nuke.atom.model.Logo;
import net.jps.nuke.atom.model.Rights;
import net.jps.nuke.atom.model.Subtitle;
import net.jps.nuke.atom.model.Title;
import net.jps.nuke.atom.model.Updated;

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
   protected ID id;
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

   public ID id() {
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
