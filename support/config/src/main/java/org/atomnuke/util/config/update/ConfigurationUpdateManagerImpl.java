package org.atomnuke.util.config.update;

import org.atomnuke.util.config.io.ConfigurationManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.remote.CancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: SRP
 *
 * @author zinic
 */
public class ConfigurationUpdateManagerImpl implements ConfigurationUpdateManager {

   private static final Logger LOG = LoggerFactory.getLogger(ConfigurationUpdateManagerImpl.class);

   private final Map<String, UpdateContext> updateContexts;
   private final ReclamationHandler reclamationHandler;

   public ConfigurationUpdateManagerImpl(ReclamationHandler reclamationHandler) {
      updateContexts = new HashMap<String, UpdateContext>();

      this.reclamationHandler = reclamationHandler;
   }

   @Override
   public void destroy() {
      updateContexts.clear();
   }

   @Override
   public void update() {
      for (UpdateContext contextToDispatch : getDispatchList()) {
         try {
            contextToDispatch.dispatch();
         } catch (ConfigurationException ce) {
            LOG.error("Exception during configuration read: " + ce.getMessage(), ce);
         }
      }
   }

   private synchronized List<UpdateContext> getDispatchList() {
      final List<UpdateContext> dispatchList = new LinkedList<UpdateContext>();

      for (Iterator<Map.Entry<String, UpdateContext>> updateContextItr = updateContexts.entrySet().iterator(); updateContextItr.hasNext();) {
         final Map.Entry<String, UpdateContext> entry = updateContextItr.next();
         final UpdateContext currentCtx = entry.getValue();

         // Cancellation may happen at anytime, remotely - check for it on every poll
         if (currentCtx.cancellationRemote().canceled()) {
            updateContextItr.remove();
            continue;
         }

         try {
            if (currentCtx.updated()) {
               dispatchList.add(currentCtx);
            }
         } catch (ConfigurationException ce) {
            LOG.error("Exception during configuration update check: " + ce.getMessage(), ce);
         }
      }

      return dispatchList;
   }

   @Override
   public synchronized <T> ConfigurationContext<T> register(String name, ConfigurationManager<T> configurationManager) {
      final ConfigurationContext context = updateContexts.get(name);

      if (context != null) {
         LOG.error("Configuration already under update watch. This registration attempt will be ignored for: " + name);

         return context;
      }

      final CancellationRemote cancellationRemote = reclamationHandler.watch(configurationManager);
      final UpdateContext<T> newContext = new UpdateContext<T>(configurationManager, cancellationRemote);

      updateContexts.put(name, newContext);

      return newContext;
   }

   @Override
   public synchronized UpdateContext get(String name) {
      return updateContexts.get(name);
   }
}
