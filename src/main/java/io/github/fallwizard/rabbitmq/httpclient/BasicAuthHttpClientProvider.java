package io.github.fallwizard.rabbitmq.httpclient;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class BasicAuthHttpClientProvider implements HttpClientProvider {

	private String username;
	private String password;
	
	public BasicAuthHttpClientProvider(String username, String password) {
		
		this.username = username;
		this.password = password;
	}

    @Override
    public boolean useSsl() {

        return false;
    }

    @Override
	public Client getClient() {
		
		ClientConfig clientConf = new DefaultClientConfig();
		clientConf.getClasses().add(GsonMessageBodyHandler.class);
		
		Client client = Client.create(clientConf);
		
		client.addFilter(new HTTPBasicAuthFilter(this.username, this.password));
		
		return client;
	}
}