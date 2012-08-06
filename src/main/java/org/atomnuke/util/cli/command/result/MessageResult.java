package org.atomnuke.util.cli.command.result;

/**
 *
 * @author zinic
 */
public class MessageResult implements CommandResult, MessageBuilder {

   public final StringBuilder messageBuilder;

   public MessageResult() {
      this("");
   }

   public MessageResult(String message) {
      this.messageBuilder = new StringBuilder(message);
   }

   @Override
   public boolean shouldExit() {
      return false;
   }

   @Override
   public MessageBuilder append(double d) {
      messageBuilder.append(d);
      return this;
   }

   @Override
   public MessageBuilder append(float f) {
      messageBuilder.append(f);
      return this;
   }

   @Override
   public MessageBuilder append(long lng) {
      messageBuilder.append(lng);
      return this;
   }

   @Override
   public MessageBuilder append(int i) {
      messageBuilder.append(i);
      return this;
   }

   @Override
   public MessageBuilder append(char c) {
      messageBuilder.append(c);
      return this;
   }

   @Override
   public MessageBuilder append(boolean b) {
      messageBuilder.append(b);
      return this;
   }

   @Override
   public MessageBuilder append(char[] str, int offset, int len) {
      messageBuilder.append(str, offset, len);
      return this;
   }

   @Override
   public MessageBuilder append(char[] str) {
      messageBuilder.append(str);
      return this;
   }

   @Override
   public MessageBuilder append(CharSequence s, int start, int end) {
      messageBuilder.append(s, start, end);
      return this;
   }

   @Override
   public MessageBuilder append(CharSequence s) {
      messageBuilder.append(s);
      return this;
   }

   @Override
   public MessageBuilder append(StringBuffer sb) {
      messageBuilder.append(sb);
      return this;
   }

   @Override
   public MessageBuilder append(String str) {
      messageBuilder.append(str);
      return this;
   }

   @Override
   public MessageBuilder append(Object obj) {
      messageBuilder.append(obj);
      return this;
   }

   @Override
   public String getStringResult() {
      return messageBuilder.toString();
   }

   @Override
   public int getStatusCode() {
      return StatusCodes.OK.intValue();
   }
}
