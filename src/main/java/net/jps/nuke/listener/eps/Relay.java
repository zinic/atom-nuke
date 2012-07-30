package net.jps.nuke.listener.eps;

import java.util.LinkedList;
import java.util.List;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.listener.AtomListener;
import net.jps.nuke.listener.AtomListenerException;
import net.jps.nuke.listener.AtomListenerResult;
import net.jps.nuke.listener.ListenerResult;
import net.jps.nuke.listener.eps.handler.AtomEventHandler;
import net.jps.nuke.listener.eps.handler.Selector;

/**
 * EPS Relay: The event processing system relay.
 *
 * "Oh, yes! For humans, touch can connect you to an object in a very personal
 * way; make it seem more real."
 *
 * @author zinic
 */
public class Relay implements AtomListener {

   private final List<Conduit> epsConduits;

   public Relay() {
      epsConduits = new LinkedList<Conduit>();
   }

   private synchronized List<Conduit> copyConduits() {
      return new LinkedList<Conduit>(epsConduits);
   }

   private synchronized boolean removeConduit(Conduit conduit) {
      return epsConduits.remove(conduit);
   }

   public synchronized void enlistHandler(AtomEventHandler handler, Selector selector) {
      epsConduits.add(new Conduit(handler, selector));
   }

   @Override
   public void init() throws AtomListenerException {
   }

   @Override
   public void destroy() throws AtomListenerException {
      epsConduits.clear();
   }

   @Override
   public ListenerResult entry(Entry entry) throws AtomListenerException {
      for (Conduit conduit : copyConduits()) {
         switch (conduit.select(entry)) {
            case CLOSE:
               removeConduit(conduit);
               break;

            default:
         }
      }

      return AtomListenerResult.ok();
   }

   @Override
   public ListenerResult feedPage(Feed page) throws AtomListenerException {
      for (Conduit conduit : copyConduits()) {
         switch (conduit.select(page)) {
            case CLOSE:
               removeConduit(conduit);
               break;

            default:
         }
      }

      return AtomListenerResult.ok();
   }
}
