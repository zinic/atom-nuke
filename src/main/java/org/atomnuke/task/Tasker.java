package org.atomnuke.task;

import org.atomnuke.source.AtomSource;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface Tasker {

   Task follow(AtomSource source);

   Task follow(AtomSource source, TimeValue pollingInterval);
}
