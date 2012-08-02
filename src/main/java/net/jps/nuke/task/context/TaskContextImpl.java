package net.jps.nuke.task.context;

import net.jps.nuke.task.submission.Tasker;

/**
 *
 * @author zinic
 */
public class TaskContextImpl implements TaskContext {

   private final Tasker submitter;

   public TaskContextImpl(Tasker submitter) {
      this.submitter = submitter;
   }

   @Override
   public Tasker submitter() {
      return submitter;
   }
}
