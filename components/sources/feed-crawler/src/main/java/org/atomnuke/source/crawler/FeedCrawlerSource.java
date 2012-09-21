package org.atomnuke.source.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.atomnuke.atom.model.Link;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.result.AtomSourceResult;
import org.atomnuke.task.context.TaskContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.atomnuke.atom.io.AtomReadException;
import org.atomnuke.atom.io.AtomReaderFactory;
import org.atomnuke.atom.io.ReaderResult;
import org.atomnuke.source.result.AtomSourceResultImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class FeedCrawlerSource implements AtomSource {

   private static final Logger LOG = LoggerFactory.getLogger(FeedCrawlerSource.class);
   private static final String STATE_FILE_EXTENSION = ".state";
   private final AtomReaderFactory atomReaderFactory;
   private final HttpClient httpClient;
   private final File stateFile;
   private String location;

   public FeedCrawlerSource(String name, String initialLocation, File stateDirectory, HttpClient httpClient, AtomReaderFactory atomReaderFactory) {
      this.httpClient = httpClient;
      this.stateFile = new File(stateDirectory, name + STATE_FILE_EXTENSION);
      this.atomReaderFactory = atomReaderFactory;
      this.location = initialLocation;
   }

   @Override
   public void init(TaskContext tc) throws InitializationException {
      loadState();
   }

   @Override
   public void destroy() throws DestructionException {
      writeState();
   }

   private void loadState() {
      try {
         final BufferedReader fin = new BufferedReader(new FileReader(stateFile));
         location = fin.readLine();

         fin.close();
      } catch (FileNotFoundException fnfe) {
         // Suppress this
      } catch (IOException ioe) {
         LOG.error("Failed to load statefile \"" + stateFile.getAbsolutePath() + "\" - Reason: " + ioe.getMessage());
      }
   }

   private void writeState() {
      try {
         final FileWriter fout = new FileWriter(stateFile);
         fout.append(location);
         fout.append("\n");

         fout.close();
      } catch (IOException ioe) {
         LOG.error("Failed to write statefile \"" + stateFile.getAbsolutePath() + "\" - Reason: " + ioe.getMessage());
      }
   }

   @Override
   public AtomSourceResult poll() throws AtomSourceException {
      try {
         final AtomSourceResult result = read(location);

         if (result.isFeedPage()) {
            for (Link pageLink : result.feed().links()) {
               if (pageLink.rel().equalsIgnoreCase("previous")) {
                  location = pageLink.href();
                  writeState();
                  break;
               }
            }
         }

         return result;
      } catch (Exception ex) {
         throw new AtomSourceException("Failed to poll ATOM feed: \"" + location + "\"", ex);
      }
   }

   private AtomSourceResult read(String location) throws IOException, AtomReadException {
      final HttpGet httpGet = new HttpGet(location);
      InputStream inputStream = null;

      try {
         final HttpResponse response = httpClient.execute(httpGet);
         final HttpEntity entity = response.getEntity();
         final ReaderResult readResult = atomReaderFactory.getInstance().read(entity.getContent());

         if (readResult.isFeed()) {
            return new AtomSourceResultImpl(readResult.getFeed());
         } else {
            return new AtomSourceResultImpl(readResult.getEntry());
         }
      } finally {
         if (inputStream != null) {
            inputStream.close();
         }
      }
   }
}
