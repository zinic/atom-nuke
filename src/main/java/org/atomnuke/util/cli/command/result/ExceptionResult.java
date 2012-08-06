package org.atomnuke.util.cli.command.result;

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

   public Throwable throwable() {
      return throwable;
   }

   @Override
   public boolean shouldExit() {
      return true;
   }

   @Override
   public int getStatusCode() {
      return -22;
   }

   @Override
   public String getStringResult() {
      final StringWriter stringWriter = new StringWriter();
      stringWriter.write(throwable.getMessage());
      stringWriter.write("\n");
      
      throwable.printStackTrace(new PrintWriter(stringWriter));

      return stringWriter.toString();
   }
}
