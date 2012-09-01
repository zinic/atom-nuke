package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.PersonConstruct;
import org.atomnuke.atom.model.impl.PersonConstructImpl;

/**
 *
 * @author zinic
 */
public class PersonConstructBuilder <T extends PersonConstructBuilder, B extends PersonConstruct> extends AtomConstructBuilderImpl<T, B, PersonConstructImpl> {

   public PersonConstructBuilder(Class<T> builderClass) {
      super(builderClass, new PersonConstructImpl());
   }

   public PersonConstructBuilder setName(String name) {
      construct().setName(name);
      return this;
   }

   public PersonConstructBuilder setEmail(String email) {
      construct().setEmail(email);
      return this;
   }

   public PersonConstructBuilder setUri(String uri) {
      construct().setUri(uri);
      return this;
   }
}
