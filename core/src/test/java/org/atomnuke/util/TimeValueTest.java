package org.atomnuke.util;

import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 *
 * @author zinic
 */
@RunWith(Enclosed.class)
public class TimeValueTest {

   public static class WhenAdding {
      @Test
      public void shouldAddNanoToMicro() {
         final TimeValue nanoseconds = new TimeValue(2000, TimeUnit.NANOSECONDS);
         final TimeValue microseconds = new TimeValue(1, TimeUnit.MICROSECONDS);

         assertEquals(nanoseconds.add(microseconds).value(TimeUnit.NANOSECONDS), 3000);
         assertEquals(nanoseconds.subtract(microseconds).value(TimeUnit.NANOSECONDS), 1000);
      }
   }

   public static class WhenConverting {
      @Test
      public void shouldConvertUp() {
         final TimeValue nanoseconds = new TimeValue(1, TimeUnit.NANOSECONDS);

         assertEquals(nanoseconds.value(TimeUnit.MILLISECONDS), 0);
      }
   }
}
