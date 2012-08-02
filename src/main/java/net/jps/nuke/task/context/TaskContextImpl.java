package net.jps.nuke.task.context;

import net.jps.nuke.task.submission.TaskSubmitter;

/**
 *
 * @author zinic
 */
public class TaskContextImpl implements TaskContext {

   private final TaskSubmitter submitter;

   public TaskContextImpl(TaskSubmitter submitter) {
      this.submitter = submitter;
   }

   @Override
   public TaskSubmitter submitter() {
      return submitter;
   }
}
