package net.jps.nuke.atom.model.impl;

import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Contributor;
import net.jps.nuke.atom.model.PersonConstruct;

/**
 *
 * @author zinic
 */
public abstract class PersonConstructImpl extends AtomCommonAttributesImpl implements PersonConstruct, Author, Contributor {

   protected String name;
   protected String email;
   protected String uri;

   public String name() {
      return name;
   }

   public String email() {
      return email;
   }

   public String uri() {
      return uri;
   }
}
