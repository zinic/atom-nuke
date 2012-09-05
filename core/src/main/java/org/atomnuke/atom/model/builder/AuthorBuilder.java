package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Author;

/**
 *
 * @author zinic
 */
public class AuthorBuilder extends PersonConstructBuilder<AuthorBuilder, Author> {

   public AuthorBuilder() {
      super(AuthorBuilder.class);
   }

   public AuthorBuilder(Author author) {
      super(AuthorBuilder.class, author);
   }
}
