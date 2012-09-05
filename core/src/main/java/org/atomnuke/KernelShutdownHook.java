package org.atomnuke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class KernelShutdownHook implements Runnable {

   private static final Logger LOG = LoggerFactory.getLogger(KernelShutdownHook.class);
   
   private final Nuke nukeRef;

   public KernelShutdownHook(Nuke nukeRef) {
      this.nukeRef = nukeRef;
   }

   @Override
   public void run() {
      LOG.info("Process shutting down. Destroying nuke kernel: " + nukeRef);
      
      nukeRef.destroy();
   }
}
