package org.atomnuke.container.service.config;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.service.ResolutionAction;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.context.ServiceContext;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.manager.service.TaskingModule;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;
import org.atomnuke.util.config.update.ConfigurationUpdateManagerImpl;
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

   public static final String CFG_POLLER_PROPERTY_KEY = "org.atomnuke.container.service.config.ConfigurationService.poll_interval_ms";
   public static final String CFG_SERVICE_NAME = "org.atomnuke.container.service.config.ConfigurationService";

   private static final TimeValue DEFAULT_POLL_INTERVAL = new TimeValue(15, TimeUnit.SECONDS);
   private static final Logger LOG = LoggerFactory.getLogger(ConfigurationService.class);

   private ConfigurationUpdateManager cfgUpdateMangaer;
   private TaskHandle cfgPollerHandle;

   @Override
   public ResolutionAction resolve(ServiceManager serviceManager) {
      final boolean hasReclamationHandler = serviceManager.serviceRegistered(ReclamationHandler.class);
      final boolean hasTaskingModule = serviceManager.serviceRegistered(TaskingModule.class);

      return hasTaskingModule && hasReclamationHandler ? ResolutionAction.INIT : ResolutionAction.DEFER;
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

   private static TimeValue pollerTime(Map<String, String> parameters) {
      TimeValue pollerTime = DEFAULT_POLL_INTERVAL;

      if (parameters.containsKey(CFG_POLLER_PROPERTY_KEY)) {
         final String configuredPollTime = parameters.get(CFG_POLLER_PROPERTY_KEY);

         try {
            pollerTime = new TimeValue(Long.parseLong(configuredPollTime), TimeUnit.MILLISECONDS);
         } catch (NumberFormatException nfe) {
            LOG.error("Value: " + configuredPollTime + " is not a valid number. The configuration poller accepts time periods in ms.", nfe);
         }
      }

      return pollerTime;
   }

   @Override
   public void init(ServiceContext sc) throws InitializationException {
      LOG.info("Nuke configuration poller starting.");

      final TimeValue pollerTime = pollerTime(sc.parameters());

      try {
         final ReclamationHandler reclamationHandler = ServiceHandler.instance().firstAvailable(sc.manager(), ReclamationHandler.class);
         final TaskingModule taskingModule = ServiceHandler.instance().firstAvailable(sc.manager(), TaskingModule.class);

         cfgUpdateMangaer = new ConfigurationUpdateManagerImpl(reclamationHandler);
         cfgPollerHandle = taskingModule.tasker().task(new ConfigurationUpdateRunnable(cfgUpdateMangaer), pollerTime);
      } catch (ServiceUnavailableException sue) {
         throw new InitializationException(sue);
      }
   }

   @Override
   public void destroy() {
      LOG.info("Nuke configuration poller stopping.");

      cfgPollerHandle.cancellationRemote().cancel();
   }
}
