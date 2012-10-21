package org.atomnuke.container.service.config;

import java.util.concurrent.TimeUnit;
import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.service.ResolutionAction;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.manager.Tasker;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;
import org.atomnuke.util.config.update.ConfigurationUpdateManagerImpl;
import org.atomnuke.util.config.update.ConfigurationUpdateRunnable;
import org.atomnuke.util.lifecycle.InitializationException;
import org.atomnuke.util.service.ServiceHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
@NukeService
public class ConfigurationService implements Service {

   public static String CFG_POLLER_PROPERTY_KEY = "org.atomnuke.container.service.config.ConfigurationService.poll_interval_ms";
   public static String CFG_SERVICE_NAME = "org.atomnuke.container.service.config.ConfigurationService";

   private static final TimeValue DEFAULT_POLL_INTERVAL = new TimeValue(15, TimeUnit.SECONDS);
   private static final Logger LOG = LoggerFactory.getLogger(ConfigurationService.class);

   private final ConfigurationUpdateManager cfgUpdateMangaer;
   private TaskHandle cfgPollerHandle;

   public ConfigurationService() {
      cfgUpdateMangaer = new ConfigurationUpdateManagerImpl();
   }

   @Override
   public ResolutionAction resolve(ServiceManager serviceManager) {
      if (serviceManager.listRegisteredServicesFor(Tasker.class).isEmpty()) {
         return ResolutionAction.DEFER;
      }

      return ResolutionAction.INIT;
   }

   @Override
   public Object instance() {
      return cfgUpdateMangaer;
   }

   @Override
   public String name() {
      return CFG_SERVICE_NAME;
   }

   @Override
   public boolean provides(Class serviceInterface) {
      return serviceInterface.isAssignableFrom(cfgUpdateMangaer.getClass());
   }

   @Override
   public void init(ServiceContext sc) throws InitializationException {
      Tasker tasker;

      try {
         tasker = new ServiceHandler(sc.manager()).firstAvailable(Tasker.class);
      } catch(ServiceUnavailableException sue) {
         throw new InitializationException(sue);
      }

      TimeValue pollerTime = DEFAULT_POLL_INTERVAL;

      if (sc.parameters().containsKey(CFG_POLLER_PROPERTY_KEY)) {
         final String configuredPollTime = sc.parameters().get(CFG_POLLER_PROPERTY_KEY);

         try {
            pollerTime = new TimeValue(Long.parseLong(configuredPollTime), TimeUnit.MILLISECONDS);
         } catch (NumberFormatException nfe) {
            LOG.error("Value: " + configuredPollTime + " is not a valid number. The configuration poller accepts time periods in ms.", nfe);
         }
      }

      LOG.info("Nuke configuration poller starting.");

      cfgPollerHandle = tasker.task(new ConfigurationUpdateRunnable(cfgUpdateMangaer), pollerTime);
   }

   @Override
   public void destroy() {
      LOG.info("Nuke configuration poller stopping.");

      cfgPollerHandle.cancellationRemote().cancel();
   }
}
