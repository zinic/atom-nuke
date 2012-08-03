package net.jps.nuke.util.cli.command.result;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author zinic
 */
public class ExceptionResult implements CommandResult {

   private final Throwable throwable;

   public ExceptionResult(Throwable throwable) {
      this.throwable = throwable;
   }

   @Override
   public int getStatusCode() {
      return -22;
   }

   @Override
   public String getStringResult() {
      String message = throwable.getMessage();
      
      if (message == null) {
         final StringWriter stringWriter = new StringWriter();
         throwable.printStackTrace(new PrintWriter(stringWriter));
         
         message = stringWriter.toString();
      }
      
      return message;
   }
}
