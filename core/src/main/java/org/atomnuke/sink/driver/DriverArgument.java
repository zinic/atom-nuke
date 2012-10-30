package org.atomnuke.sink.driver;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.sink.SinkResult;

/**
 *
 * @author zinic
 */
public class DriverArgument {

   private final Feed feedPageToProcess;
   private final Entry entryToProcess;

   private SinkResult capturedResult;

   public DriverArgument(Feed feedPageToProcess, Entry entryToProcess) {
      this.feedPageToProcess = feedPageToProcess;
      this.entryToProcess = entryToProcess;

      capturedResult = null;
   }

   public Feed feed() {
      return feedPageToProcess;
   }

   public Entry entry() {
      return entryToProcess;
   }

   public void setCapturedResult(SinkResult capturedResult) {
      this.capturedResult = capturedResult;
   }

   public SinkResult result() {
      return capturedResult;
   }
}
