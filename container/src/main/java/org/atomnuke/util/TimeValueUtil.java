package org.atomnuke.util;

import java.util.concurrent.TimeUnit;
import org.atomnuke.config.model.PollingInterval;
import org.atomnuke.config.model.TimeUnitType;

/**
 *
 * @author zinic
 */
public final class TimeValueUtil {

   public static TimeValue fromPollingInterval(PollingInterval interval) {
      return new TimeValue(interval.getValue(), fromTimeUnitType(interval.getUnit()));

   }

   public static TimeUnit fromTimeUnitType(TimeUnitType type) {
      switch (type) {
         case NANOSECONDS:
            return TimeUnit.NANOSECONDS;
         case MICROSECONDS:
            return TimeUnit.MICROSECONDS;
         case SECONDS:
            return TimeUnit.SECONDS;
         case MINUTES:
            return TimeUnit.MINUTES;
         case HOURS:
            return TimeUnit.HOURS;
         case DAYS:
            return TimeUnit.DAYS;

         default:
         case MILLISECONDS:
            return TimeUnit.MILLISECONDS;
      }
   }

   private TimeValueUtil() {
   }
}
