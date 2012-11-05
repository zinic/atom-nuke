package org.atomnuke.examples;

import java.io.File;
import java.util.concurrent.TimeUnit;
import org.atomnuke.NukeKernel;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.builder.CategoryBuilder;
import org.atomnuke.examples.eventlets.FeedFileWriterHandler;
import org.atomnuke.examples.source.EventGenerator;
import org.atomnuke.sink.eps.EventletChainSink;
import org.atomnuke.sink.eps.eventlet.AtomEventletException;
import org.atomnuke.sink.eps.eventlet.AtomEventletPartial;
import org.atomnuke.sink.selectors.CategorySelector;
import org.atomnuke.sink.selectors.CategorySelectorImpl;
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
      final EventletChainSink relay1 = new EventletChainSink();

      final CategorySelector selector1 = new CategorySelectorImpl();
      selector1.addCategory(new CategoryBuilder().setTerm("test").build());

      // Event eventlet partial makes delegate creation more simple
      relay1.enlistHandler(new AtomEventletPartial() {

         @Override
         public void entry(Entry entry) throws AtomEventletException {
            System.out.println("Relay 1 - Entry: " + entry.id().toString());
         }
      }, selector1);


      // Second relay for selecting feeds that have the category 'test' and only
      // the entries inside that feed that have the category 'other-cat'
      final EventletChainSink relay2 = new EventletChainSink();

      // Creating your own handler allows you to implement the init and destroy
      // methods however you like
      final CategorySelector selector2 = new CategorySelectorImpl();
      selector1.addCategory(new CategoryBuilder().setTerm("test").build());
      selector1.addCategory(new CategoryBuilder().setTerm("other-cat").build());

      relay2.enlistHandler(new FeedFileWriterHandler(new File("/tmp/test.feed")), selector2);


      // Set up Nuke
      final NukeKernel nukeKernel = new NukeKernel();

      final AtomTask task1 = nukeKernel.follow(new EventGenerator("Task 1", true), new TimeValue(500, TimeUnit.MILLISECONDS));
      task1.addSink(relay1);

      final AtomTask task2 = nukeKernel.follow(new EventGenerator("Task 2", true), new TimeValue(1, TimeUnit.SECONDS));
      task2.addSink(relay1);
      task2.addSink(relay2);

      final AtomTask task3 = nukeKernel.follow(new EventGenerator("Task 3", true), new TimeValue(2, TimeUnit.SECONDS));
      task3.addSink(relay1);

      nukeKernel.start();

      Thread.sleep(10000);

      nukeKernel.destroy();
   }
}
