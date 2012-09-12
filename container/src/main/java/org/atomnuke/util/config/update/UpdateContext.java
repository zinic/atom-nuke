package org.atomnuke.util.config.update;

import org.atomnuke.util.config.io.ConfigurationManager;
import java.util.Iterator;
import org.atomnuke.util.config.update.listener.ConfigurationListener;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.UpdateTag;
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

   private final List<ListenerContext<T>> listenerContexts;
   private final CancellationRemote cancellationRemote;
   private final ConfigurationManager<T> manager;

   private UpdateTag lastUpdateTag;

   public UpdateContext(ConfigurationManager<T> manager) {
      this.manager = manager;

      cancellationRemote = new AtomicCancellationRemote();
      listenerContexts = new LinkedList<ListenerContext<T>>();
   }

   @Override
   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }

   private synchronized void addListenerContext(ListenerContext<T> context) {
      listenerContexts.add(context);
   }

   @Override
   public CancellationRemote addListener(ConfigurationListener<T> listener) throws ConfigurationException {
      final ListenerContext<T> newListenerContext = new ListenerContext<T>(listener, new AtomicCancellationRemote());
      addListenerContext(newListenerContext);

      dispatch();

      return newListenerContext.cancellationRemote();
   }

   private synchronized List<ConfigurationListener<T>> getListeners() {
      final List<ConfigurationListener<T>> listeners = new LinkedList<ConfigurationListener<T>>();

      for (Iterator<ListenerContext<T>> listenerContextItr = listenerContexts.iterator(); listenerContextItr.hasNext();) {
         final ListenerContext<T> nextCtx = listenerContextItr.next();

         if (nextCtx.cancellationRemote().canceled()) {
            listenerContextItr.remove();
            continue;
         }

         listeners.add(nextCtx.configurationListener());
      }

      return listeners;
   }

   public boolean updated() throws ConfigurationException {
      final UpdateTag newUpdateTag = manager.reader().readUpdateTag();

      if (lastUpdateTag == null || !lastUpdateTag.equals(newUpdateTag)) {
         lastUpdateTag = newUpdateTag;
         return true;
      }

      return false;
   }

   public void dispatch() throws ConfigurationException {
      final T configuration = manager.read();

      for (ConfigurationListener<T> listener : getListeners()) {
         try {
            listener.updated(configuration);
         } catch (Exception ex) {
            LOG.error("Configuration exception during dispatch: " + ex.getMessage(), ex);
         }
      }
   }
}
