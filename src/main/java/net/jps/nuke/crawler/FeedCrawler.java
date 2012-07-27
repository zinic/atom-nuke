package net.jps.nuke.crawler;

import com.rackspace.papi.commons.util.Destroyable;
import net.jps.nuke.crawler.task.CrawlerTask;
import net.jps.nuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface FeedCrawler extends Destroyable {

   void start();

   CrawlerTask follow(String origin);

   CrawlerTask follow(String origin, TimeValue pollingInterval);
}
