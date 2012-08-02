package net.jps.nuke;

import net.jps.nuke.task.submission.Tasker;

/**
 *
 * @author zinic
 */
public interface Nuke {

   void start();

   void destroy();

   Tasker submitter();
}
