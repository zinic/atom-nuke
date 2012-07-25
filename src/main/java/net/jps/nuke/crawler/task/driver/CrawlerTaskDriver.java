package net.jps.nuke.crawler.task.driver;

import java.io.IOException;
import net.jps.nuke.atom.AtomParserException;
import net.jps.nuke.crawler.task.CrawlerTask;
import net.jps.nuke.listener.ListenerResult;

/**
 *
 * @author zinic
 */
public interface CrawlerTaskDriver {

   ListenerResult drive(CrawlerTask task) throws IOException, AtomParserException;
}
