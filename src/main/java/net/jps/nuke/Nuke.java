package net.jps.nuke;

import net.jps.nuke.source.AtomSource;
import net.jps.nuke.task.Task;
import net.jps.nuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface Nuke {

   void start();

   void destroy();

   Task follow(AtomSource source);

   Task follow(AtomSource source, TimeValue pollingInterval);
}
