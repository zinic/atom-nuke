package org.atomnuke.syslog.parser;

import java.text.ParseException;
import java.util.Calendar;
import org.atomnuke.syslog.SyslogMessage;
import org.atomnuke.syslog.SyslogMessageBuilder;
import org.atomnuke.syslog.util.RFC3339DateParser;
import org.jboss.netty.util.CharsetUtil;

/**
 * http://tools.ietf.org/html/rfc5424
 *
 * @author zinic
 */
public class FramingSyslogParser {

   private final Accumulator characterAccumulator;
   private final SyslogMessageBuilder messageBuilder;
   private SyslogParserState parserState;
   private String errorMessage;
   private int octetsRemaining, skipCount;
   private boolean countOctets, escaped;

   public FramingSyslogParser() {
      // Smallest message for syslog is 2K so we'll start there
      characterAccumulator = new Accumulator(2048);

      // Sup.
      messageBuilder = new SyslogMessageBuilder();
      parserState = SyslogParserState.START;

      skipCount = 0;
      countOctets = false;
      escaped = false;
   }

   public SyslogParserState getState() {
      return parserState;
   }

   public SyslogMessage getResult() {
      return messageBuilder;
   }

   public String getErrorMessage() {
      return errorMessage;
   }

   private void skipNext(int bytes) throws SyslogParserException {
      if (countOctets && skipCount > octetsRemaining) {
         throw new SyslogParserException("Attempting to skip more bytes than available");
      }

      skipCount = bytes;
   }

   public void next(byte b) {
      if (countOctets) {
         octetsRemaining--;
      }

      try {
         if (skipCount <= 0) {
            handleByte(b);
         } else {
            skipCount--;
         }
      } catch (SyslogParserException spe) {
         updateState(SyslogParserState.ERROR);
         errorMessage = spe.getMessage();
      }
   }

   private SyslogParserState currentState() {
      return parserState;
   }

   private void updateState(SyslogParserState state) {
      parserState = state;
   }

   private void handleByte(byte b) throws SyslogParserException {
      switch (currentState()) {
         case START:
            readStart(b);
            break;

         case MESSAGE_OCTET_COUNT:
            readMessageOctetLength(b);
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
            readStructuredDataHead(b);
            break;

         case STRUCTURED_DATA_ID:
            readStructuredDataId(b);
            break;

         case STRUCTURED_DATA_PARAM:
            readStructuredDataParam(b);
            break;

         case STRUCTURED_DATA_PARAM_NAME:
            readStructuredDataParamName(b);
            break;

         case STRUCTURED_DATA_PARAM_VALUE:
            readStructuredDataParamValue(b);
            break;

         case MESSAGE_CONTENT:
            readMessageContent(b);
            break;

         case STOP:
         case ERROR:
         default:
      }
   }

   private boolean doneAccumulating(byte b, char... controlCharacters) {
      boolean done = false;

      for (char control : controlCharacters) {
         // Found a control character that matches
         if (b == control) {
            if (!escaped) {
               // If this character isn't escaped then we're done
               done = true;
            } else {
               // If this character is escaped, mark that we aren't escaped anymore and accumulate
               escaped = false;
            }

            break;
         }
      }

      if (!done) {
         characterAccumulator.add(b);
      }

      return done;
   }

   private boolean doneAccumulatingEscaped(byte b, char... controlCharacters) {
      if (!escaped && b == CharConstants.ESCAPE) {
         escaped = true;
         return false;
      }

      return doneAccumulating(b, controlCharacters);
   }

   private void readStart(byte b) {
      if (b == CharConstants.LEFT_ANGLE_BRACKET) {
         updateState(SyslogParserState.PRIORITY);
      } else {
         // Accumulate this
         characterAccumulator.add(b);

         // This has an octet count so let's process it
         updateState(SyslogParserState.MESSAGE_OCTET_COUNT);
      }
   }

   private Integer readAccumulatedInteger() throws SyslogParserException {
      final String asciiString = characterAccumulator.getAll(CharsetUtil.US_ASCII);

      try {
         // Parse the priority value
         return Integer.parseInt(asciiString);
      } catch (NumberFormatException nfe) {
         throw new SyslogParserException("Unable to read accumulated bytes as a valid integer. Decoded bytes: " + asciiString + " - State: " + currentState() + " - Reason: " + nfe.getMessage(), nfe);
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

   private void readMessageOctetLength(byte b) throws SyslogParserException {
      if (doneAccumulating(b, CharConstants.SPACE)) {
         countOctets = true;
         octetsRemaining = readAccumulatedInteger();

         // Skip the left angle bracket
         skipNext(1);

         updateState(SyslogParserState.PRIORITY);
      }
   }

   private void readPriority(byte b) throws SyslogParserException {
      if (doneAccumulating(b, CharConstants.RIGHT_ANGLE_BRACKET)) {
         // Parse the priority value
         final Integer priority = readAccumulatedInteger();
         messageBuilder.setPriority(priority);

         // Update the state for reading the version
         updateState(SyslogParserState.VERSION);
      }
   }

   private void readVersion(byte b) throws SyslogParserException {
      if (doneAccumulating(b, CharConstants.SPACE)) {
         // Parse the version value
         final Integer version = readAccumulatedInteger();
         messageBuilder.setVersion(version);

         // Update the state for reading the version
         updateState(SyslogParserState.TIMESTAMP_HEAD);
      }
   }

   private void readTimestampContent(byte b) throws SyslogParserException {
      if (doneAccumulating(b, CharConstants.SPACE)) {
         final String timestampValue = characterAccumulator.getAll(CharsetUtil.US_ASCII);

         try {
            final Calendar timestamp = RFC3339DateParser.parseRFC3339Date(timestampValue);
            messageBuilder.setTimestamp(timestamp);

            updateState(SyslogParserState.HOSTNAME_HEAD);
         } catch (ParseException parseException) {
            throw new SyslogParserException("Unable to parse date field: " + timestampValue, parseException);
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

   private void readStructuredDataHead(byte b) {
      if (b == CharConstants.NIL_CHAR) {
         updateState(SyslogParserState.MESSAGE_CONTENT);
      } else {
         updateState(SyslogParserState.STRUCTURED_DATA_ID);
      }
   }

   private void readStructuredDataId(byte b) {
      if (b == CharConstants.RIGHT_SQUARE_BRACKET) {
         readStructuredDataId(SyslogParserState.MESSAGE_CONTENT);
      } else if (b == CharConstants.SPACE) {
         readStructuredDataId(SyslogParserState.STRUCTURED_DATA_PARAM);
      } else {
         characterAccumulator.add(b);
      }
   }

   private void readStructuredDataId(SyslogParserState nextState) {
      messageBuilder.getStructuredDataBuilder().setId(characterAccumulator.getAll(CharsetUtil.US_ASCII));
      updateState(nextState);
   }

   private void readStructuredDataParam(byte b) {
      if (b == CharConstants.RIGHT_SQUARE_BRACKET) {
         updateState(SyslogParserState.MESSAGE_CONTENT);
      } else {
         characterAccumulator.add(b);
         updateState(SyslogParserState.STRUCTURED_DATA_PARAM_NAME);
      }
   }

   private void readStructuredDataParamName(byte b) throws SyslogParserException {
      if (doneAccumulating(b, CharConstants.EQUALS)) {
         // Switching buffers lets us store the name for now
         characterAccumulator.switchBuffers();

         // Skip the quotation mark, don't care
         skipNext(1);

         // Next is the value itself
         updateState(SyslogParserState.STRUCTURED_DATA_PARAM_VALUE);
      }
   }

   private void readStructuredDataParamValue(byte b) {
      if (doneAccumulatingEscaped(b, CharConstants.QUOTE)) {
         // Record our value as a UTF-8 String
         final String value = characterAccumulator.getAll(CharsetUtil.UTF_8);

         // Flip buffers to get back to the name
         characterAccumulator.switchBuffers();

         // Read the param name as an ASCII string
         final String name = characterAccumulator.getAll(CharsetUtil.US_ASCII);

         // Put the parameter
         messageBuilder.getStructuredDataBuilder().setParam(name, value);

         // Read the next parameter pair
         updateState(SyslogParserState.STRUCTURED_DATA_PARAM);
      }
   }

   private void readMessageContent(byte b) {
      boolean commitMessageContent = false;

      // If we're counting octets, this will be fine
      if (countOctets) {
         characterAccumulator.add(b);

         // If this is the last octet then we should commit the message
         commitMessageContent = octetsRemaining == 0;
      } else if (doneAccumulatingEscaped(b, CharConstants.LINE_FEED)) {
         commitMessageContent = true;
      }

      if (commitMessageContent) {
         // Record our value as a UTF-8 String
         final String value = characterAccumulator.getAll(CharsetUtil.UTF_8);

         // Put the parameter
         messageBuilder.setContent(value);

         // Read the next parameter pair
         updateState(SyslogParserState.STOP);
      }
   }
}
