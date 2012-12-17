package org.atomnuke.source.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.atomnuke.atom.io.AtomReadException;
import org.atomnuke.atom.io.AtomReaderFactory;
import org.atomnuke.atom.io.ReaderResult;
import org.atomnuke.atom.io.reader.sax.SaxAtomReaderFactory;
import org.atomnuke.atom.model.Link;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.result.AtomSourceResult;
import org.atomnuke.source.result.AtomSourceResultImpl;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.introspection.ServicesInterrogator;
import org.atomnuke.source.action.ActionType;
import org.atomnuke.source.action.AtomSourceActionImpl;
import org.atomnuke.source.crawler.config.model.FeedCrawlerTargets;
import org.atomnuke.source.crawler.config.model.FeedTarget;
import org.atomnuke.source.crawler.config.model.HttpHeader;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.ConfigurationManager;
import org.atomnuke.util.config.io.file.FileConfigurationManager;
import org.atomnuke.util.config.io.marshall.ConfigurationMarshaller;
import org.atomnuke.util.config.io.marshall.jaxb.JaxbConfigurationMarhsaller;
import org.atomnuke.util.config.update.ConfigurationContext;
import org.atomnuke.util.config.update.ConfigurationUpdateService;
import org.atomnuke.util.config.update.listener.ConfigurationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class FeedCrawlerSource implements AtomSource {

   private static final QName _CrawlerTargets_QNAME = new QName("http://atomnuke.org/configuration/feed-crawler", "crawler-targets");
   private static final String CFG_MANAGER_NAME = "FeedCrawlerTargetsConfigurationManager";
   private static final Logger LOG = LoggerFactory.getLogger(FeedCrawlerSource.class);
   
   private final AtomReaderFactory atomReaderFactory;
   private final Map<String, String> httpHeaders;
   
   private String nextLocation, myActorId;
   private HttpClient httpClient;
   private File stateFile;

   public FeedCrawlerSource() {
      atomReaderFactory = new SaxAtomReaderFactory();
      httpHeaders = new HashMap<String, String>();
   }

   public ConfigurationContext<FeedCrawlerTargets> getConfigurationContext(ServicesInterrogator interrogator, String configDir) throws JAXBException, ServiceUnavailableException, ConfigurationException {
      final ConfigurationUpdateService cfgService = interrogator.firstAvailable(ConfigurationUpdateService.class);

      ConfigurationContext<FeedCrawlerTargets> ctx = cfgService.get(CFG_MANAGER_NAME);

      if (ctx == null) {
         final File configurationFile = new File(configDir, "feed-crawler-targets.cfg.xml");
         final ConfigurationMarshaller<FeedCrawlerTargets> marshallerInstance = JaxbConfigurationMarhsaller.newJaxConfigurationMarshaller(FeedCrawlerTargets.class, _CrawlerTargets_QNAME);
         final ConfigurationManager<FeedCrawlerTargets> targetsConfigurationManager = new FileConfigurationManager<FeedCrawlerTargets>(marshallerInstance, configurationFile);

         ctx = cfgService.register(CFG_MANAGER_NAME, targetsConfigurationManager);
      }

      return ctx;
   }

   @Override
   public void init(AtomTaskContext tc) throws InitializationException {
      myActorId = tc.actorId();

      try {
         final ConfigurationContext<FeedCrawlerTargets> configContext = getConfigurationContext(tc.services(), tc.environment().configurationDirectory());

         configContext.addListener(new ConfigurationListener<FeedCrawlerTargets>() {
            @Override
            public void updated(FeedCrawlerTargets configuration) throws ConfigurationException {
               configUpdate(configuration);
            }
         });

         httpClient = tc.services().firstAvailable(HttpClient.class);
      } catch (Exception sue) {
         throw new InitializationException(sue);
      }

      loadState();
   }

   @Override
   public void destroy() {
      writeState();
   }

   private void loadState() {
      if (stateFile != null) {
         try {
            final BufferedReader fin = new BufferedReader(new FileReader(stateFile));
            nextLocation = fin.readLine();

            fin.close();
         } catch (FileNotFoundException fnfe) {
            // Suppress this
         } catch (IOException ioe) {
            LOG.error("Failed to load statefile \"" + stateFile.getAbsolutePath() + "\" - Reason: " + ioe.getMessage());
         }
      }
   }

   private void writeState() {
      if (stateFile != null) {
         try {
            final FileWriter fout = new FileWriter(stateFile);
            fout.append(nextLocation);
            fout.append("\n");

            fout.close();
         } catch (IOException ioe) {
            LOG.error("Failed to write statefile \"" + stateFile.getAbsolutePath() + "\" - Reason: " + ioe.getMessage());
         }
      }
   }

   @Override
   public synchronized AtomSourceResult poll() throws AtomSourceException {
      if (nextLocation != null) {
         try {
            final ReaderResult readResult = read(nextLocation);

            if (readResult.isFeed()) {
               for (Link pageLink : readResult.getFeed().links()) {
                  if (pageLink.rel().equalsIgnoreCase("previous")) {
                     nextLocation = pageLink.href();
                     writeState();
                     break;
                  }
               }
               
               return new AtomSourceResultImpl(new AtomSourceActionImpl(ActionType.HAS_NEXT), readResult.getFeed());
            }
         } catch (Exception ex) {
            throw new AtomSourceException("Failed to poll ATOM feed: \"" + nextLocation + "\"", ex);
         }
      }
      
      return new AtomSourceResultImpl(new AtomSourceActionImpl(ActionType.SLEEP));
   }

   private synchronized void configUpdate(FeedCrawlerTargets configuration) {
      for (FeedTarget feedTarget : configuration.getFeed()) {
         if (myActorId.equals(feedTarget.getActorRef())) {
            // This is us, let's configure
            nextLocation = feedTarget.getHref();
            httpHeaders.clear();

            if (feedTarget.getHttpOptions() != null) {
               if (feedTarget.getHttpOptions().getHeaders() != null) {
                  for (HttpHeader header : feedTarget.getHttpOptions().getHeaders().getHeader()) {
                     httpHeaders.put(header.getName(), header.getValue());
                  }
               }
            }

            if (feedTarget.getFsOptions() != null) {
               stateFile = new File(URI.create(feedTarget.getFsOptions().getStateFile()));
            }
            
            break;
         }
      }
   }

   private ReaderResult read(String location) throws AtomReadException, IOException {
      final HttpGet httpGet = new HttpGet(location);

      for (Map.Entry<String, String> headerToAdd : httpHeaders.entrySet()) {
         httpGet.addHeader(headerToAdd.getKey(), headerToAdd.getValue());
      }

      ReaderResult readResult = null;
      InputStream inputStream = null;

      try {
         final HttpResponse response = httpClient.execute(httpGet);
         final HttpEntity entity = response.getEntity();

         readResult = atomReaderFactory.getInstance().read(entity.getContent());
      } finally {
         if (inputStream != null) {
            inputStream.close();
         }
      }

      return readResult;
   }
}
