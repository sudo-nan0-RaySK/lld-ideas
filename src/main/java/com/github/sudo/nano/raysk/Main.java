package com.github.sudo.nano.raysk;

import com.github.sudo.nano.raysk.pubsub.domain.Channel;
import com.github.sudo.nano.raysk.pubsub.infra.impl.application.ApplicationNativeChannelFactory;
import java.io.IOException;
import java.util.List;

/**
 * @author Saksham Sethi (saksham.s@media.net)
 * @version 06/11/24
 */
public class Main {
  public static void main(String[] args) {
    try (Channel<String> channel = ApplicationNativeChannelFactory.createChannel("test-channel")) {
      Main.addSubscriber(channel, "1");
      Main.addSubscriber(channel, "2");
      Thread.sleep(1000);
      Main.producerWorker(channel);
      Thread.sleep(11000);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static void producerWorker(Channel<String> channel) {
    List<String> messages = List.of("Hello", "Hi", "Wassup", "Hola");
    try {
      for (String message : messages) {
        System.out.println("[Producer] " + message);
        channel.publish(message);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static void addSubscriber(Channel<String> channel, String subId) throws IOException {
    channel.subscribe(message -> System.out.println("[Subscriber: " + subId + "]: " + message));
  }
}
