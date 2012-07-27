package net.jps.nuke.crawler.task;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.jps.nuke.crawler.remote.CancellationRemote;
import net.jps.nuke.crawler.remote.CancellationRemoteImpl;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import net.jps.nuke.atom.AtomParserException;
import net.jps.nuke.atom.Reader;
import net.jps.nuke.atom.Result;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.atom.sax.impl.SaxAtomParser;
import net.jps.nuke.crawler.task.driver.RegisteredListenerDriver;
import net.jps.nuke.crawler.task.driver.AtomListenerDriver;
import net.jps.nuke.util.TimeValue;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author zinic
 */
public class ManagedTaskImpl extends CrawlerTaskImpl implements ManagedTask {

   private final ExecutorService executorService;
   private final HttpClient httpClient;
   private final Reader atomReader;
   private final UUID id;

   public ManagedTaskImpl(TimeValue interval, ExecutorService executorService) {
      this(new DefaultHttpClient(), new SaxAtomParser(), interval, new CancellationRemoteImpl(), executorService);
   }

   public ManagedTaskImpl(HttpClient httpClient, Reader atomReader, TimeValue interval, CancellationRemote cancelationRemote, ExecutorService executorService) {
      super(interval, cancelationRemote);

      this.executorService = executorService;
      this.httpClient = httpClient;
      this.atomReader = atomReader;
      this.id = UUID.randomUUID();
   }

   private Feed read(String location) throws IOException, AtomParserException {
      final HttpGet httpGet = new HttpGet(location);
      InputStream inputStream = null;

      try {
         final HttpResponse response = httpClient.execute(httpGet);
         final HttpEntity entity = response.getEntity();
         final Result parsedResult = atomReader.read(entity.getContent());

         return parsedResult.getFeed();
      } finally {
         if (inputStream != null) {
            inputStream.close();
         }
      }
   }

   private synchronized List<RegisteredListener> activeListeners() {
      final List<RegisteredListener> activeListeners = new LinkedList<RegisteredListener>();

      for (Iterator<RegisteredListener> listenerIterator = listeners().iterator(); listenerIterator.hasNext();) {
         final RegisteredListener registeredListener = listenerIterator.next();

         if (registeredListener.cancellationRemote().canceled()) {
            listenerIterator.remove();
         } else {
            activeListeners.add(registeredListener);
         }
      }

      return activeListeners;
   }

   @Override
   public void followNow(String location) {
      setLocation(location);
   }

   @Override
   public void followLater(String location) {
      followNow(location);
      setTimestamp(TimeValue.now());
   }

   @Override
   public void run() {
      try {
         final Feed nextPage = read(location());

         for (RegisteredListener listener : activeListeners()) {
            final RegisteredListenerDriver listenerDriver = new AtomListenerDriver(listener, nextPage);

            executorService.submit(listenerDriver);
         }
      } catch (Exception ex) {
         // TODO:Log
         ex.printStackTrace(System.err);
      }
   }

   @Override
   public UUID id() {
      return id;
   }

   @Override
   public int hashCode() {
      int hash = 5;
      hash = 59 * hash + id().hashCode();
      return hash;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj != null && ManagedTask.class == obj.getClass()) {
         final ManagedTask other = (ManagedTask) obj;

         return id().equals(other.id());
      }

      return false;
   }
}
