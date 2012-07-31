package net.jps.nuke.listener.eps;

import java.util.LinkedList;
import java.util.List;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.listener.AtomListener;
import net.jps.nuke.listener.AtomListenerException;
import net.jps.nuke.listener.AtomListenerResult;
import net.jps.nuke.listener.ListenerResult;
import net.jps.nuke.listener.ReentrantAtomListener;
import net.jps.nuke.listener.eps.handler.AtomEventlet;
import net.jps.nuke.listener.eps.selector.DefaultSelector;
import net.jps.nuke.listener.eps.selector.Selector;
import net.jps.nuke.service.DestructionException;
import net.jps.nuke.service.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EPS Relay - The event processing system relay
 *
 * "Oh, yes! For humans, touch can connect you to an object in a very personal
 * way; make it seem more real."
 *
 * -Jean-Luc Picard
 *
 * @author zinic
 */
public class Relay implements AtomListener, AtomEventHandlerRelay {

   private static final Logger LOG = LoggerFactory.getLogger(Relay.class);
   private final List<HandlerConduit> epsConduits;

   public Relay() {
      epsConduits = new LinkedList<HandlerConduit>();
   }

   private static void destroyConduit(HandlerConduit conduit) {
      try {
         conduit.destroy();
      } catch (DestructionException de) {
         LOG.error("Exception caught while destroying AtomEventHandler. Reason: " + de.getMessage(), de);
      }
   }

   private synchronized List<HandlerConduit> copyConduits() {
      return new LinkedList<HandlerConduit>(epsConduits);
   }

   private synchronized boolean removeConduit(HandlerConduit conduit) {
      destroyConduit(conduit);

      return epsConduits.remove(conduit);
   }

   @Override
   public void enlistHandler(AtomEventlet handler) throws InitializationException {
      enlistHandler(handler, DefaultSelector.INSTANCE);
   }

   @Override
   public synchronized void enlistHandler(AtomEventlet handler, Selector selector) throws InitializationException {
      handler.init();

      epsConduits.add(new HandlerConduit(handler, selector));
   }

   @Override
   public void init() throws InitializationException {
      LOG.info("Relay(" + this + ") started.");
   }

   @Override
   public void destroy() throws DestructionException {
      for (HandlerConduit conduit : epsConduits) {
         destroyConduit(conduit);
      }

      epsConduits.clear();
   }

   @Override
   public ListenerResult entry(Entry entry) throws AtomListenerException {
      for (HandlerConduit conduit : copyConduits()) {
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
      for (HandlerConduit conduit : copyConduits()) {
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
