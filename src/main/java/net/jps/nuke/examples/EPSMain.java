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
 * Atom EPS example
 * 
 * @author zinic
 */
public class EPSMain {

   public static void main(String[] args) {
      final Nuke nukeKernel = new NukeKernel();

      final Task task = nukeKernel.follow(new EventGenerator(true), new TimeValue(200, TimeUnit.MILLISECONDS));
      final Relay relay = new Relay();
      task.addListener(relay);

      /**
       * How EPS works
       *
       * AtomEvenHandlers process only events. This simplifies the interface.
       * 
       * AtomEventHandlers elect into events received by EPS by 'selecting'
       * them. Selection is accomplished with a Selector object that contains
       * the selection logic. The relay will then relay atom events to the
       * AtomEventHandler based on the selection result.
       * 
       * The following enlistment will only select events that come from a feed
       * that has a category element with a term equal to 'test'
       * 
       * When a feed is selected, its entries are processed in order. Each entry
       * must be selected.
       * 
       * The following enlistment will only select entries that have a category
       * element specified with a term equal to 'test'
       */
      relay.enlistHandler(new AtomEventHandler() {
         @Override
         public Entry entry(Entry entry) throws EventProcessingException {
            System.out.println("Entry: " + entry.id().value());

            return entry;
         }
      }, new CategorySelector(new String[]{"test"}, new String[]{"test"}));

      nukeKernel.start();
   }
}
