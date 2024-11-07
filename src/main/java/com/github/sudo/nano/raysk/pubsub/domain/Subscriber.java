package com.github.sudo.nano.raysk.pubsub.domain;

import java.io.Serializable;
import java.util.EventListener;

/**
 * @author Saksham Sethi (saksham.s@media.net)
 * @version 06/11/24
 */
@FunctionalInterface
public interface Subscriber<T extends Serializable> extends EventListener {
  void handle(T event);
}
