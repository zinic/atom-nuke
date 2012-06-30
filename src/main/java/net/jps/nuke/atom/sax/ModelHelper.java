package net.jps.nuke.atom.sax;

import java.util.List;
import net.jps.nuke.atom.model.PersonConstruct;
import net.jps.nuke.atom.model.builder.EntryBuilder;
import net.jps.nuke.atom.model.builder.FeedBuilder;
import net.jps.nuke.atom.stax.AtomElement;

/**
 *
 * @author zinic
 */
public class ModelHelper {

   public List<? extends PersonConstruct> getPersonConstructList(AtomElement personElement, HandlerContext parent) {
      switch (parent.getElementDef()) {
         case FEED:
            return getPersonConstructListFromFeed(personElement, (FeedBuilder) parent.getBuilder());

         case ENTRY:
            return getPersonConstructListFromEntry(personElement, (EntryBuilder) parent.getBuilder());

         case SOURCE:
         default:
            throw invalidState(parent.getElementDef(), "Unexpected parent element for person construct list.");
      }
   }

   private List<? extends PersonConstruct> getPersonConstructListFromFeed(AtomElement personElement, FeedBuilder feed) {
      switch (personElement) {
         case AUTHOR:
            return feed.authors();

         case CONTRIBUTOR:
            return feed.contributors();

         default:
            throw invalidState(personElement, "Unexpected person construct element.");
      }
   }

   private List<? extends PersonConstruct> getPersonConstructListFromEntry(AtomElement personElement, EntryBuilder entry) {
      switch (personElement) {
         case AUTHOR:
            return entry.authors();

         case CONTRIBUTOR:
            return entry.contributors();

         default:
            throw invalidState(personElement, "Unexpected person construct element.");
      }
   }

   public InvalidElementException unexpectedElement(AtomElement parent, AtomElement child) {
      throw new InvalidElementException(child, "Unexpected parent element: " + parent);
   }

   public InvalidElementException invalidState(AtomElement cause, String message) {
      throw new InvalidStateException(cause, message);
   }
}
