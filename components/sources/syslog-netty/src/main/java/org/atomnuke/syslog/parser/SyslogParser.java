package org.atomnuke.syslog.parser;

import java.text.ParseException;
import java.util.Calendar;
import org.atomnuke.syslog.SyslogMessageBuilder;
import org.atomnuke.syslog.util.RFC3339DateParser;
import org.jboss.netty.util.CharsetUtil;

/**
 * http://tools.ietf.org/html/rfc5424
 *
 * @author zinic
 */
public class SyslogParser {

   private final Accumulator characterAccumulator;
   private final SyslogMessageBuilder messageBuilder;
   
   private SyslogParserState parserState;
   private String errorMessage;
   private int bytesToSkip;

   public SyslogParser() {
      // Smallest message for syslog is 2K so we'll start there
      characterAccumulator = new Accumulator(2048);
      
      // Sup.
      messageBuilder = new SyslogMessageBuilder();
      parserState = SyslogParserState.START;

      bytesToSkip = 0;
   }

   public String getErrorMessage() {
      return errorMessage;
   }

   public void next(byte b) {
      if (bytesToSkip > 0) {
         bytesToSkip--;
      } else {
         handleByte(b);
      }
   }

   private SyslogParserState currentState() {
      return parserState;
   }

   private void updateState(SyslogParserState state) {
      parserState = state;
   }

   private void skipNext(int bytesToSkip) {
      this.bytesToSkip = bytesToSkip;
   }

   private void handleByte(byte b) {
      switch (currentState()) {
         case START:
            readStart(b);
            break;

         case PRIORITY:
            readPriority(b);
            break;

         case VERSION:
            readVersion(b);
            break;

         case TIMESTAMP_HEAD:
            readValueHead(b, SyslogParserState.TIMESTAMP_CONTENT, SyslogParserState.HOSTNAME_HEAD);
            break;

         case TIMESTAMP_CONTENT:
            readTimestampContent(b);
            break;

         case HOSTNAME_HEAD:
            readValueHead(b, SyslogParserState.HOSTNAME_CONTENT, SyslogParserState.APPNAME_HEAD);
            break;

         case HOSTNAME_CONTENT:
            readHostnameContent(b);
            break;

         case APPNAME_HEAD:
            readValueHead(b, SyslogParserState.APPNAME_CONTENT, SyslogParserState.PROCESS_ID_HEAD);
            break;

         case APPNAME_CONTENT:
            readAppNameContent(b);
            break;

         case PROCESS_ID_HEAD:
            readValueHead(b, SyslogParserState.PROCESS_ID_CONTENT, SyslogParserState.MESSAGE_ID_HEAD);
            break;

         case PROCESS_ID_CONTENT:
            readProcessIdContent(b);
            break;

         case MESSAGE_ID_HEAD:
            readValueHead(b, SyslogParserState.MESSAGE_ID_CONTENT, SyslogParserState.STRUCTURED_DATA_HEAD);
            break;

         case MESSAGE_ID_CONTENT:
            readMessageIdContent(b);
            break;
            
         case STRUCTURED_DATA_HEAD:
            
            readValueHead(b, SyslogParserState.MESSAGE_ID_CONTENT, SyslogParserState.STRUCTURED_DATA_HEAD);
            break;

         case STOP:
         case ERROR:
         default:
      }
   }

   private boolean doneAccumulating(byte b, char controlCharacter) {
      if (b == controlCharacter) {
         return true;
      }

      characterAccumulator.add(b);
      return false;
   }

   private void readStart(byte b) {
      if (b == CharConstants.LEFT_ANGLE_BRACKET) {
         updateState(SyslogParserState.PRIORITY);
      } else {
         updateState(SyslogParserState.STOP);
      }
   }

   private void readValueHead(byte b, SyslogParserState contentState, SyslogParserState nilValueState) {
      if (b == CharConstants.NIL_CHAR) {
         updateState(nilValueState);
      } else {
         characterAccumulator.add(b);
         updateState(contentState);
      }
   }

   private void readPriority(byte b) {
      if (doneAccumulating(b, CharConstants.RIGHT_ANGLE_BRACKET)) {
         try {
            // Parse the priority value
            final Integer parsedInteger = Integer.parseInt(characterAccumulator.getAll(CharsetUtil.US_ASCII));
            messageBuilder.setPriority(parsedInteger);

            // Update the state for reading the version
            updateState(SyslogParserState.VERSION);
         } catch (NumberFormatException nfe) {
            updateState(SyslogParserState.ERROR);
            errorMessage = nfe.getMessage();
         }
      }
   }

   private void readVersion(byte b) {
      if (doneAccumulating(b, CharConstants.SPACE)) {
         try {
            // Parse the version value
            final Integer parsedInteger = Integer.parseInt(characterAccumulator.getAll(CharsetUtil.US_ASCII));
            messageBuilder.setVersion(parsedInteger);

            // Update the state for reading the version
            updateState(SyslogParserState.TIMESTAMP_HEAD);
         } catch (NumberFormatException nfe) {
            updateState(SyslogParserState.ERROR);
            errorMessage = nfe.getMessage();
         }
      }
   }

   private void readTimestampContent(byte b) {
      if (doneAccumulating(b, CharConstants.SPACE)) {
         try {
            final Calendar timestamp = RFC3339DateParser.parseRFC3339Date(characterAccumulator.getAll(CharsetUtil.US_ASCII));
            messageBuilder.setTimestamp(timestamp);

            updateState(SyslogParserState.HOSTNAME_HEAD);
         } catch (ParseException parseException) {
            updateState(SyslogParserState.ERROR);
            errorMessage = parseException.getMessage();
         }
      }
   }

   private void readHostnameContent(byte b) {
      if (doneAccumulating(b, CharConstants.SPACE)) {
         messageBuilder.setOriginHostname(characterAccumulator.getAll(CharsetUtil.US_ASCII));
         updateState(SyslogParserState.APPNAME_HEAD);
      }
   }

   private void readAppNameContent(byte b) {
      if (doneAccumulating(b, CharConstants.SPACE)) {
         messageBuilder.setApplicationName(characterAccumulator.getAll(CharsetUtil.US_ASCII));
         updateState(SyslogParserState.PROCESS_ID_HEAD);
      }
   }

   private void readProcessIdContent(byte b) {
      if (doneAccumulating(b, CharConstants.SPACE)) {
         messageBuilder.setProcessId(characterAccumulator.getAll(CharsetUtil.US_ASCII));
         updateState(SyslogParserState.MESSAGE_ID_HEAD);
      }
   }

   private void readMessageIdContent(byte b) {
      if (doneAccumulating(b, CharConstants.SPACE)) {
         messageBuilder.setMessageId(characterAccumulator.getAll(CharsetUtil.US_ASCII));
         updateState(SyslogParserState.STRUCTURED_DATA_HEAD);
      }
   }
}
