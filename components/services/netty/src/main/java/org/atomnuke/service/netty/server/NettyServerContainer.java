package org.atomnuke.service.netty.server;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.atomnuke.NukeEnvironment;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 *
 * @author zinic
 */
public class NettyServerContainer implements NettyServer {

   private final ExecutorService nettyChannelWorkers, nettyBossWorkers;
   private final ServerBootstrap nettyServerBootstrap;
   private final ChannelFactory channelFactory;
   private final List<Channel> activeChannels;

   public NettyServerContainer(NukeEnvironment nukeEnvironment) {
      nettyChannelWorkers = Executors.newCachedThreadPool();
      nettyBossWorkers = Executors.newCachedThreadPool();
      
      channelFactory = new NioServerSocketChannelFactory(nettyBossWorkers, nettyChannelWorkers, nukeEnvironment.numProcessors());
      nettyServerBootstrap = new ServerBootstrap(channelFactory);
      
      activeChannels = new LinkedList<Channel>();
   }
   
   @Override
   public synchronized void setChannelPipelineFactory(ChannelPipelineFactory cpf) {
      nettyServerBootstrap.setPipelineFactory(cpf);
   }
   
   @Override
   public synchronized void open(int port) {
      try {
         final Channel newChannel = nettyServerBootstrap.bind(new InetSocketAddress(port));
         activeChannels.add(newChannel);
      } catch(ChannelException ce) {
         
      }
   }
   
   @Override
   public synchronized void close(int port) {
      for (Iterator<Channel> channelIterator = activeChannels.iterator(); channelIterator.hasNext();) {
         final Channel activeChannel = channelIterator.next();
         
         if (activeChannel.getLocalAddress() instanceof InetSocketAddress) {
            final InetSocketAddress address = (InetSocketAddress) activeChannel.getLocalAddress();
            
            if (address.getPort() == port) {
               channelIterator.remove();
               activeChannel.close();
               
               return;
            }
         }
      }
   }
   
   public void shutdown() {
      for (Channel c : activeChannels) {
         c.close();
      }
      
      nettyBossWorkers.shutdown();
      nettyChannelWorkers.shutdown();
      
      channelFactory.releaseExternalResources();
   }
}
