package net.jps.nuke.source.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import net.jps.nuke.atom.AtomParserException;
import net.jps.nuke.atom.Reader;
import net.jps.nuke.atom.model.Link;
import net.jps.nuke.task.lifecycle.DestructionException;
import net.jps.nuke.task.lifecycle.InitializationException;
import net.jps.nuke.source.AtomSource;
import net.jps.nuke.source.AtomSourceException;
import net.jps.nuke.source.AtomSourceResult;
import net.jps.nuke.source.impl.ParserSourceResultImpl;
import net.jps.nuke.task.context.TaskContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class FeedCrawlerSource implements AtomSource {

   private static final Logger LOG = LoggerFactory.getLogger(FeedCrawlerSource.class);
   private static final String STATE_FILE_EXTENSION = ".state";
   private final HttpClient httpClient;
   private final File stateFile;
   private final Reader atomReader;
   private String location;

   public FeedCrawlerSource(String name, String initialLocation, File stateDirectory, HttpClient httpClient, Reader atomReader) {
      this.httpClient = httpClient;
      this.stateFile = new File(stateDirectory, name + STATE_FILE_EXTENSION);
      this.atomReader = atomReader;
      this.location = initialLocation;
   }

   @Override
   public void init(TaskContext tc) throws InitializationException {
      loadState();
   }

   @Override
   public void destroy(TaskContext tc) throws DestructionException {
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

   private AtomSourceResult read(String location) throws IOException, AtomParserException {
      final HttpGet httpGet = new HttpGet(location);
      InputStream inputStream = null;

      try {
         final HttpResponse response = httpClient.execute(httpGet);
         final HttpEntity entity = response.getEntity();

         return new ParserSourceResultImpl(atomReader.read(entity.getContent()));
      } finally {
         if (inputStream != null) {
            inputStream.close();
         }
      }
   }
}
