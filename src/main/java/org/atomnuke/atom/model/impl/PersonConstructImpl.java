package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.Author;
import org.atomnuke.atom.model.Contributor;
import org.atomnuke.atom.model.PersonConstruct;

/**
 *
 * @author zinic
 */
public class PersonConstructImpl extends AtomCommonAttributesImpl implements PersonConstruct, Author, Contributor {

   private String name, email, uri;

   public void setName(String name) {
      this.name = name;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public void setUri(String uri) {
      this.uri = uri;
   }

   @Override
   public String name() {
      return name;
   }

   @Override
   public String email() {
      return email;
   }

   @Override
   public String uri() {
      return uri;
   }
}
