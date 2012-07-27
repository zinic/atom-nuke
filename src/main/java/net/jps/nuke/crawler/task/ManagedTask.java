package net.jps.nuke.crawler.task;

import java.util.UUID;

/**
 *
 * @author zinic
 */
public interface ManagedTask extends CrawlerTask, Runnable {

   UUID id();

   void followNow(String location);

   void followLater(String location);
}
