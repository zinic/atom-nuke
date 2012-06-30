package net.jps.nuke.atom.model.builder;

import java.net.URI;
import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Contributor;
import net.jps.nuke.atom.model.PersonConstruct;
import net.jps.nuke.atom.model.impl.PersonConstructImpl;

/**
 *
 * @author zinic
 */
public class PersonConstructBuilder extends PersonConstructImpl {

   public static PersonConstructBuilder newBuilder() {
      return new PersonConstructBuilder();
   }

   protected PersonConstructBuilder() {
   }

   public Author buildAuthor() {
      return this;
   }

   public Contributor buildContributor() {
      return this;
   }
   
   public PersonConstruct build() {
      return this;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public void setUri(String uri) {
      this.uri = uri;
   }

   public void setBase(URI base) {
      this.base = base;
   }

   public void setLang(String lang) {
      this.lang = lang;
   }
}
