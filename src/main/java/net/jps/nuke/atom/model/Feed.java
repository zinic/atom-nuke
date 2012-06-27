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

   List<Contributer> contributers();

   List<Category> categories();

   Generator generator();

   Icon icon();

   @Required
   ID id();

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
