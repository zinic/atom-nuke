package org.atomnuke.container.service.config;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.plugin.LocalInstanceContext;
import org.atomnuke.lifecycle.resolution.ResolutionActionType;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.ServiceContext;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.lifecycle.resolution.ResolutionAction;
import org.atomnuke.lifecycle.resolution.ResolutionActionImpl;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.manager.service.TaskingService;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;
import org.atomnuke.util.config.update.ConfigurationUpdateManagerImpl;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.service.runtime.AbstractRuntimeService;
import org.atomnuke.util.service.ServiceHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
@NukeService
public class ConfigurationService extends AbstractRuntimeService {

   public static final String CFG_POLLER_PROPERTY_KEY = "org.atomnuke.container.service.config.ConfigurationService.poll_interval_ms";
   public static final String CFG_SERVICE_NAME = "org.atomnuke.container.service.config.ConfigurationService";

   private static final TimeValue DEFAULT_POLL_INTERVAL = new TimeValue(15, TimeUnit.SECONDS);
   private static final Logger LOG = LoggerFactory.getLogger(ConfigurationService.class);

   private ConfigurationUpdateManager cfgUpdateMangaer;
   private TaskHandle cfgPollerHandle;

   public ConfigurationService() {
      super(ConfigurationUpdateManager.class);
   }

   @Override
   public ResolutionAction resolve(ServiceManager serviceManager) {
      final boolean hasReclamationHandler = serviceManager.serviceRegistered(ReclamationHandler.class);
      final boolean hasTaskingModule = serviceManager.serviceRegistered(TaskingService.class);

      return hasTaskingModule && hasReclamationHandler
              ? new ResolutionActionImpl(ResolutionActionType.INIT) : new ResolutionActionImpl(ResolutionActionType.DEFER);
   }

   @Override
   public Object instance() {
      return cfgUpdateMangaer;
   }

   @Override
   public String name() {
      return CFG_SERVICE_NAME;
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
         final ReclamationHandler reclamationHandler = ServiceHandler.instance().firstAvailable(sc.services(), ReclamationHandler.class);
         final TaskingService taskingModule = ServiceHandler.instance().firstAvailable(sc.services(), TaskingService.class);

         cfgUpdateMangaer = new ConfigurationUpdateManagerImpl(reclamationHandler);
         cfgPollerHandle = taskingModule.tasker().pollTask(new LocalInstanceContext(new ConfigurationUpdateRunnable(cfgUpdateMangaer)), pollerTime);
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
