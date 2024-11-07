package com.github.sudo.nano.raysk.pubsub.infra.impl.rabbitmq;

import com.github.sudo.nano.raysk.pubsub.domain.ChannelMediator;
import com.github.sudo.nano.raysk.pubsub.domain.Message;
import com.github.sudo.nano.raysk.pubsub.domain.Subscriber;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author Saksham Sethi (saksham.s@media.net)
 * @version 06/11/24
 */
class RabbitMQChannelMediator<T extends Serializable> implements ChannelMediator<T> {
  private final String exchangeName;
  private final Connection connection;
  private final Channel rabbitMQChannel;

  public RabbitMQChannelMediator(String exchangeName) throws TimeoutException, IOException {
    this.exchangeName = exchangeName;
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost("localhost");
    this.connection = connectionFactory.newConnection();
    this.rabbitMQChannel = this.connection.createChannel();

    this.rabbitMQChannel.exchangeDeclare(exchangeName, "fanout");
  }

  @Override
  public void publish(Message<T> message) throws IOException {
    rabbitMQChannel.basicPublish(
        exchangeName,
        "",
        null,
        message.getMessageData().toString().getBytes(StandardCharsets.UTF_8));
  }

  @SuppressWarnings("unchecked")
  @Override
  public void subscribe(Subscriber<T> subscriber) throws IOException {
    String anonQueue = rabbitMQChannel.queueDeclare().getQueue();
    rabbitMQChannel.queueBind(anonQueue, exchangeName, "");
    rabbitMQChannel.basicConsume(
        anonQueue,
        true,
        (consumerTag, message) -> {
          byte[] buf = message.getBody();
          // TODO: Handle type casting in a type safe way
          subscriber.handle((T) new String(buf, StandardCharsets.UTF_8));
        },
        consumerTag -> {});
  }

  @Override
  public void close() throws Exception {
    this.rabbitMQChannel.close();
    this.connection.close();
  }
}
