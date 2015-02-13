package io.github.fallwizard.rabbitmq.httpclient;

import com.sun.jersey.api.client.Client;

public interface HttpClientProvider {

    public boolean useSsl();

	public Client getClient();
	
}