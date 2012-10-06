package org.atomnuke.listener.eps;

import java.util.LinkedList;
import java.util.List;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.plugin.local.LocalInstanceEnvironment;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.AtomListenerException;
import org.atomnuke.listener.AtomListenerResult;
import org.atomnuke.listener.ListenerResult;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.listener.eps.selector.DefaultSelector;
import org.atomnuke.listener.eps.selector.Selector;
import org.atomnuke.plugin.Environment;
import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.util.remote.CancellationRemote;
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
public class EventletRelay implements AtomListener, AtomEventHandlerRelay {

   private static final Logger LOG = LoggerFactory.getLogger(EventletRelay.class);

   private final List<HandlerConduit> epsConduits;

   public EventletRelay() {
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
   public CancellationRemote enlistHandler(AtomEventlet handler) {
      return enlistHandlerContext(LocalInstanceEnvironment.getInstance(), handler, DefaultSelector.instance());
   }

   @Override
   public CancellationRemote enlistHandler(AtomEventlet handler, Selector selector) {
      return enlistHandlerContext(LocalInstanceEnvironment.getInstance(), handler, selector);
   }

   @Override
   public CancellationRemote enlistHandlerContext(Environment environment, AtomEventlet handler){
      return enlistHandlerContext(environment, handler, DefaultSelector.instance());
   }

   @Override
   public synchronized CancellationRemote enlistHandlerContext(Environment environment, AtomEventlet handler, Selector selector) {
      final HandlerConduit newConduit = new HandlerConduit(environment, handler, selector);
      epsConduits.add(newConduit);

      return newConduit.cancellationRemote();
   }

   @Override
   public void init(TaskContext tc) throws InitializationException {
   }

   @Override
   public void destroy() throws DestructionException {
      for (HandlerConduit conduit : epsConduits) {
         destroyConduit(conduit);
      }

      epsConduits.clear();
   }

   private void garbageCollect() {
      for (HandlerConduit conduit : copyConduits()) {
         if (conduit.cancellationRemote().canceled()) {
            destroyConduit(conduit);
         }
      }
   }

   @Override
   public ListenerResult entry(Entry entry) throws AtomListenerException {
      garbageCollect();

      for (HandlerConduit conduit : copyConduits()) {
         switch (conduit.select(entry)) {
            case HALT:
               removeConduit(conduit);
               break;

            default:
         }
      }

      return AtomListenerResult.ok();
   }

   @Override
   public ListenerResult feedPage(Feed page) throws AtomListenerException {
      garbageCollect();

      for (HandlerConduit conduit : copyConduits()) {
         switch (conduit.select(page)) {
            case HALT:
               removeConduit(conduit);
               break;

            default:
         }
      }

      return AtomListenerResult.ok();
   }
}
