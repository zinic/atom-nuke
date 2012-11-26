package org.atomnuke.task.context;

import java.util.Map;
import org.atomnuke.NukeEnvironment;
import org.atomnuke.service.introspection.ServicesInterrogator;
import org.atomnuke.task.manager.AtomTasker;
import org.slf4j.Logger;

/**
 *
 * @author zinic
 */
public class TaskContextImpl implements AtomTaskContext {

   private final ServicesInterrogator servicesInterrogator;
   private final NukeEnvironment nukeEnvironment;
   private final Map<String, String> parameters;
   private final AtomTasker submitter;
   private final String actorId;
   private final Logger log;

   public TaskContextImpl(NukeEnvironment nukeEnvironment, Logger log, Map<String, String> parameters, ServicesInterrogator servicesInterrogator, AtomTasker submitter, String actorId) {
      this.servicesInterrogator = servicesInterrogator;
      this.nukeEnvironment = nukeEnvironment;
      this.parameters = parameters;
      this.submitter = submitter;
      this.actorId = actorId;
      this.log = log;
   }

   @Override
   public String actorId() {
      return actorId;
   }

   @Override
   public NukeEnvironment environment() {
      return nukeEnvironment;
   }

   @Override
   public Logger log() {
      return log;
   }

   @Override
   public ServicesInterrogator services() {
      return servicesInterrogator;
   }

   @Override
   public Map<String, String> parameters() {
      return parameters;
   }

   @Override
   public AtomTasker atomTasker() {
      return submitter;
   }
}
