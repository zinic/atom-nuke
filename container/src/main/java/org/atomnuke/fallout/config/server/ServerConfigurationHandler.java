package org.atomnuke.fallout.config.server;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.atomnuke.atombus.config.model.Binding;
import org.atomnuke.atombus.config.model.Bindings;
import org.atomnuke.atombus.config.model.MessageActor;
import org.atomnuke.atombus.config.model.MessageActors;
import org.atomnuke.atombus.config.model.MessageSource;
import org.atomnuke.atombus.config.model.MessageSources;
import org.atomnuke.atombus.config.model.ServerConfiguration;

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

   public List<MessageActor> getMessageActors() {
      if (configurationCopy.getActors() == null) {
         final MessageActors actors = new MessageActors();
         configurationCopy.setActors(actors);
      }

      return configurationCopy.getActors().getActor();
   }

   public List<MessageSource> getMessageSources() {
      if (configurationCopy.getMessageSources() == null) {
         final MessageSources sources = new MessageSources();
         configurationCopy.setMessageSources(sources);
      }

      return configurationCopy.getMessageSources().getSource();
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
         newBinding.setSourceActor(source);
         newBinding.setSinkActor(sink);

         getBindings().add(newBinding);
      }
   }

   public Binding findBinding(String source, String sink) {
      for (Binding binding : getBindings()) {
         if (binding.getSourceActor().equals(source) && binding.getSinkActor().equals(sink)) {
            return binding;
         }
      }

      return null;
   }

   public List<Binding> findBindingsForSource(String source) {
      final List<Binding> bindingsFound = new LinkedList<Binding>();

      for (Binding binding : getBindings()) {
         if (binding.getSourceActor().equals(source)) {
            bindingsFound.add(binding);
         }
      }

      return bindingsFound;
   }

   public MessageActor findMessageActor(String id) {
      for (MessageActor actor : getMessageActors()) {
         if (StringUtils.isNotEmpty(actor.getId()) && actor.getId().equals(id)) {
            return actor;
         }
      }

      return null;
   }

   public MessageSource findMessageSource(String id) {
      for (MessageSource source : getMessageSources()) {
         if (StringUtils.isNotEmpty(source.getActorRef()) && source.getActorRef().equals(id)) {
            return source;
         }
      }

      return null;
   }
}
