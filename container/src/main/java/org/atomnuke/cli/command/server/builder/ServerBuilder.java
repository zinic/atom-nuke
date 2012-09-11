package org.atomnuke.cli.command.server.builder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.atomnuke.Nuke;
import org.atomnuke.NukeEnv;
import org.atomnuke.NukeKernel;
import org.atomnuke.bindings.BindingInstantiationException;
import org.atomnuke.bindings.ear.EarBindingContext;
import org.atomnuke.bindings.resolver.BindingResolver;
import org.atomnuke.config.ConfigurationException;
import org.atomnuke.config.ConfigurationHandler;
import org.atomnuke.config.model.Binding;
import org.atomnuke.config.model.Eventlet;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.config.model.Relay;
import org.atomnuke.config.model.Sink;
import org.atomnuke.config.model.Source;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.Task;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.util.TimeValueUtil;

/**
 *
 * @author zinic
 */
public class ServerBuilder {

   private final Map<String, org.atomnuke.listener.eps.Relay> builtRelays;
   private final Map<String, AtomEventlet> builtEventlets;
   private final Map<String, AtomListener> builtListeners;
   private final Map<String, Task> registeredSources;
   private final BindingResolver bindingsResolver;
   private final ConfigurationHandler cfgHandler;
   private final Nuke kernelBeingBuilt;

   public ServerBuilder(ConfigurationHandler cfgHandler, BindingResolver bindingsResolver) {
      this.cfgHandler = cfgHandler;
      this.bindingsResolver = bindingsResolver;

      kernelBeingBuilt = new NukeKernel();

      builtEventlets = new HashMap<String, AtomEventlet>();
      builtListeners = new HashMap<String, AtomListener>();
      builtRelays = new HashMap<String, org.atomnuke.listener.eps.Relay>();

      registeredSources = new HashMap<String, Task>();
   }

   public Nuke build() throws BindingInstantiationException, ConfigurationException {
      constructSources();
      constructRelays();
      constructListeners();
      constructEventlets();

      processBindings();

      return kernelBeingBuilt;
   }

   public AtomEventlet constructEventlet(LanguageType langType, String ref) throws BindingInstantiationException {
      return bindingsResolver.resolveEventlet(langType, ref);
   }

   public AtomSource constructSource(LanguageType langType, String ref) throws BindingInstantiationException {
      return bindingsResolver.resolveSource(langType, ref);
   }

   public AtomListener constructListener(LanguageType langType, String ref) throws BindingInstantiationException {
      return bindingsResolver.resolveListener(langType, ref);
   }

   public void processBindings() throws ConfigurationException {
      for (Binding binding : cfgHandler.getBindings()) {
         final Task source = registeredSources.get(binding.getTarget());

         if (source != null) {
            final AtomListener atomListener = builtListeners.containsKey(binding.getReceiver()) ? builtListeners.get(binding.getReceiver()) : builtRelays.get(binding.getReceiver());

            if (atomListener == null) {
               throw new ConfigurationException("Unable to locate listener or realy, " + binding.getReceiver());
            }

            try {
               source.addListener(atomListener);
            } catch (InitializationException ie) {
               throw new ConfigurationException("Atom listener initialization error: " + ie.getMessage(), ie);
            }
         } else {
            final org.atomnuke.listener.eps.Relay relay = builtRelays.get(binding.getTarget());

            if (relay == null) {
               throw new ConfigurationException("Unable to locate source or relay, " + binding.getTarget());
            }

            final AtomEventlet eventlet = builtEventlets.get(binding.getReceiver());

            if (eventlet == null) {
               throw new ConfigurationException("Unable to locate eventlet, " + binding.getReceiver());
            }

            try {
               relay.enlistHandler(eventlet);
            } catch (InitializationException ie) {
               throw new ConfigurationException("Eventlet initialization error: " + ie.getMessage(), ie);
            }
         }
      }
   }

   public void constructSources() throws BindingInstantiationException, ConfigurationException {
      for (Source source : cfgHandler.getSources()) {
         final Task newTask = kernelBeingBuilt.follow(constructSource(source.getType(), source.getHref()), TimeValueUtil.fromPollingInterval(source.getPollingInterval()));

         registeredSources.put(source.getId(), newTask);
      }
   }

   public void constructRelays() throws BindingInstantiationException, ConfigurationException{
      for (Relay relay : cfgHandler.getRelays()) {
         builtRelays.put(relay.getId(), new org.atomnuke.listener.eps.Relay());
      }
   }

   public void constructListeners() throws BindingInstantiationException, ConfigurationException {
      for (Sink sink : cfgHandler.getSinks()) {
         builtListeners.put(sink.getId(), constructListener(sink.getType(), sink.getHref()));
      }
   }

   public void constructEventlets() throws BindingInstantiationException, ConfigurationException {
      for (Eventlet eventlet : cfgHandler.getEventlets()) {
         builtEventlets.put(eventlet.getId(), constructEventlet(eventlet.getType(), eventlet.getHref()));
      }
   }
}
