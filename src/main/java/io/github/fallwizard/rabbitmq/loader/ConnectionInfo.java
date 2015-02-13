package io.github.fallwizard.rabbitmq.loader;

import io.github.fallwizard.rabbitmq.httpclient.BasicAuthHttpClientProvider;
import io.github.fallwizard.rabbitmq.httpclient.HttpClientProvider;
import io.github.fallwizard.rabbitmq.httpclient.SslWithBasicAuthHttpClientProvider;
import io.github.fallwizard.rabbitmq.mgmt.RabbitMgmtService;

/**
 * This class is meant to be serializable.  So, in general, these properties
 * are only redundantly defined for ease of use by a developer or admin who
 * may need to defined them in a XML or JSON file.
 *
 * @author Richard Clayton (Berico Technologies)
 */
public class ConnectionInfo {

    String hostname = "localhost";

    int port = 15672;

    String username = "guest";

    String password = "guest";

    boolean useSsl = false;

    String keystore;

    String keystorePassword;

    String truststore;

    String truststorePassword;

    public ConnectionInfo(){}

    public ConnectionInfo(String hostname, int port, String username, String password){

        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.useSsl = false;
    }

    public ConnectionInfo(String hostname, int port, String username, String password,
                          String keystore, String keystorePassword, String truststore, String truststorePassword) {

        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.useSsl = true;
        this.keystore = keystore;
        this.keystorePassword = keystorePassword;
        this.truststore = truststore;
        this.truststorePassword = truststorePassword;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getTruststore() {
        return truststore;
    }

    public void setTruststore(String truststore) {
        this.truststore = truststore;
    }

    public String getTruststorePassword() {
        return truststorePassword;
    }

    public void setTruststorePassword(String truststorePassword) {
        this.truststorePassword = truststorePassword;
    }

    /**
     * Build a connection using the internal connection information.
     * @return
     */
    public RabbitMgmtService buildConnection(){

        HttpClientProvider provider = null;

        if (useSsl){

            provider = new SslWithBasicAuthHttpClientProvider(
                    keystore, keystorePassword, truststore, truststorePassword, username, password);
        }
        else {

            provider = new BasicAuthHttpClientProvider(username, password);
        }

        return new RabbitMgmtService(hostname, port, provider);
    }
}
