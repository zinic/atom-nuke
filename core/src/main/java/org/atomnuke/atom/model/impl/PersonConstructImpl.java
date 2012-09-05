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

   @Override
   public int hashCode() {
      int hash = 5;
      hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
      hash = 37 * hash + (this.email != null ? this.email.hashCode() : 0);
      hash = 37 * hash + (this.uri != null ? this.uri.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }

      final PersonConstructImpl other = (PersonConstructImpl) obj;

      if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
         return false;
      }

      if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
         return false;
      }

      if ((this.uri == null) ? (other.uri != null) : !this.uri.equals(other.uri)) {
         return false;
      }

      return super.equals(obj);
   }
}
