package net.jps.nuke.examples;

import java.util.concurrent.TimeUnit;
import net.jps.nuke.Nuke;
import net.jps.nuke.NukeKernel;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.examples.source.EventGenerator;
import net.jps.nuke.listener.eps.handler.EventProcessingException;
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
      final Relay relay = new Relay();
      
      relay.enlistHandler(new AtomEventHandler() {
         @Override
         public void entry(Entry entry) throws EventProcessingException {
            System.out.println("Entry: " + entry.id().value());
         }
      }, new CategorySelector(new String[]{"test"}, new String[]{"test"}));


      final Nuke nukeKernel = new NukeKernel();

      final Task task1 = nukeKernel.follow(new EventGenerator("Task 1", true), new TimeValue(500, TimeUnit.MILLISECONDS));
      task1.addListener(relay);
      
      final Task task2 = nukeKernel.follow(new EventGenerator("Task 2", true), new TimeValue(1, TimeUnit.SECONDS));
      task2.addListener(relay);
      
      final Task task3 = nukeKernel.follow(new EventGenerator("Task 3", true), new TimeValue(2, TimeUnit.SECONDS));
      task3.addListener(relay);

      nukeKernel.start();
   }
}
