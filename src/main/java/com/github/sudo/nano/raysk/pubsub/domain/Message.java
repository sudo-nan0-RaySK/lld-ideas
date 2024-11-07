package com.github.sudo.nano.raysk.pubsub.domain;

import java.io.Serializable;
import java.util.EventObject;

/**
 * @author Saksham Sethi (saksham.s@media.net)
 * @version 06/11/24
 */
public class Message<T extends Serializable> extends EventObject {

  private final T eventData;

  /**
   * Constructs a prototypical Event.
   *
   * @param source the object on which the Event initially occurred
   * @throws IllegalArgumentException if source is null
   */
  public Message(Object source, T eventData) {
    super(source);
    this.eventData = eventData;
  }

  public T getMessageData() {
    return eventData;
  }
}
