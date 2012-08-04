package org.atomnuke.task.context;

import org.atomnuke.task.Tasker;

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
