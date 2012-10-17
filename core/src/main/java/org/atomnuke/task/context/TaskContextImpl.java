package org.atomnuke.task.context;

import java.util.Map;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.task.Tasker;
import org.slf4j.Logger;

/**
 *
 * @author zinic
 */
public class TaskContextImpl implements AtomTaskContext {

   private final Map<String, String> parameters;
   private final ServiceManager services;
   private final Tasker submitter;
   private final Logger log;

   public TaskContextImpl(Logger log, Map<String, String> parameters, ServiceManager services, Tasker submitter) {
      this.parameters = parameters;
      this.services = services;
      this.submitter = submitter;
      this.log = log;
   }

   @Override
   public Logger log() {
      return log;
   }

   @Override
   public ServiceManager services() {
      return services;
   }

   @Override
   public Map<String, String> parameters() {
      return parameters;
   }

   @Override
   public Tasker atomTasker() {
      return submitter;
   }
}
