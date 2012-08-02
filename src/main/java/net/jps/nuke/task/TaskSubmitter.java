package net.jps.nuke.task;

import net.jps.nuke.source.AtomSource;
import net.jps.nuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface TaskSubmitter {

   Task follow(AtomSource source);

   Task follow(AtomSource source, TimeValue pollingInterval);
}
