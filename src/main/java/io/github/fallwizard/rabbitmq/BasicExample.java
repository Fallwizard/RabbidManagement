package io.github.fallwizard.rabbitmq;

import java.net.URISyntaxException;
import java.util.Collection;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.fallwizard.rabbitmq.httpclient.BasicAuthHttpClientProvider;
import io.github.fallwizard.rabbitmq.mgmt.RabbitMgmtService;
import io.github.fallwizard.rabbitmq.mgmt.model.*;


public class BasicExample {

    private static final Logger logger = LoggerFactory.getLogger(BasicExample.class);

    public static void main(String[] args) throws URISyntaxException {

        String hostname = (args != null && args.length > 0)? args[0] : "localhost";

        BasicAuthHttpClientProvider provider = new BasicAuthHttpClientProvider("guest", "guest");

        /*
            Intializing the service is easy.  The defaults == guest:guest@localhost:15672
         */
        RabbitMgmtService mgmt = RabbitMgmtService.builder().build();

        /* Or you can override defaults by using the builder. */
        mgmt = RabbitMgmtService.builder().host(hostname).port(15672).credentials("guest", "guest").build();

        /*
            // SSL is easy to, but you will need to enable SSL for the RabbitMQ console via configuration.
            // I wrote a post on how to do this here:
            //  https://github.com/Berico-Technologies/CMF-AMQP-Configuration/blob/master/docs/rabbitmq-mgmt-console-ssl.md

            SslWithBasicAuthHttpClientProvider provider =
                new SslWithBasicAuthHttpClientProvider(
                    "ssl/jdoe.keycert.p12", "password123", "ssl/truststore.jks", "password", "guest", "guest");
        */

        exchangesExample(mgmt);

        queueExample(mgmt);

        publishConsumeExample(mgmt);

        connectionExample(mgmt);
    }

    public static void exchangesExample(RabbitMgmtService mgmt){

        Exchange ex = new Exchange("my.exchange");

        log(mgmt.exchanges()
                .create(ex)
                .get("my.exchange"));

        mgmt.exchanges().delete("my.exchange");
    }

    public static void queueExample(RabbitMgmtService mgmt){

        Queue q = new Queue("my.queue");

        log(mgmt.queues()
                .create(q)
                .get("my.queue"));

        mgmt.queues().delete("my.queue");
    }

    public static void publishConsumeExample(RabbitMgmtService mgmt){

        mgmt.queues()
                .create(new Queue("q1"))
            .and()
            .exchanges()
                .create(new Exchange("ex1"))
            .and()
            .bindings()
                .create(new Binding("ex1", "q1", "topic1"));

        mgmt.exchanges().publish("ex1", Message.builder().routingKey("topic1").payload("Hello!").build());

        Optional<Collection<ReceivedMessage>> messages =
                mgmt.queues().consume("q1", ConsumeOptions.builder().requeueMessage(false).build());

        log(messages);
    }

    public static void connectionExample(RabbitMgmtService mgmt){

        Collection<Connection> all = mgmt.connections().all();

        log(all);
    }

    public static void log(String template, Object... args) {

        logger.info(template, args);
    }

    public static void log(Object obj) {

        logger.info("{}", obj);
    }
}
