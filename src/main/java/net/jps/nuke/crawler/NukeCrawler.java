package net.jps.nuke.crawler;

import com.rackspace.papi.commons.util.io.RawInputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import net.jps.nuke.listener.ListenerResult;
import net.jps.nuke.atom.AtomParserException;
import net.jps.nuke.atom.Reader;
import net.jps.nuke.atom.Result;
import net.jps.nuke.listener.FeedListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

/**
 *
 * @author zinic
 */
public class NukeCrawler implements FeedCrawler {

   private final FeedListener feedListener;
   private final HttpClient httpClient;
   private final Reader atomReader;
   private final File stateFile;

   public NukeCrawler(FeedListener feedListener, HttpClient httpClient, Reader atomReader, File stateFile) {
      this.feedListener = feedListener;
      this.httpClient = httpClient;
      this.atomReader = atomReader;
      this.stateFile = stateFile;
   }

   @Override
   public void crawl(String origin) {
      String targetUri = origin;
      boolean readNext = true;

      try {
         while (readNext) {
            final ListenerResult result = readNext(targetUri);

            switch (result.getAction()) {
               case FOLLOW_LINK:
                  targetUri = result.getLink().href();
                  writeMarker(targetUri);

                  break;

               case HALT:
               default:
                  readNext = false;
            }
            
            break;
         }
      } catch (Exception ioe) {
         ioe.printStackTrace(System.err);
      }
   }

   private ListenerResult readNext(String targetUri) throws IOException, AtomParserException {
      final HttpGet httpGet = new HttpGet(targetUri);
      InputStream inputStream = null;

      try {
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();
         final HttpResponse response = httpClient.execute(httpGet);
         final HttpEntity entity = response.getEntity();

         inputStream = entity.getContent();
         RawInputStreamReader.instance().copyTo(inputStream, baos);

         final Result parsedResult = atomReader.read(new ByteArrayInputStream(baos.toByteArray()));
         return parsedResult.getFeed() != null ? feedListener.readPage(parsedResult.getFeed()) : ListenerResult.halt("Feed document was null.");
      } catch (IOException ioe) {
         ioe.printStackTrace(System.err);

         return ListenerResult.halt(ioe.getMessage());
      } finally {
         if (inputStream != null) {
            inputStream.close();
         }
      }
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
