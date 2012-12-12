package org.atomnuke.util;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.atom.model.Author;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Contributor;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Link;
import org.atomnuke.atom.model.builder.AuthorBuilder;
import org.atomnuke.atom.model.builder.CategoryBuilder;
import org.atomnuke.atom.model.builder.ContributorBuilder;
import org.atomnuke.atom.model.builder.EntryBuilder;
import org.atomnuke.atom.model.builder.LinkBuilder;

/**
 *
 * @author zinic
 */
public final class CollectionUtil<R> {

   public static List<Author> copyAuthors(Collection<Author> authors) {
      return forCollection(authors).copyInto(new Copier<Author>() {
         @Override
         public Author element(Author element) {
            return new AuthorBuilder(element).build();
         }
      });
   }

   public static List<Contributor> copyContributors(Collection<Contributor> contributors) {
      return forCollection(contributors).copyInto(new Copier<Contributor>() {
         @Override
         public Contributor element(Contributor element) {
            return new ContributorBuilder(element).build();
         }
      });
   }

   public static List<Category> copyCategories(Collection<Category> categories) {
      return forCollection(categories).copyInto(new Copier<Category>() {
         @Override
         public Category element(Category element) {
            return new CategoryBuilder(element).build();
         }
      });
   }

   public static List<Link> copyLinks(Collection<Link> links) {
      return forCollection(links).copyInto(new Copier<Link>() {
         @Override
         public Link element(Link element) {
            return new LinkBuilder(element).build();
         }
      });
   }

   public static List<Entry> copyEntries(Collection<Entry> entries) {
      return forCollection(entries).copyInto(new Copier<Entry>() {
         @Override
         public Entry element(Entry element) {
            return new EntryBuilder(element).build();
         }
      });
   }

   public static <R> CollectionUtil<R> forCollection(Collection<R> collection) {
      return new CollectionUtil<R>(collection != null ? collection : Collections.EMPTY_LIST);
   }

   public static class CollectionUtilException extends RuntimeException {

      public CollectionUtilException(String message) {
         super(message);
      }

      public CollectionUtilException(String message, Throwable cause) {
         super(message, cause);
      }

      public CollectionUtilException(Throwable cause) {
         super(cause);
      }
   }

   private final Collection<R> sourceCollection;

   private CollectionUtil(Collection<R> sourceCollection) {
      this.sourceCollection = sourceCollection;
   }

   public static interface Delegate<R> {

      void element(R element);
   }

   public void each(Delegate<R> delegate) {
      for (R element : sourceCollection) {
         delegate.element(element);
      }
   }

   public static interface Copier<R> {

      R element(R element);
   }
   
   private static final Copier<Object> NOP_COPIER = new Copier<Object>() {
      @Override
      public Object element(Object element) {
         return element;
      }
   };

   public List<R> copyInto() {
      return copyInto((Copier<R>) NOP_COPIER);
   }

   public List<R> copyInto(Copier<R> copier) {
      try {
         final List<R> copyInstance = new LinkedList<R>();

         for (R value : sourceCollection) {
            copyInstance.add(copier.element(value));
         }

         return copyInstance;
      } catch (Exception ex) {
         throw new CollectionUtilException(ex);
      }
   }
}
