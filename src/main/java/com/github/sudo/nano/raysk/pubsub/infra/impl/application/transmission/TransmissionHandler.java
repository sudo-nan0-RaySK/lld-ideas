package com.github.sudo.nano.raysk.pubsub.infra.impl.application.transmission;

import com.github.sudo.nano.raysk.pubsub.domain.Message;
import com.github.sudo.nano.raysk.pubsub.infra.impl.application.dtos.DeliveryResponse;
import com.github.sudo.nano.raysk.pubsub.infra.impl.application.sub.MessageConsumer;

import java.io.Serializable;

/**
 * @author Saksham Sethi (saksham.s@media.net)
 * @version 07/11/24
 */
public interface TransmissionHandler {
  <T extends Serializable> DeliveryResponse deliver(MessageConsumer<T> messageConsumer, Message<T> message);
}
