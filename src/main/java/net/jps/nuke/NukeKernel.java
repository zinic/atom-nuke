package net.jps.nuke;

import net.jps.nuke.util.TimeValue;
import net.jps.nuke.task.threading.ExecutionManagerImpl;
import net.jps.nuke.task.ManagedTaskImpl;
import net.jps.nuke.task.Task;
import net.jps.nuke.util.remote.CancellationRemote;
import net.jps.nuke.util.remote.CancellationRemoteImpl;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.jps.nuke.source.AtomSource;

/**
 *
 * @author zinic
 */
public class NukeKernel implements Nuke {

   private final CancellationRemote crawlerCancellationRemote;
   private final ExecutorService executorService;
   private final KernelDelegate logic;
   private final Thread controlThread;

   public NukeKernel() {
      this(new ThreadPoolExecutor(2, 8, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>()));
   }

   public NukeKernel(ExecutorService executorService1) {
      crawlerCancellationRemote = new CancellationRemoteImpl();
      executorService = new ThreadPoolExecutor(2, 8, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
      logic = new KernelDelegate(crawlerCancellationRemote, new ExecutionManagerImpl(executorService));

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
   public Task follow(AtomSource source) {
      return follow(source, new TimeValue(1, TimeUnit.MINUTES));
   }

   @Override
   public Task follow(AtomSource source, TimeValue pollingInterval) {
      final ManagedTaskImpl managedTask = new ManagedTaskImpl(pollingInterval, executorService, source);
      logic.addTask(managedTask);

      return managedTask;
   }
}
