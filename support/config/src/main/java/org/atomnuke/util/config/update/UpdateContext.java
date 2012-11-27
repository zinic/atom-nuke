package org.atomnuke.util.config.update;

import org.atomnuke.util.config.io.ConfigurationManager;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.UpdateTag;
import org.atomnuke.util.config.update.listener.ConfigurationListener;
import org.atomnuke.util.config.update.listener.ListenerContext;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class UpdateContext<T> implements ConfigurationContext<T> {

   private static final Logger LOG = LoggerFactory.getLogger(UpdateContext.class);
   
   private final List<ListenerContext<T>> ListenerContexts;
   private final CancellationRemote cancellationRemote;
   private final ConfigurationManager<T> manager;
   private UpdateTag latestUpdateTag;

   public UpdateContext(ConfigurationManager<T> manager, CancellationRemote cancellationRemote) {
      this.manager = manager;
      this.cancellationRemote = cancellationRemote;

      ListenerContexts = new LinkedList<ListenerContext<T>>();
   }

   @Override
   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }

   private synchronized void addListenerContext(ListenerContext<T> context) {
      ListenerContexts.add(context);
   }

   @Override
   public CancellationRemote addListener(ConfigurationListener<T> Listener) throws ConfigurationException {
      final ListenerContext<T> newListenerContext = new ListenerContext<T>(Listener, new AtomicCancellationRemote());
      addListenerContext(newListenerContext);

      dispatch();

      return newListenerContext.cancellationRemote();
   }

   private synchronized List<ListenerContext<T>> getListeners() {
      final List<ListenerContext<T>> listeners = new LinkedList<ListenerContext<T>>();

      for (Iterator<ListenerContext<T>> ListenerContextItr = ListenerContexts.iterator(); ListenerContextItr.hasNext();) {
         final ListenerContext<T> nextCtx = ListenerContextItr.next();

         // Cancellation may happen at anytime, remotely - check for it on every poll
         if (nextCtx.cancellationRemote().canceled()) {
            ListenerContextItr.remove();
            continue;
         }

         listeners.add(nextCtx);
      }

      return listeners;
   }

   public synchronized UpdateTag currentUpdateTag() throws ConfigurationException {
      final UpdateTag newUpdateTag = manager.readUpdateTag();

      if (latestUpdateTag == null || !latestUpdateTag.equals(newUpdateTag)) {
         latestUpdateTag = newUpdateTag;
      }

      return latestUpdateTag;
   }

   public boolean updated() throws ConfigurationException {
      final UpdateTag previousUpdateTag = latestUpdateTag;
      return !previousUpdateTag.equals(currentUpdateTag());
   }

   public void dispatch() throws ConfigurationException {
      final T configuration = manager.read();

      // TODO: This might mask a deeper error - should use exception handling
      if (configuration != null) {
         final UpdateTag currentUpdateTag = currentUpdateTag();

         for (ListenerContext<T> listenerContext : getListeners()) {
            if (listenerContext.lastUpdateTagSeen() == null || !listenerContext.lastUpdateTagSeen().equals(currentUpdateTag)) {
               try {
                  listenerContext.configurationListener().updated(configuration);
                  listenerContext.setLastUpdateTagSeen(currentUpdateTag);
               } catch (Exception ex) {
                  LOG.error("Configuration exception during dispatch: " + ex.getMessage(), ex);
               }
            }
         }
      }
   }
}
