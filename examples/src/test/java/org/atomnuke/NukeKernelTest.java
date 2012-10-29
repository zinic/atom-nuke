package org.atomnuke;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.examples.listener.eventlet.CounterEventlet;
import org.atomnuke.examples.source.EventGenerator;
import org.atomnuke.fallout.service.gc.FalloutReclamationService;
import org.atomnuke.listener.eps.EventletRelay;
import org.atomnuke.plugin.InstanceContextImpl;
import org.atomnuke.plugin.env.NopInstanceEnvironment;
import org.atomnuke.plugin.proxy.japi.JapiProxyFactory;
import org.atomnuke.service.RuntimeServiceManager;
import org.atomnuke.service.Service;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.task.AtomTask;
import org.atomnuke.task.context.TaskContextImpl;
import org.atomnuke.util.TimeValue;
import org.junit.Test;

/**
 *
 * @author zinic
 */
public class NukeKernelTest {

   @Test
   public void nukeShakedownTest() throws Exception {
      final NukeKernel nukeKernel = new NukeKernel();
      final AtomicLong eventsProcessed = new AtomicLong(0);

      for (int taskId = 1; taskId <= 30; taskId++) {
         final AtomTask task = nukeKernel.follow(new EventGenerator("Task " + taskId, true), new TimeValue(1000 * taskId, TimeUnit.NANOSECONDS));

         final EventletRelay relay = new EventletRelay();

         final ServiceManager svcManager = new RuntimeServiceManager(new JapiProxyFactory());

         svcManager.submit(new InstanceContextImpl<Service>(NopInstanceEnvironment.getInstance(), new FalloutReclamationService()));
         svcManager.resolve();

         relay.init(new TaskContextImpl(null, null, svcManager, null));

         relay.enlistHandler(new CounterEventlet(eventsProcessed, false));
         relay.enlistHandler(new CounterEventlet(eventsProcessed, false));
         relay.enlistHandler(new CounterEventlet(eventsProcessed, false));
         relay.enlistHandler(new CounterEventlet(eventsProcessed, false));
         relay.enlistHandler(new CounterEventlet(eventsProcessed, false));

         task.addListener(relay);
      }

      nukeKernel.start();

      Thread.sleep(1000);

      nukeKernel.destroy();

      System.out.println("Processed " + eventsProcessed.get() + " entry events in one second.");
   }
}
