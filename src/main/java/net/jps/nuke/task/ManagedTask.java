package net.jps.nuke.task;

import java.util.UUID;

/**
 *
 * @author zinic
 */
public interface ManagedTask extends Task, Runnable {

   UUID id();

   void destroy();
}
