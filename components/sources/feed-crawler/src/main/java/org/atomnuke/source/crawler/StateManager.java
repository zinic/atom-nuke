package org.atomnuke.source.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class StateManager {
   
   private static final Logger LOG = LoggerFactory.getLogger(StateManager.class);
   
   private final File stateFile;

   public StateManager(File stateFile) {
      this.stateFile = stateFile;
   }
   
   public String loadState() {
      String nextLocation = null;
      
      if (stateFile != null) {
         try {
            final BufferedReader fin = new BufferedReader(new FileReader(stateFile));
            nextLocation = fin.readLine();

            fin.close();
         } catch (FileNotFoundException fnfe) {
            // Suppress this
         } catch (IOException ioe) {
            LOG.error("Failed to load statefile \"" + stateFile.getAbsolutePath() + "\" - Reason: " + ioe.getMessage());
         }
      }
      
      return nextLocation;
   }

   public void writeState(String nextLocation) {
      if (stateFile != null) {
         try {
            final FileWriter fout = new FileWriter(stateFile);
            fout.append(nextLocation);
            fout.append("\n");

            fout.close();
         } catch (IOException ioe) {
            LOG.error("Failed to write statefile \"" + stateFile.getAbsolutePath() + "\" - Reason: " + ioe.getMessage());
         }
      }
   }
}
