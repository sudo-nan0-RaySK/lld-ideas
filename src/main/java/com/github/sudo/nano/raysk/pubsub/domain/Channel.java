package com.github.sudo.nano.raysk.pubsub.domain;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Saksham Sethi (saksham.s@media.net)
 * @version 06/11/24
 */
public class Channel<T extends Serializable> implements AutoCloseable {
  private final String id;
  private final ChannelMediator<T> mediator;

  public Channel(String id, ChannelMediator<T> mediator) {
    this.id = id;
    this.mediator = mediator;
  }

  public String getId() {
    return id;
  }

  public void publish(T messageContent) throws IOException {
    var message = new Message<>(mediator, messageContent);
    mediator.publish(message);
  }

  public void subscribe(Subscriber<T> subscriber) throws IOException {
    mediator.subscribe(subscriber);
  }

  @Override
  public void close() throws Exception {
    mediator.close();
  }
}
