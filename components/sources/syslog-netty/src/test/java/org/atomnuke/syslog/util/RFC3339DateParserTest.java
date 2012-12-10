package org.atomnuke.syslog.util;

import java.util.Calendar;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 *
 * @author zinic
 */
@Ignore("")
@RunWith(Enclosed.class)
public class RFC3339DateParserTest {

   public static class WhenParsingDateStringsWithoutTimezones {

      @Test
      public void shouldParseValidRFC3339DateWithUTCTimezone() throws Exception {
         final Calendar calendar = RFC3339DateParser.parseRFC3339Date("2003-08-24T05:14:15Z");
      }

      @Test
      public void shouldParseValidRFC3339DateWithUTCTimezoneAndFractionalSeconds() throws Exception {
         final Calendar calendar = RFC3339DateParser.parseRFC3339Date("2003-08-24T05:14:15.512561Z");
      }
   }

   public static class WhenParsingDateStringsWithTimezones {
      
      @Test
      public void shouldParseValidRFC3339DateWithTimezoneSpecifier() throws Exception {
         final Calendar calendar = RFC3339DateParser.parseRFC3339Date("2003-08-24T05:14:15-08:00");
      }
      
      @Test
      public void shouldParseValidRFC3339DateWithTimezoneSpecifierAndFractionalSeconds() throws Exception {
         final Calendar calendar = RFC3339DateParser.parseRFC3339Date("2003-08-24T05:14:15.000003+06:00");
      }
   }
}
