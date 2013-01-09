package org.atomnuke.syslog.parser;

import org.atomnuke.syslog.SyslogMessage;
import org.jboss.netty.util.CharsetUtil;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 *
 * @author zinic
 */
@RunWith(Enclosed.class)
public class FramingSyslogParserTest {

   public static class WhenParsingMessages {

      private static final String RFC_TEST = "254 <46>1 2012-12-05T16:17:37.456194-06:00  tohru  rsyslogd  -  -  [origin software=\"rsyslogd\"  swVersion=\"7.2.2\"  x-pid=\"6886\" x-info=\"http://www.rsyslog.com\"][testing software=\"rsyslogd\" swVersion=\"7.2.2\" x-pid=\"6886\" x-info=\"http://www.rsyslog.com\"] start";
      private static final String ACTUAL_RSYSLOG = "159 <46>1 2012-12-11T15:48:23.217459-06:00 tohru rsyslogd - - -  [origin software=\"rsyslogd\" swVersion=\"7.2.2\" x-pid=\"12297\" x-info=\"http://www.rsyslog.com\"] start";

      @Test
      public void shouldPerformWell() {
         final FramingSyslogParser parser = new FramingSyslogParser();
         final byte[] testBytes = RFC_TEST.getBytes(CharsetUtil.UTF_8);

         final long then = System.nanoTime();
         final int maxIterations = 100000;

         for (int iterations = 0; iterations < maxIterations; iterations++) {
            for (int index = 0; index < testBytes.length; index++) {
               parser.next(testBytes[index]);
            }

            if (parser.getState() != SyslogParserState.STOP) {
               fail("Parsing failed. Reason: " + parser.getErrorMessage());
            }

            parser.reset();
         }
         
         final double secondsTaken = (double) (System.nanoTime() - then) / 1000000000;
         final double passesPerSecond = (double) maxIterations / secondsTaken;

         System.out.println("Performed " + maxIterations + " parsing passes. Time taken: " + secondsTaken + " seconds. Average number of operations per second: " + passesPerSecond);
      }

      @Test
      public void shouldParseValidMessage() {
         final FramingSyslogParser parser = new FramingSyslogParser();

         for (byte b : RFC_TEST.getBytes(CharsetUtil.UTF_8)) {
            if (parser.getState() == SyslogParserState.ERROR) {
               fail("Parsing failed. Reason: " + parser.getErrorMessage());
            }

            parser.next(b);
         }

         assertEquals(SyslogParserState.STOP, parser.getState());

         final SyslogMessage message = parser.getResult();

         assertEquals("tohru", message.originHostname());
         assertEquals("rsyslogd", message.applicationName());
         assertEquals("start", message.content());
         assertEquals(1, message.version());
         assertEquals(46, message.priority());
      }
   }
}
