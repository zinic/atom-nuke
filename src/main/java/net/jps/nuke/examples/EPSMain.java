package net.jps.nuke.examples;

import java.util.concurrent.TimeUnit;
import net.jps.nuke.Nuke;
import net.jps.nuke.NukeKernel;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.examples.source.EventGenerator;
import net.jps.nuke.listener.eps.EventProcessingException;
import net.jps.nuke.listener.eps.Relay;
import net.jps.nuke.listener.eps.handler.AtomEventHandler;
import net.jps.nuke.listener.eps.selectors.CategorySelector;
import net.jps.nuke.task.Task;
import net.jps.nuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class EPSMain {

   public static void main(String[] args) {
      final Nuke nukeKernel = new NukeKernel();

      final Task task = nukeKernel.follow(new EventGenerator(true), new TimeValue(200, TimeUnit.MILLISECONDS));
      final Relay relay = new Relay();
      
      relay.enlistHandler(new AtomEventHandler() {
         @Override
         public Entry entry(Entry entry) throws EventProcessingException {
            System.out.println("Entry: " + entry.id().value());
            
            return entry;
         }
      }, new CategorySelector(new String[]{"test"}));
      
      task.addListener(relay);
      
      nukeKernel.start();
   }
}
