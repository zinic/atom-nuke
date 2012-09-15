package org.atomnuke.source.crawler;

import java.io.File;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.atomnuke.atom.io.AtomReaderFactory;
import org.atomnuke.atom.io.reader.sax.SaxAtomReaderFactory;
import org.atomnuke.source.AtomSource;

/**
 *
 * @author zinic
 */
public class FeedCrawlerSourceFactory {

   private final AtomReaderFactory atomReaderFactory;
   private final HttpClient httpClient;
   private final File stateDirectory;

   public FeedCrawlerSourceFactory() {
      this(new File(System.getProperty("java.io.tmpdir")));
   }

   public FeedCrawlerSourceFactory(File stateDirectory) {
      this(stateDirectory, new DefaultHttpClient(new PoolingClientConnectionManager()), new SaxAtomReaderFactory());
   }

   public FeedCrawlerSourceFactory(File stateDirectory, HttpClient httpClient, AtomReaderFactory atomReaderFactory) {
      this.stateDirectory = stateDirectory;
      this.httpClient = httpClient;
      this.atomReaderFactory = atomReaderFactory;
   }

   public AtomSource newCrawlerSource(String crawlerName, String initialLocation) {
      return new FeedCrawlerSource(crawlerName, initialLocation, stateDirectory, httpClient, atomReaderFactory);
   }
}
