package org.atomnuke.cli.command.server.builder;

import java.util.HashMap;
import java.util.Map;
import org.atomnuke.Nuke;
import org.atomnuke.NukeKernel;
import org.atomnuke.config.ConfigurationException;
import org.atomnuke.config.ConfigurationHandler;
import org.atomnuke.config.model.LanguageType;
import org.atomnuke.config.model.Relay;
import org.atomnuke.config.model.Source;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.Task;

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

   public void build() throws ConfigurationException {
      constructSources();
      constructRelays();
      constructListeners();
      constructEventlets();
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

   public void constructSources() throws ConfigurationException {
      for (Source source : cfgHandler.getSources()) {
         final Task newTask = kernelBeingBuilt.follow((AtomSource) construct(source.getType(), source.getHref()));
         registeredSources.put(source.getId(), newTask);
      }
   }

   public void constructRelays() throws ConfigurationException {
      for (Relay relay : cfgHandler.getRelays()) {
         builtRelays.put(relay.getId(), new org.atomnuke.listener.eps.Relay());
      }
   }

   public void constructListeners() throws ConfigurationException {
      for (Source source : cfgHandler.getSources()) {
         builtListeners.put(source.getId(), (AtomListener) construct(source.getType(), source.getHref()));
      }
   }

   public void constructEventlets() throws ConfigurationException {
      for (Source source : cfgHandler.getSources()) {
         builtEventlets.put(source.getId(), (AtomEventlet) construct(source.getType(), source.getHref()));
      }
   }
}
