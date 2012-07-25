package net.jps.nuke.crawler;

import net.jps.nuke.util.TimeValue;
import net.jps.nuke.crawler.threading.ExecutionManager;
import net.jps.nuke.crawler.threading.ExecutionManagerImpl;
import net.jps.nuke.crawler.task.ManagedTask;
import net.jps.nuke.crawler.task.ManagedTaskImpl;
import net.jps.nuke.crawler.task.CrawlerTask;
import net.jps.nuke.crawler.remote.CancelationRemote;
import net.jps.nuke.crawler.remote.CancelationRemoteImpl;
import com.rackspace.papi.commons.util.Destroyable;
import com.rackspace.papi.commons.util.thread.DestroyableThreadWrapper;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.jps.nuke.listener.FeedListener;

/**
 *
 * @author zinic
 */
public class NukeCrawlerController implements FeedCrawler, Runnable, Destroyable {

   private final CancelationRemote crawlerCancelationRemote;
   private final DestroyableThreadWrapper controlThread;
   private final ExecutionManager executionManager;
   private final List<ManagedTask> crawlerTasks;

   public NukeCrawlerController() {
      controlThread = DestroyableThreadWrapper.newThread(this);

      crawlerCancelationRemote = new CancelationRemoteImpl();
      executionManager = new ExecutionManagerImpl();
      crawlerTasks = new LinkedList<ManagedTask>();
   }

   private synchronized void remove(List<ManagedTask> tasks) {
      crawlerTasks.removeAll(tasks);
   }

   private synchronized void addManager(ManagedTask state) {
      crawlerTasks.add(state);
   }

   private synchronized List<ManagedTask> copyTasks() {
      return new LinkedList<ManagedTask>(crawlerTasks);
   }

   @Override
   public void run() {
      while (!crawlerCancelationRemote.canceled()) {
         final List<ManagedTask> tasks = copyTasks();
         final TimeValue now = TimeValue.now();

         for (Iterator<ManagedTask> itr = tasks.iterator(); itr.hasNext();) {
            final ManagedTask managedTask = itr.next();

            if (!managedTask.canceled()) {
               itr.remove();

               // Sould this task be scheduled? If so, is it already in the execution queue?
               if (now.isLessThan(managedTask.nextPollTime()) && !executionManager.submitted(managedTask)) {
                  executionManager.submit(managedTask);
               }
            }
         }

         // Remove finished tasks
         remove(tasks);
      }

      executionManager.destroy();
      controlThread.destroy();
   }

   @Override
   public void destroy() {
      crawlerCancelationRemote.cancel();
   }

   @Override
   public CrawlerTask newTask(FeedListener listener, String location) {
      final ManagedTask managedTask = new ManagedTaskImpl(listener);
      managedTask.nextLocation(location);

      addManager(managedTask);

      return managedTask;
   }
}
