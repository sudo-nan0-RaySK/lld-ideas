package com.github.sudo.nano.raysk.pubsub.infra.impl.application;

import com.github.sudo.nano.raysk.pubsub.domain.ChannelMediator;
import com.github.sudo.nano.raysk.pubsub.domain.Message;
import com.github.sudo.nano.raysk.pubsub.domain.Subscriber;
import com.github.sudo.nano.raysk.pubsub.infra.impl.application.transmission.InProcessTransmissionHandler;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Saksham Sethi (saksham.s@media.net)
 * @version 06/11/24
 * @implNote Supports {@code FAN-OUT} delivery mode only
 */
class ApplicationNativeChannelMediator<T extends Serializable> implements ChannelMediator<T> {

  private final String channelName;
  private final List<Subscription<T>> subscriptions;

  public ApplicationNativeChannelMediator(String channelName) {
    this.channelName = channelName;
    this.subscriptions = new CopyOnWriteArrayList<>();
  }

  public String getChannelName() {
    return channelName;
  }

  @Override
  public void publish(Message<T> message) throws IOException {
    for (Subscription<T> subscription : subscriptions) {
      subscription.deliver(message.copy());
    }
  }

  @Override
  public void subscribe(Subscriber<T> subscriber) throws IOException {
    this.subscriptions.add(new Subscription<>(subscriber, new InProcessTransmissionHandler()));
  }

  @Override
  public void close() throws Exception {
    this.subscriptions.clear();
  }
}
