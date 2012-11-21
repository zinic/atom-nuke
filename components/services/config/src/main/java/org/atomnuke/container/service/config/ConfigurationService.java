package org.atomnuke.container.service.config;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.container.service.annotation.Requires;
import org.atomnuke.service.ServiceUnavailableException;
import org.atomnuke.service.ServiceContext;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.manager.service.TaskingService;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;
import org.atomnuke.util.config.update.ConfigurationUpdateManagerImpl;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.plugin.context.LocalInstanceContext;
import org.atomnuke.service.runtime.AbstractRuntimeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
@NukeService
@Requires({ReclamationHandler.class, TaskingService.class})
public class ConfigurationService extends AbstractRuntimeService {

   public static final String CFG_POLLER_PROPERTY_KEY = "org.atomnuke.container.service.config.ConfigurationService.poll_interval_ms";

   private static final TimeValue DEFAULT_POLL_INTERVAL = new TimeValue(15, TimeUnit.SECONDS);
   private static final Logger LOG = LoggerFactory.getLogger(ConfigurationService.class);

   private ConfigurationUpdateManager cfgUpdateMangaer;
   private TaskHandle cfgPollerHandle;

   public ConfigurationService() {
      super(ConfigurationUpdateManager.class);
   }

   @Override
   public Object instance() {
      return cfgUpdateMangaer;
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
         final ReclamationHandler reclamationHandler = sc.services().firstAvailable(ReclamationHandler.class);
         final TaskingService taskingModule = sc.services().firstAvailable(TaskingService.class);

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
