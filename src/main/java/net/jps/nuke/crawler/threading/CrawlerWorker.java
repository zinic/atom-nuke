package net.jps.nuke.crawler.threading;

import net.jps.nuke.crawler.task.ManagedTask;
import net.jps.nuke.crawler.task.driver.CrawlerTaskDriver;
import net.jps.nuke.listener.ListenerResult;

/**
 *
 * @author zinic
 */
public class CrawlerWorker implements Runnable {

   private final CrawlerTaskDriver taskDriver;
   private final ManagedTask managedTask;

   public CrawlerWorker(ManagedTask managedTask, CrawlerTaskDriver taskDriver) {
      this.managedTask = managedTask;
      this.taskDriver = taskDriver;
   }

   public void run() {
      try {
         final ListenerResult result = taskDriver.drive(managedTask);

         switch (result.getAction()) {
            case FOLLOW_LINK:
               managedTask.nextLocation(result.getLink().href());
               break;

            case HALT:
            default:
         }
      } catch (Exception ioe) {
         ioe.printStackTrace(System.err);
      }
   }
}
