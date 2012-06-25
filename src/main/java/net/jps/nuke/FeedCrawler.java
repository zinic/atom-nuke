package net.jps.nuke;

import net.jps.nuke.listener.FeedListener;
import net.jps.nuke.listener.ListenerResult;
import com.rackspace.papi.commons.util.io.RawInputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.ParseException;
import org.apache.abdera.parser.ParserFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

/**
 *
 * @author zinic
 */
public class FeedCrawler {

   private final ParserFactory parserFactory;
   private final FeedListener feedListener;
   private final HttpClient httpClient;
   private final File stateFile;

   public FeedCrawler(ParserFactory parserFactory, FeedListener feedListener, HttpClient httpClient, File stateFile) {
      this.parserFactory = parserFactory;
      this.feedListener = feedListener;
      this.httpClient = httpClient;
      this.stateFile = stateFile;
   }

   public void go(String origin) {
      String targetUri = origin;
      boolean readNext = true;

      try {
         while (readNext) {
            final ListenerResult result = readNext(targetUri);
            
            switch(result.getAction()) {
               case FOLLOW_LINK:
                  targetUri = result.getLink().getHref().toString();
                  writeMarker(targetUri);
                  
                  break;
                  
               case HALT:
               default:
                  readNext = false;
            }
         }
      } catch (IOException ioe) {
         ioe.printStackTrace(System.err);
      }
   }

   private ListenerResult readNext(String targetUri) throws IOException {
      final HttpGet httpGet = new HttpGet(targetUri);

      InputStream inputStream = null;
      Document<Feed> feedDocument = null;

      try {
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();
         final HttpResponse response = httpClient.execute(httpGet);
         final HttpEntity entity = response.getEntity();
         
         inputStream = entity.getContent();
         RawInputStreamReader.instance().copyTo(inputStream, baos);
         
         feedDocument = parserFactory.getParser().parse(new ByteArrayInputStream(baos.toByteArray()));
      } catch (IOException ioe) {
         ioe.printStackTrace(System.err);
      } catch (ParseException pe) {
         pe.printStackTrace(System.err);
      } finally {
         if (inputStream != null) {
            inputStream.close();
         }
      }

      return feedDocument != null ? feedListener.readPage(feedDocument.getRoot()) : ListenerResult.halt("Feed document was null.");
   }

   private void writeMarker(String marker) throws IOException {
      FileOutputStream outputStream = null;

      try {
         outputStream = new FileOutputStream(stateFile);

         outputStream.write(marker.getBytes());
         outputStream.write("\n".getBytes());
      } finally {
         if (outputStream != null) {
            outputStream.close();
         }
      }
   }
}
