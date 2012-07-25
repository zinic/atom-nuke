package net.jps.nuke.crawler.task.driver;

import net.jps.nuke.crawler.task.CrawlerTask;
import com.rackspace.papi.commons.util.io.RawInputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import net.jps.nuke.atom.AtomParserException;
import net.jps.nuke.atom.Reader;
import net.jps.nuke.atom.Result;
import net.jps.nuke.listener.ListenerResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

/**
 *
 * @author zinic
 */
public class NukeCrawlerTaskDriver implements CrawlerTaskDriver {

   private final HttpClient httpClient;
   private final Reader atomReader;

   public NukeCrawlerTaskDriver(HttpClient httpClient, Reader atomReader) {
      this.httpClient = httpClient;
      this.atomReader = atomReader;
   }

   @Override
   public ListenerResult drive(CrawlerTask task) throws IOException, AtomParserException {
      final HttpGet httpGet = new HttpGet(task.location());
      InputStream inputStream = null;

      try {
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();
         final HttpResponse response = httpClient.execute(httpGet);
         final HttpEntity entity = response.getEntity();

         inputStream = entity.getContent();
         RawInputStreamReader.instance().copyTo(inputStream, baos);

         final Result parsedResult = atomReader.read(new ByteArrayInputStream(baos.toByteArray()));
         return parsedResult.getFeed() != null ? task.listener().readPage(parsedResult.getFeed()) : ListenerResult.halt("Feed document was null.");
      } catch (IOException ioe) {
         // TODO:Log
         ioe.printStackTrace(System.err);

         return ListenerResult.halt(ioe.getMessage());
      } finally {
         if (inputStream != null) {
            inputStream.close();
         }
      }
   }
}
