package org.atomnuke.listener.eps;

import java.util.LinkedList;
import java.util.List;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.context.InstanceContext;
import org.atomnuke.context.SimpleInstanceContext;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.AtomListenerException;
import org.atomnuke.listener.AtomListenerResult;
import org.atomnuke.listener.ListenerResult;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.listener.eps.selector.DefaultSelector;
import org.atomnuke.listener.eps.selector.Selector;
import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;
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
   private TaskContext taskCtx;

   public EventletRelay() {
      epsConduits = new LinkedList<HandlerConduit>();
   }

   private static void destroyConduit(TaskContext tc, HandlerConduit conduit) {
      try {
         conduit.destroy(tc);
      } catch (DestructionException de) {
         LOG.error("Exception caught while destroying AtomEventHandler. Reason: " + de.getMessage(), de);
      }
   }

   private synchronized List<HandlerConduit> copyConduits() {
      return new LinkedList<HandlerConduit>(epsConduits);
   }

   private synchronized boolean removeConduit(HandlerConduit conduit) {
      destroyConduit(taskCtx, conduit);

      return epsConduits.remove(conduit);
   }

   @Override
   public void enlistHandler(AtomEventlet handler) throws InitializationException {
      enlistHandlerContext(new SimpleInstanceContext<AtomEventlet>(handler), DefaultSelector.instance());
   }

   @Override
   public void enlistHandler(AtomEventlet handler, Selector selector) throws InitializationException {
      enlistHandlerContext(new SimpleInstanceContext<AtomEventlet>(handler), selector);
   }

   @Override
   public void enlistHandlerContext(InstanceContext<AtomEventlet> handler) throws InitializationException {
      enlistHandlerContext(handler, DefaultSelector.instance());
   }

   @Override
   public synchronized void enlistHandlerContext(InstanceContext<AtomEventlet> handler, Selector selector) throws InitializationException {
      handler.stepInto();

      try {
         handler.getInstance().init(taskCtx);
      } finally {
         handler.stepOut();
      }

      epsConduits.add(new HandlerConduit(handler, selector));
   }

   @Override
   public void init(TaskContext tc) throws InitializationException {
      taskCtx = tc;
   }

   @Override
   public void destroy(TaskContext tc) throws DestructionException {
      for (HandlerConduit conduit : epsConduits) {
         destroyConduit(tc, conduit);
      }

      epsConduits.clear();
   }

   @Override
   public ListenerResult entry(Entry entry) throws AtomListenerException {
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
