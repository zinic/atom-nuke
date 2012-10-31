package org.atomnuke.fallout.config.server;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.atomnuke.config.model.Binding;
import org.atomnuke.config.model.Bindings;
import org.atomnuke.config.model.EventProcessingSystem;
import org.atomnuke.config.model.Eventlet;
import org.atomnuke.config.model.Eventlets;
import org.atomnuke.config.model.Parameter;
import org.atomnuke.config.model.Relay;
import org.atomnuke.config.model.Relays;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.config.model.Sink;
import org.atomnuke.config.model.Sinks;
import org.atomnuke.config.model.Source;
import org.atomnuke.config.model.Sources;

/**
 *
 * @author zinic
 */
public class ServerConfigurationHandler {

   private final ServerConfiguration configurationCopy;

   public ServerConfigurationHandler(ServerConfiguration configurationCopy) {
      this.configurationCopy = configurationCopy != null ? configurationCopy : new ServerConfiguration();
   }

   public ServerConfiguration getConfiguration() {
      return configurationCopy;
   }

   public void addSource(Source source) {
      getSources().add(source);
   }

   public void addSink(Sink sink) {
      getSinks().add(sink);
   }

   public void addRelay(String id) {
      final Relay newRelay = new Relay();
      newRelay.setId(id);

      getRelays().add(newRelay);
   }

   public void addEventlet(Eventlet eventlet) {
      getEventlets().add(eventlet);
   }

   public List<Source> getSources() {
      if (configurationCopy.getSources() == null) {
         configurationCopy.setSources(new Sources());
      }

      return configurationCopy.getSources().getSource();
   }

   public List<Sink> getSinks() {
      if (configurationCopy.getSinks() == null) {
         configurationCopy.setSinks(new Sinks());
      }

      return configurationCopy.getSinks().getSink();
   }

   public List<Relay> getRelays() {
      if (configurationCopy.getEps() == null) {
         final EventProcessingSystem eps = new EventProcessingSystem();
         eps.setEventlets(new Eventlets());
         eps.setRelays(new Relays());

         configurationCopy.setEps(eps);
      }

      return configurationCopy.getEps().getRelays().getRelay();
   }

   public List<Eventlet> getEventlets() {
      if (configurationCopy.getEps() == null) {
         final EventProcessingSystem eps = new EventProcessingSystem();
         eps.setEventlets(new Eventlets());
         eps.setRelays(new Relays());

         configurationCopy.setEps(eps);
      }

      return configurationCopy.getEps().getEventlets().getEventlet();
   }

   public List<Binding> getBindings() {
      if (configurationCopy.getBindings() == null) {
         final Bindings bindings = new Bindings();
         configurationCopy.setBindings(bindings);
      }

      return configurationCopy.getBindings().getBinding();
   }

   public void bind(String source, String sink) {
      // Bind only if we don't already have a binding
      if (findBinding(source, sink) == null) {
         final Binding newBinding = new Binding();

         newBinding.setId(UUID.randomUUID().toString());
         newBinding.setSource(source);
         newBinding.setSink(sink);

         getBindings().add(newBinding);
      }
   }

   public Binding findBinding(String source, String sink) {
      for (Binding binding : getBindings()) {
         if (binding.getSource().equals(source) && binding.getSink().equals(sink)) {
            return binding;
         }
      }

      return null;
   }

   public List<Binding> findBindingsForSource(String source) {
      final List<Binding> bindingsFound = new LinkedList<Binding>();

      for (Binding binding : getBindings()) {
         if (binding.getSource().equals(source)) {
            bindingsFound.add(binding);
         }
      }

      return bindingsFound;
   }

   public Relay findRelay(String id) {
      for (Relay relay : getRelays()) {
         if (relay.getId().equals(id)) {
            return relay;
         }
      }

      return null;
   }

   public Eventlet findEventlet(String eventletId) {
      for (Eventlet eventlet : getEventlets()) {
         if (eventlet.getId().equals(eventletId)) {
            return eventlet;
         }
      }

      return null;
   }

   public Eventlet firstEventletByParameter(Parameter parameter) {
      for (Eventlet eventlet : getEventlets()) {
         if (eventlet.getParameters() != null) {
            for (Parameter eventletParameter : eventlet.getParameters().getParam()) {
               if (eventletParameter.getName().equals(parameter.getName()) && eventletParameter.getValue().equals(parameter.getValue())) {
                  return eventlet;
               }
            }
         }
      }

      return null;
   }

   public Source findSource(String id) {
      for (Source source : getSources()) {
         if (source.getId().equals(id)) {
            return source;
         }
      }

      return null;
   }

   public Sink findSink(String id) {
      for (Sink sink : getSinks()) {
         if (sink.getId().equals(id)) {
            return sink;
         }
      }

      return null;
   }
}
