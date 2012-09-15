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
import org.atomnuke.util.remote.CancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @deprecated Renamed to EventletRelay. It's suggested that programmers use the
 * newer class over this one
 * @see EventletRelay
 *
 * @author zinic
 */
@Deprecated
public class Relay implements AtomListener, AtomEventHandlerRelay {


   private static final Logger LOG = LoggerFactory.getLogger(EventletRelay.class);

   private final List<HandlerConduit> epsConduits;
   private TaskContext taskCtx;

   public Relay() {
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
   public CancellationRemote enlistHandler(AtomEventlet handler) throws InitializationException {
      return enlistHandlerContext(new SimpleInstanceContext<AtomEventlet>(handler), DefaultSelector.instance());
   }

   @Override
   public CancellationRemote enlistHandler(AtomEventlet handler, Selector selector) throws InitializationException {
      return enlistHandlerContext(new SimpleInstanceContext<AtomEventlet>(handler), selector);
   }

   @Override
   public CancellationRemote enlistHandlerContext(InstanceContext<? extends AtomEventlet> handler) throws InitializationException {
      return enlistHandlerContext(handler, DefaultSelector.instance());
   }

   @Override
   public synchronized CancellationRemote enlistHandlerContext(InstanceContext<? extends AtomEventlet> handler, Selector selector) throws InitializationException {
      handler.stepInto();

      try {
         handler.getInstance().init(taskCtx);
      } finally {
         handler.stepOut();
      }

      final HandlerConduit newConduit = new HandlerConduit(handler, selector);
      epsConduits.add(newConduit);

      return newConduit.cancellationRemote();
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

   private void garbageCollect() {
      for (HandlerConduit conduit : copyConduits()) {
         if (conduit.cancellationRemote().canceled()) {
            destroyConduit(taskCtx, conduit);
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
