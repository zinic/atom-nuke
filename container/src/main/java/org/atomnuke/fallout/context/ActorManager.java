package org.atomnuke.fallout.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.atomnuke.lifecycle.Reclaimable;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ActorManager {

   private final Map<String, ActorEntry> actors;

   public ActorManager() {
      actors = new HashMap<String, ActorEntry>();
   }

   public synchronized void manageActor(String name, InstanceContext<? extends Reclaimable> actorContetx) {
      actors.put(name, new ActorEntry(actorContetx));
   }

   public synchronized void setCancellationRemote(String actorName, CancellationRemote remote) {
      final ActorEntry actor = actors.get(actorName);

      if (actor == null) {
         throw new IllegalArgumentException("No actor with name: " + actorName + " exists. This is a logic error and should be reported.");
      }

      actor.setCancellationRemote(remote);
   }

   public synchronized Collection<String> actorNames() {
      return new LinkedList<String>(actors.keySet());
   }

   public synchronized boolean hasActor(String actorName) {
      return actors.containsKey(actorName);
   }

   public synchronized ActorEntry getActor(String actorName) {
      return actors.get(actorName);
   }

   public synchronized ActorEntry removeActor(String actorId) {
      return actors.remove(actorId);
   }
}
