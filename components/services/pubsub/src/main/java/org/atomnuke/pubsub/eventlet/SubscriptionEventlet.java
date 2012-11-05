package org.atomnuke.pubsub.eventlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.ws.rs.core.Response;
import javax.xml.datatype.DatatypeConfigurationException;
import net.jps.jx.JsonReader;
import net.jps.jx.JsonWriter;
import net.jps.jx.JxFactory;
import net.jps.jx.jackson.JacksonJxFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.pubsub.api.type.SubscriptionCategory;
import org.atomnuke.pubsub.api.type.SubscriptionContent;
import org.atomnuke.pubsub.api.type.SubscriptionDocument;
import org.atomnuke.sink.eps.eventlet.AtomEventlet;
import org.atomnuke.sink.eps.eventlet.AtomEventletException;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.util.lifecycle.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class SubscriptionEventlet implements AtomEventlet {

   private static final Logger LOG = LoggerFactory.getLogger(SubscriptionEventlet.class);
   private static final JxFactory JX_FACTORY;

   static {
      JxFactory jsonFactory = null;

      try {
         jsonFactory = new JacksonJxFactory();
      } catch (DatatypeConfigurationException configurationException) {
         LOG.error(configurationException.getMessage(), configurationException);
      }

      JX_FACTORY = jsonFactory;
   }
   private final JsonReader<SubscriptionContent> contentReader = JX_FACTORY.newReader(SubscriptionContent.class);
   private final JsonWriter<SubscriptionDocument> documentWriter = JX_FACTORY.newWriter(SubscriptionDocument.class);
   private final String subscriptionId, callbackUrl;
   private final HttpClient httpClient;

   public SubscriptionEventlet(HttpClient httpClient, String subscriptionId, String callbackUrl) {
      this.httpClient = httpClient;
      this.subscriptionId = subscriptionId;
      this.callbackUrl = callbackUrl;
   }

   @Override
   public void entry(Entry entry) throws AtomEventletException {
      if (!entry.content().type().equalsIgnoreCase("application/json")) {
         return;
      }

      SubscriptionContent content;

      try {
         content = contentReader.read(new ByteArrayInputStream(entry.content().toString().getBytes()));
      } catch (Exception ex) {
         LOG.warn("Unable to parse event content JSON: " + ex.getMessage());
         return;
      }

      final SubscriptionDocument payload = new SubscriptionDocument();

      payload.setId(subscriptionId);
      payload.setCallback(callbackUrl);
      payload.setContent(content);

      for (Category entryCategory : entry.categories()) {
         final SubscriptionCategory subscriptionCategory = new SubscriptionCategory();
         subscriptionCategory.setScheme(entryCategory.scheme());
         subscriptionCategory.setTerm(entryCategory.term());

         payload.addCategory(subscriptionCategory);
      }

      final HttpPost webhookPost = new HttpPost(callbackUrl);

      try {
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();
         documentWriter.write(payload, baos);

         webhookPost.setHeader("Content-Type", "application/json");
         webhookPost.setEntity(new ByteArrayEntity(baos.toByteArray()));

         final HttpResponse webhookResponse = httpClient.execute(webhookPost);

         if (webhookResponse.getStatusLine().getStatusCode() != Response.Status.ACCEPTED.getStatusCode()) {
            LOG.error("Potential error with webhook client. Recieved the following: " + webhookResponse.getStatusLine().toString());
         }
      } catch (Exception ex) {
         LOG.error(ex.getMessage(), ex);
      } finally {
         webhookPost.releaseConnection();
      }
   }

   @Override
   public void init(AtomTaskContext contextObject) throws InitializationException {
   }

   @Override
   public void destroy() {
   }
}
