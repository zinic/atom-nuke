package org.atomnuke.fallout.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.config.model.MessageActor;
import org.atomnuke.util.LanguageTypeUtil;
import org.atomnuke.fallout.config.server.ServerConfigurationHandler;
import org.atomnuke.fallout.context.ContainerContext;
import org.atomnuke.container.packaging.PackageContext;
import org.atomnuke.container.packaging.bindings.lang.BindingLanguage;
import org.atomnuke.plugin.ReferenceInstantiationException;
import org.atomnuke.source.AtomSource;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.lifecycle.Reclaimable;
import org.atomnuke.util.lifecycle.ResourceLifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ConfigurationProcessor {

   private static final Logger LOG = LoggerFactory.getLogger(ConfigurationProcessor.class);

   private final Map<String, InstanceContext> builtInstances;
   private final Collection<PackageContext> loadedPackages;
   private final ServerConfigurationHandler cfgHandler;
   private final ContainerContext containerContext;

   public ConfigurationProcessor(ContainerContext containerContext, ServerConfigurationHandler cfgHandler, Collection<PackageContext> loadedPackages) {
      this.containerContext = containerContext;
      this.cfgHandler = cfgHandler;
      this.loadedPackages = loadedPackages;

      builtInstances = new HashMap<String, InstanceContext>();
   }

   public void merge() throws ConfigurationException {
      for (MessageActor actor : cfgHandler.getMessageActors()) {
         try {
            final InstanceContext<? extends Reclaimable> actorContext = constructActor(actor.getType(), actor.getHref());
            containerContext.enlistActor(actor.getId(), actorContext);
         } catch (ReferenceInstantiationException bie) {
            LOG.error("Could not look up actor instance, \"" + actor.getId() + "\" - Reason: " + bie.getMessage(), bie);
         }
      }

      containerContext.process(cfgHandler);
      builtInstances.clear();
   }

   private InstanceContext<? extends Reclaimable> constructActor(LanguageType langType, String ref) throws ReferenceInstantiationException {
      if (builtInstances.containsKey(ref)) {
         return builtInstances.get(ref);
      }

      final BindingLanguage bindingLanguage = LanguageTypeUtil.asBindingLanguage(langType);

      for (PackageContext packageContext : loadedPackages) {
         final InstanceContext<ResourceLifeCycle> source = packageContext.packageBindings().resolveReference(ResourceLifeCycle.class, bindingLanguage, ref);

         if (source != null) {
            builtInstances.put(ref, source);
            return source;
         }
      }

      throw new ReferenceInstantiationException("Unable to locate actor reference: " + ref);
   }
}
