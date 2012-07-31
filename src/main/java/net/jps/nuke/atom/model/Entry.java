package net.jps.nuke.atom.model;

import java.util.List;
import net.jps.nuke.atom.model.annotation.Required;

/**
 *
 * @author zinic
 */
public interface Entry extends AtomCommonAtributes {

   List<Author> authors();

   List<Category> categories();

   Content content();

   List<Contributor> contributors();

   @Required
   Id id();

   List<Link> links();

   Published published();

   Rights rights();

   Source source();

   Summary summary();

   @Required
   Title title();

   @Required
   Updated updated();
}
