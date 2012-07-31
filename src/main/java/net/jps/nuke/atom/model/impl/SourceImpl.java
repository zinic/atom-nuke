package net.jps.nuke.atom.model.impl;

import java.util.List;
import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Generator;
import net.jps.nuke.atom.model.Id;
import net.jps.nuke.atom.model.Icon;
import net.jps.nuke.atom.model.Link;
import net.jps.nuke.atom.model.Logo;
import net.jps.nuke.atom.model.Rights;
import net.jps.nuke.atom.model.Source;
import net.jps.nuke.atom.model.Subtitle;
import net.jps.nuke.atom.model.Title;
import net.jps.nuke.atom.model.Updated;

/**
 *
 * @author zinic
 */
public abstract class SourceImpl extends AtomCommonAttributesImpl implements Source {

   protected List<Author> authors;
   protected List<Category> categories;
   protected List<Link> links;
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

   public List<Category> categories() {
      return categories;
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

   public List<Link> links() {
      return links;
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
