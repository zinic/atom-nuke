package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.Author;
import org.atomnuke.atom.model.Contributor;
import org.atomnuke.atom.model.PersonConstruct;

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
