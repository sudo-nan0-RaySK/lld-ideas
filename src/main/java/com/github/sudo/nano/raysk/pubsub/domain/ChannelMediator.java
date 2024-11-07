package com.github.sudo.nano.raysk.pubsub.domain;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Saksham Sethi (saksham.s@media.net)
 * @version 06/11/24
 */
public interface ChannelMediator<T extends Serializable> extends AutoCloseable{
  void publish(Message<T> message) throws IOException;

  void subscribe(Subscriber<T> subscriber) throws IOException;
}
