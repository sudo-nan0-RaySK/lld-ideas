package com.github.sudo.nano.raysk.pubsub.infra.impl.application.sub;

import com.github.sudo.nano.raysk.pubsub.domain.Message;
import com.github.sudo.nano.raysk.pubsub.domain.Subscriber;
import com.github.sudo.nano.raysk.pubsub.infra.impl.application.dtos.DeliveryAcknowledgementResponse;
import com.github.sudo.nano.raysk.pubsub.infra.impl.application.dtos.DeliveryFailedResponse;
import com.github.sudo.nano.raysk.pubsub.infra.impl.application.dtos.DeliveryResponse;
import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Saksham Sethi (saksham.s@media.net)
 * @version 07/11/24
 */
public class MessageConsumer<T extends Serializable> {
  private static final int CONSUMER_BUFFER_SIZE = 1000;
  private final Subscriber<T> subscriber;
  private final BlockingQueue<Message<T>> recvQueue;

  public MessageConsumer(Subscriber<T> subscriber) {
    this.subscriber = subscriber;
    this.recvQueue = new ArrayBlockingQueue<>(CONSUMER_BUFFER_SIZE);
    Thread.ofVirtual().start(this::doConsume);
  }

  public DeliveryResponse consumeMessage(Message<T> message) {
    if (this.recvQueue.offer(message)) {
      return new DeliveryAcknowledgementResponse(); // Auto ack
    }
    return new DeliveryFailedResponse(500, "Queue is full!");
  }

  private void doConsume() {
    while (true) {
      try {
        Message<T> message = this.recvQueue.take();
        this.subscriber.handle(message.getMessageData());
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
