package org.atomnuke.task.context;

import java.util.Map;
import org.atomnuke.task.Tasker;

/**
 *
 * @author zinic
 */
public class TaskContextImpl implements TaskContext {

   private final Map<String, String> parameters;
   private final Tasker submitter;

   public TaskContextImpl(Map<String, String> parameters, Tasker submitter) {
      this.parameters = parameters;
      this.submitter = submitter;
   }

   @Override
   public Map<String, String> instanceParameters() {
      return parameters;
   }

   @Override
   public Tasker submitter() {
      return submitter;
   }
}
