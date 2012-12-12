package org.atomnuke.syslog.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author zinic
 */
public class RFC3339DateParser {

   private static final SimpleDateFormat RFC3339_BASIC_WITH_TZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
   private static final SimpleDateFormat RFC3339_FRACTIONAL_WITH_TZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
   
   private static final SimpleDateFormat RFC3339_BASIC_NO_TZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
   private static final SimpleDateFormat RFC3339_FRACTIONAL_NO_TZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

   static {
      RFC3339_FRACTIONAL_NO_TZ.setLenient(Boolean.TRUE);
   }

   public static Calendar parseRFC3339Date(String datestring) throws ParseException {
      final boolean hasFractionalSeconds = datestring.contains(".");
      final boolean hasUTCTimezone = datestring.contains("Z");

      Date parsedDate;

      if (hasUTCTimezone) {
         parsedDate = hasFractionalSeconds ? RFC3339_FRACTIONAL_NO_TZ.parse(datestring) : RFC3339_BASIC_NO_TZ.parse(datestring);
      } else {
         parsedDate = hasFractionalSeconds ? RFC3339_FRACTIONAL_WITH_TZ.parse(datestring) : RFC3339_BASIC_WITH_TZ.parse(datestring);
      }

      // Set our time
      final Calendar calendarRepresentation = Calendar.getInstance();
      calendarRepresentation.setTime(parsedDate);

      return calendarRepresentation;
   }
}
