package net.jps.nuke.atom.model;

import java.util.List;
import net.jps.nuke.atom.model.annotation.ElementValue;
import net.jps.nuke.atom.model.annotation.Required;

/**
 *
 * @author zinic
 */
public interface Feed extends AtomCommonAtributes {

   List<Author> authors();

   List<Contributor> contributors();

   List<Category> categories();

   Generator generator();

   Icon icon();

   @Required
   Id id();

   List<Link> links();

   Logo logo();

   Rights rights();

   Subtitle subtitle();

   @Required
   Title title();

   @Required
   Updated updated();

   @ElementValue
   List<Entry> entries();
}
