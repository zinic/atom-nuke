package org.atomnuke.sink.eps;

import java.util.LinkedList;
import java.util.List;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.sink.AtomSink;
import org.atomnuke.sink.AtomSinkException;
import org.atomnuke.sink.AtomSinkResult;
import org.atomnuke.sink.SinkResult;
import org.atomnuke.sink.eps.eventlet.AtomEventlet;
import org.atomnuke.sink.eps.selector.DefaultSelector;
import org.atomnuke.sink.eps.selector.Selector;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.InstanceContextImpl;
import org.atomnuke.plugin.env.NopInstanceEnvironment;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.util.lifecycle.InitializationException;
import org.atomnuke.util.remote.CancellationRemote;
import org.atomnuke.util.service.ServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EPS - The event processing system... thingy
 *
 * TODO: Review this model a little bit more :x
 *
 * "Oh, yes! For humans, touch can connect you to an object in a very personal
 * way; make it seem more real."
 *
 * -Jean-Luc Picard
 *
 * @author zinic
 */
public class FanoutSink implements AtomSink, AtomEventletHandler {

   private static final Logger LOG = LoggerFactory.getLogger(FanoutSink.class);

   private final List<EventletConduit> epsConduits;
   private ReclamationHandler reclamationHandler;

   public FanoutSink() {
      epsConduits = new LinkedList<EventletConduit>();
   }

   private static void destroyConduit(EventletConduit conduit) {
      try {
         conduit.destroy();
      } catch (Exception de) {
         LOG.error("Exception caught while destroying AtomEventHandler. Reason: " + de.getMessage(), de);
      }
   }

   private synchronized List<EventletConduit> copyConduits() {
      return new LinkedList<EventletConduit>(epsConduits);
   }

   private synchronized boolean removeConduit(EventletConduit conduit) {
      destroyConduit(conduit);

      return epsConduits.remove(conduit);
   }

   @Override
   public CancellationRemote enlistHandler(AtomEventlet handler) {
      return enlistHandler(new InstanceContextImpl<AtomEventlet>(NopInstanceEnvironment.getInstance(), handler));
   }

   @Override
   public CancellationRemote enlistHandler(AtomEventlet handler, Selector selector) {
      return enlistHandler(new InstanceContextImpl<AtomEventlet>(NopInstanceEnvironment.getInstance(), handler), selector);
   }

   @Override
   public CancellationRemote enlistHandler(InstanceContext<? extends AtomEventlet> handler) {
      return enlistHandler(handler, DefaultSelector.instance());
   }

   @Override
   public synchronized CancellationRemote enlistHandler(InstanceContext<? extends AtomEventlet> handler, Selector selector) {
      final CancellationRemote cancellationRemote = reclamationHandler.watch(handler);

      final EventletConduit newConduit = new EventletConduit((InstanceContext<AtomEventlet>) handler, cancellationRemote, selector);
      epsConduits.add(newConduit);

      return newConduit.cancellationRemote();
   }

   @Override
   public void init(AtomTaskContext tc) throws InitializationException {
      try {
         reclamationHandler = ServiceHandler.instance().firstAvailable(tc.services(), ReclamationHandler.class);
      } catch (ServiceUnavailableException sue) {
         throw new InitializationException(sue);
      }
   }

   @Override
   public void destroy() {
      for (EventletConduit conduit : epsConduits) {
         destroyConduit(conduit);
      }

      epsConduits.clear();
   }

   private void garbageCollect() {
      for (EventletConduit conduit : copyConduits()) {
         if (conduit.cancellationRemote().canceled()) {
            removeConduit(conduit);
         }
      }
   }

   @Override
   public SinkResult entry(Entry entry) throws AtomSinkException {
      garbageCollect();

      for (EventletConduit conduit : copyConduits()) {
         switch (conduit.select(entry)) {
            case HALT:
               removeConduit(conduit);
               break;

            default:
         }
      }

      return AtomSinkResult.ok();
   }

   @Override
   public SinkResult feedPage(Feed page) throws AtomSinkException {
      garbageCollect();

      for (EventletConduit conduit : copyConduits()) {
         switch (conduit.select(page)) {
            case HALT:
               removeConduit(conduit);
               break;

            default:
         }
      }

      return AtomSinkResult.ok();
   }
}
