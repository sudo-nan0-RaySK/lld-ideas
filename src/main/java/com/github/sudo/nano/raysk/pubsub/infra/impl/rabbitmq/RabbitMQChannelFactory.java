package com.github.sudo.nano.raysk.pubsub.infra.impl.rabbitmq;

import com.github.sudo.nano.raysk.pubsub.domain.Channel;

import java.io.Serializable;

/**
 * @author Saksham Sethi (saksham.s@media.net)
 * @version 06/11/24
 */
public class RabbitMQChannelFactory {
  public static <T extends Serializable> Channel<T> createChannel(String channelName)
      throws Exception {
    return new Channel<>(channelName, new RabbitMQChannelMediator<>(channelName));
  }
}
