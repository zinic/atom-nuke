package org.atomnuke.task;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.source.AtomSource;
import org.atomnuke.util.TimeValue;

/**
 * A tasker represents an object that may schedule an AtomSource for polling.
 *
 * @see AtomSource
 * @see InstanceContext
 *
 * @author zinic
 */
public interface Tasker {

   /**
    * Follows an AtomSource at a defined polling rate. The tasker requires that
    * an InstanceContext be give for each AtomSource to allow for the
    * abstraction of system internals like custom class loaders.
    *
    * @param source the instance context of the source to be polled.
    * @param pollingInterval the polling interval of this source.
    * @return a new polling task instance.
    */
   AtomTask follow(InstanceContext<AtomSource> source, TimeValue pollingInterval);
}
