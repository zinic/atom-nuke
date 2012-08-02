package net.jps.nuke.task;

import net.jps.nuke.task.context.TaskContext;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import net.jps.nuke.listener.RegisteredListener;
import net.jps.nuke.source.AtomSource;
import net.jps.nuke.source.AtomSourceResult;
import net.jps.nuke.listener.driver.RegisteredListenerDriver;
import net.jps.nuke.listener.driver.AtomListenerDriver;
import net.jps.nuke.task.lifecycle.DestructionException;
import net.jps.nuke.util.TimeValue;
import net.jps.nuke.util.remote.AtomicCancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ManagedTaskImpl extends TaskImpl implements ManagedTask {

   private static final Logger LOG = LoggerFactory.getLogger(ManagedTaskImpl.class);
   private final ExecutorService executorService;
   private final AtomSource atomSource;
   private final UUID id;

   public ManagedTaskImpl(TaskContext taskContext, TimeValue interval, ExecutorService executorService, AtomSource atomSource) {
      super(taskContext, interval.convert(TimeUnit.NANOSECONDS), new AtomicCancellationRemote());

      this.executorService = executorService;
      this.atomSource = atomSource;
      this.id = UUID.randomUUID();
   }

   private synchronized List<RegisteredListener> activeListeners() {
      final List<RegisteredListener> activeListeners = new LinkedList<RegisteredListener>();

      for (Iterator<RegisteredListener> listenerIterator = listeners().iterator(); listenerIterator.hasNext();) {
         final RegisteredListener registeredListener = listenerIterator.next();

         if (registeredListener.cancellationRemote().canceled()) {
            listenerIterator.remove();
         } else {
            activeListeners.add(registeredListener);
         }
      }

      return activeListeners;
   }

   @Override
   public synchronized void destroy() {
      for (RegisteredListener registeredListener : listeners()) {
         try {
            registeredListener.listener().destroy(taskContext());
         } catch (DestructionException sde) {
            LOG.error(sde.getMessage(), sde);
         }
      }

      listeners().clear();
   }

   /**
    * Returns true if the poll result was either an ATOM entry or an ATOM feed
    * page with zero entries in it.
    *
    * @param pollResult
    * @return
    */
   private static boolean shouldUpdateTimestamp(AtomSourceResult pollResult) {
      return !pollResult.isFeedPage() || pollResult.feed().entries().isEmpty();
   }

   @Override
   public void run() {
      final List<RegisteredListener> activeListeners = activeListeners();

      // Only poll if we have listeners
      if (!activeListeners.isEmpty()) {
         try {
            final AtomSourceResult pollResult = atomSource.poll();

            for (RegisteredListener listener : activeListeners()) {
               RegisteredListenerDriver listenerDriver;

               if (pollResult.isFeedPage()) {
                  listenerDriver = new AtomListenerDriver(listener, pollResult.feed());
               } else {
                  listenerDriver = new AtomListenerDriver(listener, pollResult.entry());
               }

               executorService.submit(listenerDriver);
            }

            // Updating our timestamp will mean waiting our scheduled interval until
            // next polling interval
            if (shouldUpdateTimestamp(pollResult)) {
               setTimestamp(TimeValue.now());
            }
         } catch (Exception ex) {
            // TODO:Log
            ex.printStackTrace(System.err);
         }
      }
   }

   @Override
   public UUID id() {
      return id;
   }

   @Override
   public int hashCode() {
      int hash = 5;
      hash = 59 * hash + id().hashCode();
      return hash;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj != null && ManagedTask.class == obj.getClass()) {
         final ManagedTask other = (ManagedTask) obj;

         return id().equals(other.id());
      }

      return false;
   }
}
