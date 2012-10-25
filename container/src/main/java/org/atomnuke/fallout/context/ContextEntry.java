package org.atomnuke.fallout.context;

import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ContextEntry<T> {

   private final CancellationRemote cancellationRemote;
   private final T contextContent;

   public ContextEntry(CancellationRemote cancellationRemote, T contextContent) {
      this.cancellationRemote = cancellationRemote;
      this.contextContent = contextContent;
   }

   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }

   public T contextContent() {
      return contextContent;
   }
}
