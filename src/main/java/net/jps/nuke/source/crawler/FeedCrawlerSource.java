package net.jps.nuke.source.crawler;

import java.io.IOException;
import java.io.InputStream;
import net.jps.nuke.atom.AtomParserException;
import net.jps.nuke.atom.Reader;
import net.jps.nuke.atom.model.Link;
import net.jps.nuke.source.AtomSource;
import net.jps.nuke.source.AtomSourceException;
import net.jps.nuke.source.AtomSourceResult;
import net.jps.nuke.source.impl.ParserSourceResultImpl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

/**
 *
 * @author zinic
 */
public class FeedCrawlerSource implements AtomSource {

   private final HttpClient httpClient;
   private final Reader atomReader;
   private String location;

   public FeedCrawlerSource(String location, HttpClient httpClient, Reader atomReader) {
      this.httpClient = httpClient;
      this.atomReader = atomReader;
      this.location = location;
   }

   public AtomSourceResult poll() throws AtomSourceException {
      try {
         final AtomSourceResult result = read(location);

         if (result.isFeedPage()) {
            for (Link pageLink : result.feed().links()) {
               if (pageLink.rel().equalsIgnoreCase("previous")) {
                  location = pageLink.href();
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
