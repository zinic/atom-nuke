package net.jps.nuke.crawler.threading;

import net.jps.nuke.crawler.task.driver.NukeCrawlerTaskDriver;
import net.jps.nuke.crawler.task.ManagedTask;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.jps.nuke.atom.sax.impl.SaxAtomParser;
import net.jps.nuke.crawler.task.driver.CrawlerTaskDriver;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author zinic
 */
public class ExecutionManagerImpl implements ExecutionManager {

   private final Map<ManagedTask, Future> executionStates;
   private final ExecutorService executorService;
   private final CrawlerTaskDriver taskDriver;

   public ExecutionManagerImpl() {
      this(new ThreadPoolExecutor(2, 8, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>()));
   }

   public ExecutionManagerImpl(ExecutorService executorService) {
      this.executorService = executorService;

      taskDriver = new NukeCrawlerTaskDriver(new DefaultHttpClient(), new SaxAtomParser());
      executionStates = new HashMap<ManagedTask, Future>();
   }

   @Override
   public void destroy() {
      executorService.shutdown();
   }

   @Override
   public synchronized void submit(ManagedTask task) {
      final Future execFuture = executorService.submit(new CrawlerWorker(task, taskDriver));
      executionStates.put(task, execFuture);
   }

   @Override
   public synchronized boolean submitted(ManagedTask task) {
      final Future taskFuture = executionStates.get(task);
      boolean submitted = false;

      if (taskFuture != null) {
         if (taskFuture.isDone()) {
            executionStates.remove(task);
         } else {
            submitted = true;
         }
      }

      return submitted;
   }
}
