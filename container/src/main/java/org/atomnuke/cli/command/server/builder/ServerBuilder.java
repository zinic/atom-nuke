package org.atomnuke.cli.command.server.builder;

import java.util.HashMap;
import java.util.Map;
import org.atomnuke.Nuke;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.bindings.resolver.BindingResolver;
import org.atomnuke.context.InstanceContext;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.config.model.Binding;
import org.atomnuke.config.model.Eventlet;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.config.model.Relay;
import org.atomnuke.config.model.Sink;
import org.atomnuke.config.model.Source;
import org.atomnuke.config.server.ServerConfigurationHandler;
import org.atomnuke.context.SimpleInstanceContext;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.eps.EventletRelay;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.Task;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.util.TimeValueUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ServerBuilder {

   private static final Logger LOG = LoggerFactory.getLogger(ServerBuilder.class);
   private final Map<String, InstanceContext<EventletRelay>> builtRelays;
   private final Map<String, InstanceContext<AtomEventlet>> builtEventlets;
   private final Map<String, InstanceContext<AtomListener>> builtListeners;
   private final Map<String, Task> registeredSources;
   private final BindingResolver bindingsResolver;
   private final ServerConfigurationHandler cfgHandler;

   public ServerBuilder(ServerConfigurationHandler cfgHandler, BindingResolver bindingsResolver) {
      this.cfgHandler = cfgHandler;
      this.bindingsResolver = bindingsResolver;

      builtEventlets = new HashMap<String, InstanceContext<AtomEventlet>>();
      builtListeners = new HashMap<String, InstanceContext<AtomListener>>();
      builtRelays = new HashMap<String, InstanceContext<EventletRelay>>();

      registeredSources = new HashMap<String, Task>();
   }

   public void build(Nuke kernelBeingBuilt) throws BindingInstantiationException, ConfigurationException {
      constructSources(kernelBeingBuilt);
      constructRelays();
      constructListeners();
      constructEventlets();

      processBindings();
   }

   public InstanceContext<AtomEventlet> constructEventlet(LanguageType langType, String ref) throws BindingInstantiationException {
      return bindingsResolver.resolveEventlet(langType, ref);
   }

   public InstanceContext<AtomSource> constructSource(LanguageType langType, String ref) throws BindingInstantiationException {
      return bindingsResolver.resolveSource(langType, ref);
   }

   public InstanceContext<AtomListener> constructListener(LanguageType langType, String ref) throws BindingInstantiationException {
      return bindingsResolver.resolveListener(langType, ref);
   }

   public void processBindings() throws ConfigurationException {
      for (Binding binding : cfgHandler.getBindings()) {
         final Task source = registeredSources.get(binding.getTarget());

         if (source != null) {
            final InstanceContext<? extends AtomListener> atomListenerContext = builtListeners.containsKey(binding.getReceiver()) ? builtListeners.get(binding.getReceiver()) : builtRelays.get(binding.getReceiver());

            if (atomListenerContext == null) {
               throw new ConfigurationException("Unable to locate listener or realy, " + binding.getReceiver());
            }

            atomListenerContext.stepInto();

            try {
               source.addListener(atomListenerContext);
            } catch (InitializationException ie) {
               throw new ConfigurationException("Atom listener initialization error: " + ie.getMessage(), ie);
            } finally {
               atomListenerContext.stepOut();
            }
         } else {
            final InstanceContext<EventletRelay> relayContext = builtRelays.get(binding.getTarget());

            if (relayContext == null) {
               throw new ConfigurationException("Unable to locate source or relay, " + binding.getTarget());
            }

            final InstanceContext<AtomEventlet> eventlet = builtEventlets.get(binding.getReceiver());

            if (eventlet == null) {
               throw new ConfigurationException("Unable to locate eventlet, " + binding.getReceiver());
            }

            relayContext.stepInto();

            try {
               relayContext.getInstance().enlistHandler(eventlet.getInstance());
            } catch (InitializationException ie) {
               throw new ConfigurationException("Eventlet initialization error: " + ie.getMessage(), ie);
            } finally {
               relayContext.stepOut();
            }
         }
      }
   }

   public void constructSources(Nuke kernelBeingBuilt) throws ConfigurationException {
      for (Source source : cfgHandler.getSources()) {
         try {
            final InstanceContext<AtomSource> sourceContext = constructSource(source.getType(), source.getHref());
            final Task newTask = kernelBeingBuilt.follow(sourceContext, TimeValueUtil.fromPollingInterval(source.getPollingInterval()));

            registeredSources.put(source.getId(), newTask);
         } catch (BindingInstantiationException bie) {
            LOG.error("Could not create source instance " + source.getId() + ". Reason: " + bie.getMessage(), bie);
         } catch (InitializationException ie) {
            LOG.error("Could not initialize source instance " + source.getId() + ". Reason: " + ie.getMessage(), ie);
         }
      }
   }

   public void constructRelays() throws ConfigurationException {
      for (Relay relay : cfgHandler.getRelays()) {
         builtRelays.put(relay.getId(), new SimpleInstanceContext<EventletRelay>(new EventletRelay()));
      }
   }

   public void constructListeners() throws ConfigurationException {
      for (Sink sink : cfgHandler.getSinks()) {
         try {
            builtListeners.put(sink.getId(), constructListener(sink.getType(), sink.getHref()));
         } catch (BindingInstantiationException bie) {
            LOG.error("Could not create sink instance " + sink.getId() + ". Reason: " + bie.getMessage(), bie);
         }
      }
   }

   public void constructEventlets() throws BindingInstantiationException, ConfigurationException {
      for (Eventlet eventlet : cfgHandler.getEventlets()) {
         try {
            builtEventlets.put(eventlet.getId(), constructEventlet(eventlet.getType(), eventlet.getHref()));
         } catch (BindingInstantiationException bie) {
            LOG.error("Could not create eventlet instance " + eventlet.getId() + ". Reason: " + bie.getMessage(), bie);
         }
      }
   }
}
