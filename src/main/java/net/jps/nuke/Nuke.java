package net.jps.nuke;

import com.rackspace.papi.commons.util.Destroyable;
import net.jps.nuke.task.Task;
import net.jps.nuke.source.AtomSource;
import net.jps.nuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface Nuke extends Destroyable {

   void start();

   Task follow(AtomSource source);

   Task follow(AtomSource source, TimeValue pollingInterval);
}
