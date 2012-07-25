package net.jps.nuke.crawler.task;

import net.jps.nuke.crawler.task.CrawlerTask;
import java.util.UUID;

/**
 *
 * @author zinic
 */
public interface ManagedTask extends CrawlerTask {

   UUID id();
}
