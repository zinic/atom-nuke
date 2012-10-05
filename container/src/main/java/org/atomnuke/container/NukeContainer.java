package org.atomnuke.container;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import javax.xml.bind.JAXBException;
import org.atomnuke.NukeEnv;
import org.atomnuke.NukeKernel;
import org.atomnuke.bindings.BindingLoaderException;
import org.atomnuke.bindings.loader.DirectoryLoaderManager;
import org.atomnuke.bindings.resolver.BindingResolver;
import org.atomnuke.bindings.resolver.BindingResolverImpl;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.config.server.ServerConfigurationManager;
import org.atomnuke.container.context.ContextManager;
import org.atomnuke.service.ServiceManager;
import org.atomnuke.service.ServiceManagerImpl;
import org.atomnuke.kernel.NukeRejectionHandler;
import org.atomnuke.kernel.NukeThreadPoolExecutor;
import org.atomnuke.task.manager.TaskManager;
import org.atomnuke.task.manager.TaskManagerImpl;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.task.threading.ExecutionManagerImpl;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.io.ConfigurationManager;
import org.atomnuke.util.config.update.ConfigurationContext;
import org.atomnuke.util.config.update.ConfigurationUpdateManager;
import org.atomnuke.container.service.config.ConfigurationService;
import org.atomnuke.plugin.local.LocalInstanceEnvironment;
import org.atomnuke.service.ServiceDescriptor;
import org.atomnuke.service.ServiceLifeCycle;
import org.atomnuke.service.context.ServiceContextImpl;
import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.context.TaskContextImpl;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.task.lifecycle.TaskLifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class NukeContainer {

   private static final Logger LOG = LoggerFactory.getLogger(NukeContainer.class);
   private static final ThreadFactory DEFAULT_THREAD_FACTORY = new ThreadFactory() {
      @Override
      public Thread newThread(Runnable r) {
         return new Thread(r, "nuke-worker-" + TID.incrementAndGet());
      }
   };

   private static final AtomicLong TID = new AtomicLong(0);

   private static final int NUM_PROCESSORS = Runtime.getRuntime().availableProcessors(), MAX_THREADS = NUM_PROCESSORS * 2;
   private static final int MAX_QUEUE_SIZE = 256000;

   private final ServiceManager serviceManager;
   private TaskManager taskManager;
   private NukeKernel nukeInstance;
   private ContextManager contextManager;

   public NukeContainer() {
      this.serviceManager = new ServiceManagerImpl();
   }

   public NukeKernel nukeInstance() {
      return nukeInstance;
   }

   public void init(TaskLifeCycle tlc) throws InitializationException {
      init(tlc, Collections.EMPTY_MAP);
   }

   public void init(TaskLifeCycle tlc, Map<String, String> params) throws InitializationException {
      tlc.init(new TaskContextImpl(params, serviceManager, taskManager));
   }

   public void start() {
      final long initTime = System.currentTimeMillis();

      LOG.info("Starting Nuke container...");
      LOG.debug("Building loader manager.");

      final BindingResolver bindingsResolver = BindingResolverImpl.defaultResolver();
      final DirectoryLoaderManager loaderManager = new DirectoryLoaderManager(NukeEnv.NUKE_LIB, bindingsResolver.registeredBindingContexts());

      try {
         LOG.debug("Loader manager starting.");

         loaderManager.load();
      } catch (BindingLoaderException ble) {
         LOG.debug("An error occured while loading bindings. Reason: " + ble.getMessage(), ble);

         throw new ContainerInitException(ble);
      }

      LOG.debug("Building nuke kernel.");

      final BlockingQueue<Runnable> runQueue = new LinkedBlockingQueue<Runnable>();
      final ExecutorService execService = new NukeThreadPoolExecutor(NUM_PROCESSORS, MAX_THREADS, 30, TimeUnit.SECONDS, runQueue, DEFAULT_THREAD_FACTORY, new NukeRejectionHandler());
      final ExecutionManager executionManager = new ExecutionManagerImpl(MAX_QUEUE_SIZE, runQueue, execService);

      taskManager = new TaskManagerImpl(executionManager);
      nukeInstance = new NukeKernel(executionManager, taskManager);

      LOG.debug("Building context manager.");

      contextManager = new ContextManager(serviceManager, bindingsResolver, nukeInstance, taskManager);

      LOG.debug("Buidling services.");

      final ConfigurationService cfgService = new ConfigurationService();
      cfgService.init(new ServiceContextImpl(Collections.EMPTY_MAP, serviceManager));

      serviceManager.register(new ServiceDescriptor("Stock cfg service", new LocalInstanceEnvironment<ServiceLifeCycle>(cfgService)));

      LOG.debug("Registering configuration listener.");

      try {
         final ConfigurationManager<ServerConfiguration> cfgManager = new ServerConfigurationManager(new File(NukeEnv.NUKE_HOME, NukeEnv.CONFIG_NAME));
         final ConfigurationContext<ServerConfiguration> configurationContext = serviceManager.findService(ConfigurationUpdateManager.class).register("org.atomnuke.container.cfg", cfgManager);

         configurationContext.addListener(contextManager);
      } catch (JAXBException jaxbe) {
         LOG.error(jaxbe.getMessage(), jaxbe);
         throw new ContainerInitException(jaxbe);
      } catch (ConfigurationException ce) {
         LOG.error(ce.getMessage(), ce);
         throw new ContainerInitException(ce);
      }

      LOG.debug("Kernel thread start.");

      nukeInstance.shutdownHook().enlist(serviceManager);

      nukeInstance.start();

      LOG.info("Nuke container started. Elapsed start-up time: " + (System.currentTimeMillis() - initTime) + "ms.");
   }
}
