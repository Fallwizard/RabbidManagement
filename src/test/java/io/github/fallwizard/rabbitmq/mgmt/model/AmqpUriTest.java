package io.github.fallwizard.rabbitmq.mgmt.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class AmqpUriTest {

    @Test
    public void test_getUri__host_only(){

        String expected = "amqp://rabbit.example.com";

        String actual = AmqpUri.builder().host("rabbit.example.com").build().getUri();

        assertEquals(expected, actual);
    }

    @Test
    public void test_getUri__host_and_port(){

        String expected = "amqp://rabbit.example.com:11111";

        String actual = AmqpUri.builder().host("rabbit.example.com").port(11111).build().getUri();

        assertEquals(expected, actual);
    }

    @Test
    public void test_getUri__host_and_port_and_vhost(){

        String expected = "amqp://rabbit.example.com:11111/my-vhost";

        String actual = AmqpUri.builder().host("rabbit.example.com").port(11111).vhost("my-vhost").build().getUri();

        assertEquals(expected, actual);
    }

    @Test
    public void test_getUri__host_and_port_and_vhost_and_basic_auth(){

        String expected = "amqp://guest:guest@rabbit.example.com:11111/my-vhost";

        String actual = AmqpUri.builder()
                .host("rabbit.example.com").port(11111).vhost("my-vhost")
                .credentials("guest", "guest").build().getUri();

        assertEquals(expected, actual);
    }

    @Test
    public void test_getUri__ssl_with_certificates(){

        String expected = "amqps://rabbit.example.com/my-vhost?" +
                "cacertfile=/certs/cacert.pem&certfile=/certs/cert.pem&keyfile=/certs/key.pem";

        String actual = AmqpUri.builder()
                .host("rabbit.example.com")
                .vhost("my-vhost")
                .useSsl("/certs/cacert.pem", "/certs/cert.pem", "/certs/key.pem")
                .build().getUri();

        assertEquals(expected, actual);
    }

    @Test
    public void test_getUri__ssl_with_certificates_and_verify_peer(){

        String expected = "amqps://rabbit.example.com/my-vhost?" +
                "cacertfile=/certs/cacert.pem&certfile=/certs/cert.pem&keyfile=/certs/key.pem" +
                "&verify=verify";

        String actual = AmqpUri.builder()
                .host("rabbit.example.com")
                .vhost("my-vhost")
                .useSsl("/certs/cacert.pem", "/certs/cert.pem", "/certs/key.pem")
                .verifyCertificateIfPresent()
                .build().getUri();

        assertEquals(expected, actual);
    }

    @Test
    public void test_getUri__ssl_with_certificates_and_fail_if_no_peer(){

        String expected = "amqps://rabbit.example.com/my-vhost?" +
                "cacertfile=/certs/cacert.pem&certfile=/certs/cert.pem&keyfile=/certs/key.pem" +
                "&fail_if_no_peer_cert=true";

        String actual = AmqpUri.builder()
                .host("rabbit.example.com")
                .vhost("my-vhost")
                .useSsl("/certs/cacert.pem", "/certs/cert.pem", "/certs/key.pem")
                .requirePeerCertificate()
                .build().getUri();

        assertEquals(expected, actual);
    }


    @Test
    public void test_getUri__ssl_with_certificates_and_auth_mechanism(){

        String expected = "amqps://rabbit.example.com/my-vhost?" +
                "cacertfile=/certs/cacert.pem&certfile=/certs/cert.pem&keyfile=/certs/key.pem" +
                "&auth_mechanism=external";

        String actual = AmqpUri.builder()
                .host("rabbit.example.com")
                .vhost("my-vhost")
                .useSsl("/certs/cacert.pem", "/certs/cert.pem", "/certs/key.pem")
                .authMechanism("external")
                .build().getUri();

        assertEquals(expected, actual);
    }
}
