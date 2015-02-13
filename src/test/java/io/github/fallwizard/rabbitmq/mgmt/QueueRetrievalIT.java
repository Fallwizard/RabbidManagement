package io.github.fallwizard.rabbitmq.mgmt;

import io.github.fallwizard.test.RequireProperties;
import io.github.fallwizard.test.TestProperties;
import org.junit.Rule;
import org.junit.Test;
import io.github.fallwizard.rabbitmq.mgmt.model.Binding;
import io.github.fallwizard.rabbitmq.mgmt.model.Message;
import io.github.fallwizard.rabbitmq.mgmt.model.Queue;

import static org.junit.Assert.*;
/**
 * @author Richard Clayton (Berico Technologies)
 */
public class QueueRetrievalIT extends ITBase {

    @Rule
    public TestProperties properties = new TestProperties();

    @Test
    @RequireProperties({ USERNAME, PASSWORD, HOSTNAME, PORT })
    public void queue_properties_returned_from_rabbit_are_not_pruned_by_gson() throws InterruptedException {

        RabbitMgmtService mgmt = getManagementService();

        String defaultExchange = "amq.direct";

        String routingKey = "io.github.fallwizard.rabbitmq.mgmt.QueueRetrievalIT_1";

        Queue expected = new Queue("test-queue-" + System.nanoTime());

        mgmt.queues().create(expected);

        // Add it to the cleanup bucket in ITBase.
        queuesToDelete.add(expected.getName());

        mgmt.bindings()
                .create(Binding.builder()
                        .fromExchange(defaultExchange)
                        .toQueue(expected.getName())
                        .routingKey(routingKey)
                        .build());

        mgmt.exchanges().publish(defaultExchange,
                Message.builder().routingKey(routingKey).payload("Test Message.").build());

        Thread.sleep(1000);

        Queue actual = mgmt.queues().get(expected.getName()).get();

        assertEquals(1, actual.getMessages());
    }

}
