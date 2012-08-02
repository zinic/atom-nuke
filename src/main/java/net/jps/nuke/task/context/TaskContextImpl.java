package net.jps.nuke.task.context;

import net.jps.nuke.Nuke;

/**
 *
 * @author zinic
 */
public class TaskContextImpl implements TaskContext {

   private final Nuke nuke;

   public TaskContextImpl(Nuke nuke) {
      this.nuke = nuke;
   }

   @Override
   public Nuke nuke() {
      return nuke;
   }
}
