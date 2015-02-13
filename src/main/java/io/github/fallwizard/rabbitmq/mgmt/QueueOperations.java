package io.github.fallwizard.rabbitmq.mgmt;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.fallwizard.rabbitmq.mgmt.model.Binding;
import io.github.fallwizard.rabbitmq.mgmt.model.ConsumeOptions;
import io.github.fallwizard.rabbitmq.mgmt.model.Queue;
import io.github.fallwizard.rabbitmq.mgmt.model.ReceivedMessage;

import java.util.Collection;


public class QueueOperations extends BaseFluent {
	
	private static final Logger logger = LoggerFactory.getLogger(QueueOperations.class);
	
	public QueueOperations(HttpContext httpContext, RabbitMgmtService mgmtService) {
		super(httpContext, mgmtService);
	}
	
	/**
	 * Get all queues, regardless of what virtual host it is on.
	 * @return Collection of Queues
	 */
	public Collection<Queue> all(){
		
		return HTTP.GET("/queues", QUEUE_COLLECTION).get();
	}
	
	public Collection<Queue> allOnDefault(String vhost){
		
		logger.debug("Getting queues at from the default vhost.");
		
		return allOnVHost("/").get();
	}
	
	public Optional<Collection<Queue>> allOnVHost(String vhost){
		
		logger.debug("Getting queues on vhost: {}.", vhost);
		
		return HTTP.GET(
			String.format("/queues/%s", encodeSlashes(vhost)), QUEUE_COLLECTION);
	}
	
	public Optional<Queue> get(String queueName){
		
		return get("/", queueName);
	}
	
	public Optional<Queue> get(String vhost, String queueName){
		
		return HTTP.GET(
			String.format("/queues/%s/%s", encodeSlashes(vhost), queueName), QUEUE);
	}
	
	public QueueOperations create(Queue queue){
		
		String url = String.format("/queues/%s/%s", encodeSlashes(queue.getVhost()), queue.getName());
		
		HTTP.PUT(url, queue);
		
		return this;
	}
	
	public QueueOperations delete(String queueName){
		
		return delete("/", queueName);
	}
	
	public QueueOperations delete(String vhost, String queueName){
		
		String url = String.format("/queues/%s/%s", encodeSlashes(vhost), queueName);
		
		HTTP.DELETE(url);
		
		return this;
	}

    public Optional<Collection<ReceivedMessage>> consume(String queueName){

        return this.consume("/", queueName);
    }

    public Optional<Collection<ReceivedMessage>> consume(String queueName, ConsumeOptions options){

        return this.consume("/", queueName, options);
    }

    public Optional<Collection<ReceivedMessage>> consume(String vhost, String queueName){

        return this.consume(vhost, queueName, new ConsumeOptions());
    }

    public Optional<Collection<ReceivedMessage>> consume(String vhost, String queueName, ConsumeOptions options){

        String url = String.format("/queues/%s/%s/get", encodeSlashes(vhost), queueName);

        return HTTP.POST(url, options, MESSAGE_COLLECTION);
    }
	
	public QueueOperations purge(String queueName){
		
		return this.purge("/", queueName);
	}
	
	public QueueOperations purge(String vhost, String queueName){
		
		String url = String.format("/queues/%s/%s/contents", encodeSlashes(vhost), queueName);
		
		HTTP.DELETE(url);
		
		return this;
	}
	
	public Optional<Collection<Binding>> bindings(String queueName){
		
		return bindings("/", queueName);
	}
	
	public Optional<Collection<Binding>> bindings(String vhost, String queueName){
		
		return HTTP.GET(
			String.format("/queues/%s/%s/bindings", encodeSlashes(vhost), queueName), 
			BINDING_COLLECTION);
	}
}
