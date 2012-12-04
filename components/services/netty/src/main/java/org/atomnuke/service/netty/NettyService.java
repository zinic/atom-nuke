package org.atomnuke.service.netty;

import org.atomnuke.container.service.annotation.NukeService;
import org.atomnuke.lifecycle.InitializationException;
import org.atomnuke.service.ServiceContext;
import org.atomnuke.service.netty.server.NettyServer;
import org.atomnuke.service.netty.server.NettyServerContainer;
import org.atomnuke.service.runtime.AbstractRuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
@NukeService
public class NettyService extends AbstractRuntimeService {

   private static final Logger LOG = LoggerFactory.getLogger(NettyService.class);

   private NettyServerContainer container;
   
   public NettyService() {
      super(NettyServer.class);
   }

   @Override
   public Object instance() {
      return container;
   }

   @Override
   public void init(ServiceContext context) throws InitializationException {
      container = new NettyServerContainer(context.environment());
   }

   @Override
   public void destroy() {
      container.shutdown();
   }
}
