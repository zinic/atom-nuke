package net.jps.nuke.task.submission;

import net.jps.nuke.source.AtomSource;
import net.jps.nuke.task.Task;
import net.jps.nuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface Tasker {

   Task follow(AtomSource source);

   Task follow(AtomSource source, TimeValue pollingInterval);
}
