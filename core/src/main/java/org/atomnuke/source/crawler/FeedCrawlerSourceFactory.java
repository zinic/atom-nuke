package org.atomnuke.source.crawler;

import java.io.File;
import org.atomnuke.atom.Reader;
import org.atomnuke.atom.sax.impl.SaxAtomParser;
import org.atomnuke.source.AtomSource;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

/**
 *
 * @author zinic
 */
public class FeedCrawlerSourceFactory {

   private final HttpClient httpClient;
   private final File stateDirectory;
   private final Reader atomReader;
   
   public FeedCrawlerSourceFactory() {
      this(new File(System.getProperty("java.io.tmpdir")));
   }

   public FeedCrawlerSourceFactory(File stateDirectory) {
      this(stateDirectory, new DefaultHttpClient(new PoolingClientConnectionManager()), new SaxAtomParser());
   }

   public FeedCrawlerSourceFactory(File stateDirectory, HttpClient httpClient, Reader atomReader) {
      this.stateDirectory = stateDirectory;
      this.httpClient = httpClient;
      this.atomReader = atomReader;
   }

   public AtomSource newCrawlerSource(String crawlerName, String initialLocation) {
      return new FeedCrawlerSource(crawlerName, initialLocation, stateDirectory, httpClient, atomReader);
   }
}
