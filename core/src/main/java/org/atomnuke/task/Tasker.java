package org.atomnuke.task;

import org.atomnuke.context.InstanceContext;
import org.atomnuke.source.AtomSource;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface Tasker {

   Task follow(InstanceContext<? extends AtomSource> source);

   Task follow(InstanceContext<? extends AtomSource> source, TimeValue pollingInterval);
}
