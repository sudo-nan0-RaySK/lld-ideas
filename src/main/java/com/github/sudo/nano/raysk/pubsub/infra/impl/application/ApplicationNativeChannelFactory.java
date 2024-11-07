package com.github.sudo.nano.raysk.pubsub.infra.impl.application;

import com.github.sudo.nano.raysk.pubsub.domain.Channel;

import java.io.Serializable;

public final class ApplicationNativeChannelFactory {
  private ApplicationNativeChannelFactory() {}
  public static <T extends Serializable> Channel<T> createChannel(String channelName) {
    return new Channel<>(channelName, new ApplicationNativeChannelMediator<>(channelName));
  }
}
