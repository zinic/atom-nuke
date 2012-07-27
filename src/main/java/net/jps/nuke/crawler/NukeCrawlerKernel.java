package net.jps.nuke.crawler;

import net.jps.nuke.util.TimeValue;
import net.jps.nuke.crawler.threading.ExecutionManagerImpl;
import net.jps.nuke.crawler.task.ManagedTaskImpl;
import net.jps.nuke.crawler.task.CrawlerTask;
import net.jps.nuke.crawler.remote.CancellationRemote;
import net.jps.nuke.crawler.remote.CancellationRemoteImpl;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zinic
 */
public class NukeCrawlerKernel implements FeedCrawler {

   private final CancellationRemote crawlerCancellationRemote;
   private final ExecutorService executorService;
   private final CrawlerKernelDelegate logic;
   private final Thread controlThread;

   public NukeCrawlerKernel() {
      this(new ThreadPoolExecutor(2, 8, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>()));
   }

   public NukeCrawlerKernel(ExecutorService executorService1) {
      crawlerCancellationRemote = new CancellationRemoteImpl();
      executorService = new ThreadPoolExecutor(2, 8, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
      logic = new CrawlerKernelDelegate(crawlerCancellationRemote, new ExecutionManagerImpl(executorService));

      controlThread = new Thread(logic);
   }

   public void start() {
      if (controlThread.getState() != Thread.State.NEW) {
         throw new IllegalStateException("Crawler already started or destroyed.");
      }

      controlThread.start();
   }

   @Override
   public void destroy() {
      crawlerCancellationRemote.cancel();
      controlThread.interrupt();

      try {
         controlThread.join();
      } catch (InterruptedException ie) {
         // TODO:Log
      }
   }

   @Override
   public CrawlerTask follow(String location) {
      return follow(location, new TimeValue(1, TimeUnit.MINUTES));
   }

   @Override
   public CrawlerTask follow(String location, TimeValue pollingInterval) {
      final ManagedTaskImpl managedTask = new ManagedTaskImpl(pollingInterval, executorService);
      managedTask.followNow(location);

      logic.addTask(managedTask);

      return managedTask;
   }
}
