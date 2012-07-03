package net.jps.nuke.atom.xml;

import java.util.List;
import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Contributor;
import net.jps.nuke.atom.model.Link;
import net.jps.nuke.atom.model.builder.EntryBuilder;
import net.jps.nuke.atom.model.builder.FeedBuilder;
import net.jps.nuke.atom.model.builder.SourceBuilder;
import net.jps.nuke.atom.sax.HandlerContext;
import net.jps.nuke.atom.sax.InvalidElementException;
import net.jps.nuke.atom.sax.InvalidStateException;

/**
 *
 * @author zinic
 */
public class ModelHelper {

   public List<Link> getLinkList(HandlerContext target) {
      switch (target.getElementDef()) {
         case FEED:
            return ((FeedBuilder) target.builder()).links();

         case ENTRY:
            return ((EntryBuilder) target.builder()).links();

         case SOURCE:
            return ((SourceBuilder) target.builder()).links();

         default:
            throw unexpectedElement(target.getElementDef());
      }
   }

   public List<Category> getCategoryList(HandlerContext target) {
      switch (target.getElementDef()) {
         case FEED:
            return ((FeedBuilder) target.builder()).categories();

         case ENTRY:
            return ((EntryBuilder) target.builder()).categories();

         case SOURCE:
            return ((SourceBuilder) target.builder()).categories();

         default:
            throw unexpectedElement(target.getElementDef());
      }
   }

   public List<Author> getAuthorList(HandlerContext target) {
      switch (target.getElementDef()) {
         case FEED:
            return ((FeedBuilder) target.builder()).authors();

         case ENTRY:
            return ((EntryBuilder) target.builder()).authors();

         case SOURCE:
            return ((SourceBuilder) target.builder()).authors();

         default:
            throw unexpectedElement(target.getElementDef());
      }
   }

   public List<Contributor> getContributorList(HandlerContext target) {
      switch (target.getElementDef()) {
         case FEED:
            return ((FeedBuilder) target.builder()).contributors();

         case ENTRY:
            return ((EntryBuilder) target.builder()).contributors();

         default:
            throw unexpectedElement(target.getElementDef());
      }
   }

   public InvalidElementException unexpectedElement(AtomElement element) {
      return unexpectedElement(element.toString());
   }

   public InvalidElementException unexpectedElement(String element) {
      return new InvalidElementException("Element: " + element + " was unexpected in this context.");
   }

   public InvalidElementException invalidState(AtomElement cause, String message) {
      throw new InvalidStateException(cause, message);
   }
}