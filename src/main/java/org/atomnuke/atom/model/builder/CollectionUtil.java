package org.atomnuke.atom.model.builder;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.atom.model.Author;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Contributor;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Link;

/**
 *
 * @author zinic
 */
public final class CollectionUtil<R> {

   public static List<Author> copyAuthors(Collection<Author> authors) {
      return forCollection(authors).copyInto(LinkedList.class, new Copier<Author>() {
         @Override
         public Author element(Author element) {
            return new AuthorBuilder(element).build();
         }
      });
   }

   public static List<Contributor> copyContributors(Collection<Contributor> contributors) {
      return forCollection(contributors).copyInto(LinkedList.class, new Copier<Contributor>() {
         @Override
         public Contributor element(Contributor element) {
            return new ContributorBuilder(element).build();
         }
      });
   }

   public static List<Category> copyCategories(Collection<Category> categories) {
      return forCollection(categories).copyInto(LinkedList.class, new Copier<Category>() {
         @Override
         public Category element(Category element) {
            return element;
         }
      });
   }

   public static List<Link> copyLinks(Collection<Link> links) {
      return forCollection(links).copyInto(LinkedList.class, new Copier<Link>() {
         @Override
         public Link element(Link element) {
            return new LinkBuilder(element).build();
         }
      });
   }

   public static List<Entry> copyEntries(Collection<Entry> entries) {
      return forCollection(entries).copyInto(LinkedList.class, new Copier<Entry>() {
         @Override
         public Entry element(Entry element) {
            return new EntryBuilder(element).build();
         }
      });
   }

   private static <R> CollectionUtil<R> forCollection(Collection<R> collection) {
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

   public <C extends Collection<R>> C copyInto(Class<C> collectionClass) {
      return copyInto(collectionClass, (Copier<R>) NOP_COPIER);
   }

   public <C extends Collection<R>> C copyInto(Class<C> collectionClass, Copier<R> copier) {
      try {
         final C copyInstance = collectionClass.newInstance();

         for (R value : sourceCollection) {
            copyInstance.add(copier.element(value));
         }

         return copyInstance;
      } catch (Exception ex) {
         throw new CollectionUtilException(ex);
      }
   }
}
