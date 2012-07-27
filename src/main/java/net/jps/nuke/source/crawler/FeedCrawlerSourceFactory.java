package net.jps.nuke.source.crawler;

import net.jps.nuke.atom.Reader;
import net.jps.nuke.atom.sax.impl.SaxAtomParser;
import net.jps.nuke.source.AtomSource;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author zinic
 */
public class FeedCrawlerSourceFactory {

   private final HttpClient httpClient;
   private final Reader atomReader;

   public FeedCrawlerSourceFactory() {
      this(new DefaultHttpClient(), new SaxAtomParser());
   }

   public FeedCrawlerSourceFactory(HttpClient httpClient, Reader atomReader) {
      this.httpClient = httpClient;
      this.atomReader = atomReader;
   }

   public AtomSource newCrawlerSource(String location) {
      return new FeedCrawlerSource(location, httpClient, atomReader);
   }
}
