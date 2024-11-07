package com.github.sudo.nano.raysk.pubsub.infra.impl.application;

import com.github.sudo.nano.raysk.pubsub.domain.Message;
import com.github.sudo.nano.raysk.pubsub.domain.Subscriber;
import com.github.sudo.nano.raysk.pubsub.infra.impl.application.dtos.DeliveryAcknowledgementResponse;
import com.github.sudo.nano.raysk.pubsub.infra.impl.application.dtos.DeliveryFailedResponse;
import com.github.sudo.nano.raysk.pubsub.infra.impl.application.dtos.DeliveryResponse;
import com.github.sudo.nano.raysk.pubsub.infra.impl.application.sub.MessageConsumer;
import com.github.sudo.nano.raysk.pubsub.infra.impl.application.transmission.TransmissionHandler;
import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Saksham Sethi (saksham.s@media.net)
 * @version 07/11/24
 */
public class Subscription<T extends Serializable> {
  private static final int PRODUCER_BUFFER_SIZE = 1000;

  private final MessageConsumer<T> messageConsumer;
  private final TransmissionHandler transmissionHandler;
  private final BlockingQueue<Message<T>> sendQueue;

  public Subscription(Subscriber<T> subscriber, TransmissionHandler transmissionHandler) {
    this.transmissionHandler = transmissionHandler;
    this.messageConsumer = new MessageConsumer<>(subscriber);
    this.sendQueue = new ArrayBlockingQueue<>(PRODUCER_BUFFER_SIZE);
    Thread.ofVirtual().start(this::doPublish);
  }

  public DeliveryResponse deliver(Message<T> message) {
    if (!this.sendQueue.offer(message)) {
      System.err.println("Dropping message as send queue is full " + message.getMessageData());
    }
    return new DeliveryAcknowledgementResponse();
  }

  private void doPublish() {
    while (true) {
      try {
        Message<T> message = this.sendQueue.take();
        switch (transmissionHandler.deliver(messageConsumer, message)) {
          case DeliveryAcknowledgementResponse deliveryAcknowledgementResponse:
            continue;
          case DeliveryFailedResponse(int errorCode, String errorMessage):
            // Drop for now, but an exponential backoff retry can be used here.
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }
}
