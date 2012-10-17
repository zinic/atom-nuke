package org.atomnuke.examples;

import java.io.File;
import java.util.concurrent.TimeUnit;
import org.atomnuke.Nuke;
import org.atomnuke.NukeKernel;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.examples.handler.FeedFileWriterHandler;
import org.atomnuke.examples.source.EventGenerator;
import org.atomnuke.listener.eps.EventletRelay;
import org.atomnuke.listener.eps.eventlet.AtomEventletException;
import org.atomnuke.listener.eps.eventlet.AtomEventletPartial;
import org.atomnuke.listener.eps.selectors.CategorySelector;
import org.atomnuke.task.AtomTask;
import org.atomnuke.util.TimeValue;

/**
 *
 * How EPS works
 *
 * AtomEvenHandlers process only events. This simplifies the interface.
 *
 * AtomEventHandlers elect into events received by EPS by 'selecting' them.
 * Selection is accomplished with a Selector object that contains the selection
 * logic. The relay will then relay atom events to the AtomEventHandler based on
 * the selection result.
 *
 * When a feed is selected, its entries are processed in order. Each entry must
 * be selected.
 *
 * @author zinic
 */
public class EPSMain {

   public static void main(String[] args) throws Exception {
      // First relay for selecting feeds that have the category 'test' and only
      // the entries inside that feed that also have the category 'test'
      final EventletRelay relay1 = new EventletRelay();

      // Event eventlet partial makes delegate creation more simple
      relay1.enlistHandler(new AtomEventletPartial() {

         @Override
         public void entry(Entry entry) throws AtomEventletException {
            System.out.println("Relay 1 - Entry: " + entry.id().toString());
         }
      }, new CategorySelector(new String[]{"test"}, new String[]{"test"}));


      // Second relay for selecting feeds that have the category 'test' and only
      // the entries inside that feed that have the category 'other-cat'
      final EventletRelay relay2 = new EventletRelay();

      // Creating your own handler allows you to implement the init and destroy
      // methods however you like
      relay2.enlistHandler(new FeedFileWriterHandler(new File("/tmp/test.feed")), new CategorySelector(new String[]{"test"}, new String[]{"other-cat"}));


      // Set up Nuke
      final Nuke nukeKernel = new NukeKernel();

      final AtomTask task1 = nukeKernel.follow(new EventGenerator("Task 1", true), new TimeValue(500, TimeUnit.MILLISECONDS));
      task1.addListener(relay1);

      final AtomTask task2 = nukeKernel.follow(new EventGenerator("Task 2", true), new TimeValue(1, TimeUnit.SECONDS));
      task2.addListener(relay1);
      task2.addListener(relay2);

      final AtomTask task3 = nukeKernel.follow(new EventGenerator("Task 3", true), new TimeValue(2, TimeUnit.SECONDS));
      task3.addListener(relay1);

      nukeKernel.start();

      Thread.sleep(10000);

      nukeKernel.destroy();
   }
}
