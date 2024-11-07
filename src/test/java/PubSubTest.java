import com.github.sudo.nano.raysk.pubsub.domain.Channel;
import com.github.sudo.nano.raysk.pubsub.infra.impl.application.ApplicationNativeChannelFactory;
import com.github.sudo.nano.raysk.pubsub.infra.impl.rabbitmq.RabbitMQChannelFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author Saksham Sethi (saksham.s@media.net)
 * @version 06/11/24
 */
class PubSubTest {

  @Nested
  class ApplicationNativePubSubTests {
    @Test
    void testApplicationNativePubSubChannelCreation() {
      Assertions.assertDoesNotThrow(
          () -> ApplicationNativeChannelFactory.createChannel("test-channel"));
    }
  }

  @Nested
  class RabbitMQTests {
    @Test
    void testRabbitMQChannelCreation() {
      Assertions.assertDoesNotThrow(() -> RabbitMQChannelFactory.createChannel("test-channel"));
    }

    @Test
    void testRabbitMQPublish() {
      try (Channel<String> channel = RabbitMQChannelFactory.createChannel("test-channel")) {
        Assertions.assertDoesNotThrow(() -> channel.publish("message #1"));
        Assertions.assertDoesNotThrow(() -> channel.publish("message #2"));
        Assertions.assertDoesNotThrow(() -> channel.publish("message #3"));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    // Try this in main(String...)
    void runRabbitMQNonDurableSubscribe() {
      new Thread(
              () -> {
                try (Channel<String> channel =
                    RabbitMQChannelFactory.createChannel("test-channel")) {
                  for (int i = 0; i < 100; i++) {
                    channel.publish("Hey");
                    channel.publish("Hola!");
                    Thread.sleep(100);
                  }
                } catch (Exception e) {
                  throw new RuntimeException(e);
                }
              })
          .start();
      new Thread(
              () -> {
                try (Channel<String> channel =
                    RabbitMQChannelFactory.createChannel("test-channel")) {
                  channel.subscribe(msg -> System.out.println("Sub #1 " + msg));
                  Thread.sleep(60000);
                } catch (Exception e) {
                  throw new RuntimeException(e);
                }
              })
          .start();
      new Thread(
              () -> {
                try (Channel<String> channel =
                    RabbitMQChannelFactory.createChannel("test-channel")) {
                  channel.subscribe(msg -> System.out.println("Sub #2 " + msg));
                  Thread.sleep(60000);
                } catch (Exception e) {
                  throw new RuntimeException(e);
                }
              })
          .start();
    }
  }
}
