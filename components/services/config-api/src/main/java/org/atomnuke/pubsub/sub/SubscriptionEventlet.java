package org.atomnuke.pubsub.sub;

import java.io.ByteArrayInputStream;
import javax.xml.datatype.DatatypeConfigurationException;
import net.jps.jx.JsonReader;
import net.jps.jx.jackson.JacksonJxFactory;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.control.api.type.SubscriptionCategory;
import org.atomnuke.control.api.type.SubscriptionContent;
import org.atomnuke.control.api.type.SubscriptionDocument;
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

   public static final String SUBSCRIPTION_ID_PROPERTY = "org.atomnuke.pubsub.sub.SubscriptionId";
   public static final String CALLBACK_PROPERTY = "org.atomnuke.pubsub.sub.CalllbackUrl";

   private static final JsonReader<SubscriptionContent> CONTENT_JSON_READER;

   static {
      JsonReader<SubscriptionContent> contentReader = null;

      try {
         contentReader = new JacksonJxFactory().newReader(SubscriptionContent.class);
      } catch (DatatypeConfigurationException configurationException) {
         LOG.error(configurationException.getMessage(), configurationException);
      }

      CONTENT_JSON_READER = contentReader;
   }

   private String subscriptionId, callbackUrl;

   @Override
   public void entry(Entry entry) throws AtomEventletException {
      if (!entry.content().type().equalsIgnoreCase("application/json")) {
         return;
      }

      SubscriptionContent content;

      try {
         content = CONTENT_JSON_READER.read(new ByteArrayInputStream(entry.content().toString().getBytes()));
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
   }

   @Override
   public void init(AtomTaskContext contextObject) throws InitializationException {
      subscriptionId = contextObject.parameters().get(SUBSCRIPTION_ID_PROPERTY);
      callbackUrl = contextObject.parameters().get(CALLBACK_PROPERTY);
   }

   @Override
   public void destroy() {
   }
}
