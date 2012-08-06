package org.atomnuke.cli.command.server.builder;

import java.util.HashMap;
import java.util.Map;
import org.atomnuke.Nuke;
import org.atomnuke.NukeKernel;
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
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class ServerBuilder {

   private final Map<String, org.atomnuke.listener.eps.Relay> builtRelays;
   private final Map<String, AtomEventlet> builtEventlets;
   private final Map<String, AtomListener> builtListeners;
   private final Map<String, Task> registeredSources;
   private final ConfigurationHandler cfgHandler;
   private final Nuke kernelBeingBuilt;

   public ServerBuilder(ConfigurationHandler cfgHandler) {
      this.cfgHandler = cfgHandler;

      kernelBeingBuilt = new NukeKernel();
      builtEventlets = new HashMap<String, AtomEventlet>();
      builtListeners = new HashMap<String, AtomListener>();
      builtRelays = new HashMap<String, org.atomnuke.listener.eps.Relay>();
      registeredSources = new HashMap<String, Task>();
   }

   public Nuke build() throws ConfigurationException {
      constructSources();
      constructRelays();
      constructListeners();
      constructEventlets();

      processBindings();

      return kernelBeingBuilt;
   }

   public Object construct(LanguageType langType, String href) throws ConfigurationException {
      switch (langType) {
         case JAVA:
            try {
               final Class sourceClass = Class.forName(href);

               return sourceClass.newInstance();
            } catch (Exception ex) {
               throw new ConfigurationException(ex.getMessage(), ex);
            }

         case JAVASCRIPT:
         case PYTHON:
         default:
            throw new ConfigurationException("Unsupported scriptlet lang: " + langType);
      }
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

   public void constructSources() throws ConfigurationException {
      for (Source source : cfgHandler.getSources()) {
         final Task newTask = kernelBeingBuilt.follow((AtomSource) construct(source.getType(), source.getHref()), TimeValue.fromPollingInterval(source.getPollingInterval()));

         registeredSources.put(source.getId(), newTask);
      }
   }

   public void constructRelays() throws ConfigurationException {
      for (Relay relay : cfgHandler.getRelays()) {
         builtRelays.put(relay.getId(), new org.atomnuke.listener.eps.Relay());
      }
   }

   public void constructListeners() throws ConfigurationException {
      for (Sink sink : cfgHandler.getSinks()) {
         builtListeners.put(sink.getId(), (AtomListener) construct(sink.getType(), sink.getHref()));
      }
   }

   public void constructEventlets() throws ConfigurationException {
      for (Eventlet eventlet : cfgHandler.getEventlets()) {
         builtEventlets.put(eventlet.getId(), (AtomEventlet) construct(eventlet.getType(), eventlet.getHref()));
      }
   }
}
