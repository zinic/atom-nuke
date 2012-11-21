package org.atomnuke.service.http.client;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.service.ServiceContext;
import org.atomnuke.service.runtime.AbstractRuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gee, that was easy...
 *
 * @author zinic
 */
@NukeService
public class HttpClientService extends AbstractRuntimeService {

   private static final Logger LOG = LoggerFactory.getLogger(HttpClientService.class);

   private HttpClient httpClient;

   public HttpClientService() {
      super(HttpClient.class);
   }

   @Override
   public Object instance() {
      return httpClient;
   }

   @Override
   public void init(ServiceContext contextObject) throws InitializationException {
      httpClient = new DefaultHttpClient(new PoolingClientConnectionManager());
   }

   @Override
   public void destroy() {
      try {
         httpClient.getConnectionManager().shutdown();
      } catch (Exception ex) {
         LOG.error(ex.getMessage(), ex);
      }
   }
}
