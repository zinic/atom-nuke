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

   private UpdateTag lastUpdateTag;

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

   private synchronized List<ConfigurationListener<T>> getListeners() {
      final List<ConfigurationListener<T>> Listeners = new LinkedList<ConfigurationListener<T>>();

      for (Iterator<ListenerContext<T>> ListenerContextItr = ListenerContexts.iterator(); ListenerContextItr.hasNext();) {
         final ListenerContext<T> nextCtx = ListenerContextItr.next();

         // Cancellation may happen at anytime, remotely - check for it on every poll
         if (nextCtx.cancellationRemote().canceled()) {
            ListenerContextItr.remove();
            continue;
         }

         Listeners.add(nextCtx.configurationListener());
      }

      return Listeners;
   }

   public boolean updated() throws ConfigurationException {
      final UpdateTag newUpdateTag = manager.readUpdateTag();

      if (lastUpdateTag == null || !lastUpdateTag.equals(newUpdateTag)) {
         lastUpdateTag = newUpdateTag;
         return true;
      }

      return false;
   }

   public void dispatch() throws ConfigurationException {
      final T configuration = manager.read();

      for (ConfigurationListener<T> Listener : getListeners()) {
         try {
            Listener.updated(configuration);
         } catch (Exception ex) {
            LOG.error("Configuration exception during dispatch: " + ex.getMessage(), ex);
         }
      }
   }
}
