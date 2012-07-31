package net.jps.nuke.source.crawler;

import java.io.File;
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
   private final File stateDirectory;
   private final Reader atomReader;

   public FeedCrawlerSourceFactory() {
      this(new File(System.getProperty("java.io.tmpdir")));
   }

   public FeedCrawlerSourceFactory(File stateDirectory) {
      this(stateDirectory, new DefaultHttpClient(), new SaxAtomParser());
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
