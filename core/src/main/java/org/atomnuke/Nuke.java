package org.atomnuke;

import org.atomnuke.context.InstanceContext;
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

   Task follow(InstanceContext<? extends AtomSource> source);

   Task follow(InstanceContext<? extends AtomSource> source, TimeValue pollingInterval);
}
