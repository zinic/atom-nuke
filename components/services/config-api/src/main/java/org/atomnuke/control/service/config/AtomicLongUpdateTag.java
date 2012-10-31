package org.atomnuke.control.service.config;

import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.util.config.io.UpdateTag;

/**
 *
 * @author zinic
 */
public class AtomicLongUpdateTag implements UpdateTag {

   private final AtomicLong atomicLong;

   public AtomicLongUpdateTag() {
      this.atomicLong = new AtomicLong(0);
   }

   public void updated() {
      atomicLong.incrementAndGet();
   }

   @Override
   public long tagValue() {
      return atomicLong.get();
   }
}
