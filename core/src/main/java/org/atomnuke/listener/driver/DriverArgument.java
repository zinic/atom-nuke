package org.atomnuke.listener.driver;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.listener.ListenerResult;

/**
 *
 * @author zinic
 */
public class DriverArgument {

   private final Feed feedPageToProcess;
   private final Entry entryToProcess;

   private ListenerResult capturedResult;

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

   public void setCapturedResult(ListenerResult capturedResult) {
      this.capturedResult = capturedResult;
   }

   public ListenerResult result() {
      return capturedResult;
   }
}
