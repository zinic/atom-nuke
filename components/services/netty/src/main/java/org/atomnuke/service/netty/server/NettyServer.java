package org.atomnuke.service.netty.server;

import org.jboss.netty.channel.ChannelPipelineFactory;

/**
 *
 * @author zinic
 */
public interface NettyServer {

   void close(int port);

   void open(int port);

   void setChannelPipelineFactory(ChannelPipelineFactory cpf);
}
