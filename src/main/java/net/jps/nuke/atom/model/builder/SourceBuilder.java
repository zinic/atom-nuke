package net.jps.nuke.atom.model.builder;

import java.util.Collections;
import java.util.LinkedList;
import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Contributor;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Link;
import net.jps.nuke.atom.model.impl.SourceImpl;

/**
 *
 * @author zinic
 */
public class SourceBuilder extends SourceImpl {

   public SourceBuilder newBuilder() {
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
}
