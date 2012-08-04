package org.atomnuke.nuke;

import org.atomnuke.source.AtomSource;
import org.atomnuke.task.Task;
import org.atomnuke.util.TimeValue;

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
