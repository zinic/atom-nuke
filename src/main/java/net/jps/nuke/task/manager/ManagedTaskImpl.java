package net.jps.nuke.task.manager;

import java.util.UUID;
import net.jps.nuke.listener.RegisteredListener;
import net.jps.nuke.listener.driver.AtomListenerDriver;
import net.jps.nuke.listener.driver.RegisteredListenerDriver;
import net.jps.nuke.listener.manager.ListenerManager;
import net.jps.nuke.source.AtomSource;
import net.jps.nuke.source.AtomSourceResult;
import net.jps.nuke.task.Task;
import net.jps.nuke.task.context.TaskContext;
import net.jps.nuke.task.lifecycle.DestructionException;
import net.jps.nuke.task.threading.ExecutionManager;
import net.jps.nuke.util.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ManagedTaskImpl implements ManagedTask {

   private static final Logger LOG = LoggerFactory.getLogger(ManagedTaskImpl.class);
   private final ExecutionManager executorService;
   private final ListenerManager listenerManager;
   private final AtomSource atomSource;
   private final Task task;
   private final UUID id;
   private TimeValue timestamp;

   public ManagedTaskImpl(Task task, ListenerManager listenerManager, TimeValue interval, ExecutionManager executorService, AtomSource atomSource) {
      this.task = task;

      this.listenerManager = listenerManager;
      this.executorService = executorService;
      this.atomSource = atomSource;

      timestamp = TimeValue.now();
      id = UUID.randomUUID();
   }

   @Override
   public boolean canceled() {
      return task.canceled();
   }

   @Override
   public void cancel() {
      task.cancel();
   }

   @Override
   public boolean isReentrant() {
      return listenerManager.isReentrant();
   }

   @Override
   public UUID id() {
      return id;
   }

   @Override
   public TimeValue nextPollTime() {
      return timestamp.add(task.interval());
   }

   @Override
   public void destroy(TaskContext taskContext) {
      for (RegisteredListener registeredListener : listenerManager.listeners()) {
         try {
            registeredListener.listener().destroy(taskContext);
         } catch (DestructionException sde) {
            LOG.error(sde.getMessage(), sde);
         }
      }
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
      // Only poll if we have listeners
      if (listenerManager.hasListeners()) {
         try {
            final AtomSourceResult pollResult = atomSource.poll();

            for (RegisteredListener listener : listenerManager.listeners()) {
               RegisteredListenerDriver listenerDriver;

               if (pollResult.isFeedPage()) {
                  listenerDriver = new AtomListenerDriver(listener, pollResult.feed());
               } else {
                  listenerDriver = new AtomListenerDriver(listener, pollResult.entry());
               }

               executorService.queue(listenerDriver);
            }

            // Updating our timestamp will mean waiting our scheduled interval until
            // next polling interval
            if (shouldUpdateTimestamp(pollResult)) {
               timestamp = TimeValue.now();
            }
         } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
         }
      }
   }
}
