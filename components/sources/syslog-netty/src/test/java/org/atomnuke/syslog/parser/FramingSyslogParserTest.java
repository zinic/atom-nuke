package org.atomnuke.syslog.parser;

import org.atomnuke.syslog.SyslogMessage;
import org.jboss.netty.util.CharsetUtil;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author zinic
 */
@Ignore
@RunWith(Enclosed.class)
public class FramingSyslogParserTest {

   public static class WhenParsingMessages {
      private static final String TEST_1 = "159 <46>1 2012-12-05T16:17:37.456194-06:00 tohru rsyslogd - - -  [origin software=\"rsyslogd\" swVersion=\"7.2.2\" x-pid=\"6886\" x-info=\"http://www.rsyslog.com\"] start";
      
      @Test
      public void shouldParseValidMessage() {
         final FramingSyslogParser parser = new FramingSyslogParser();

         for (byte b : TEST_1.getBytes(CharsetUtil.UTF_8)) {
            if (parser.getState() == SyslogParserState.ERROR) {
               fail("Parsing failed. Reason: " + parser.getErrorMessage());
            }
            
            parser.next(b);
         }
         
         assertEquals(SyslogParserState.STOP, parser.getState());
         
         final SyslogMessage message = parser.getResult();
         
         
      }
   }
}
