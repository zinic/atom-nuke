package net.jps.nuke.crawler;

import net.jps.nuke.crawler.task.CrawlerTask;
import net.jps.nuke.listener.FeedListener;

/**
 *
 * @author zinic
 */
public interface FeedCrawler {

   CrawlerTask newTask(FeedListener listener, String origin);
}
