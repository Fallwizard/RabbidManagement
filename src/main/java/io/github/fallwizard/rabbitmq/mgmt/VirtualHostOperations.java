package io.github.fallwizard.rabbitmq.mgmt;

import com.google.common.base.Optional;
import com.sun.jersey.api.client.GenericType;
import io.github.fallwizard.rabbitmq.mgmt.model.Permission;
import io.github.fallwizard.rabbitmq.mgmt.model.Status;
import io.github.fallwizard.rabbitmq.mgmt.model.VirtualHost;

import java.util.Collection;


public class VirtualHostOperations extends BaseFluent {

	public VirtualHostOperations(HttpContext httpContext, RabbitMgmtService mgmtService) {
		super(httpContext, mgmtService);
	}

	public Collection<VirtualHost> all(){
		
		return HTTP.GET("/vhosts", VHOST_COLLECTION).get();
	}
	
	public Optional<VirtualHost> get(String vhost){
		
		return HTTP.GET(String.format("/vhosts/%s", encodeSlashes(vhost)), VHOST);
    }


	public VirtualHostOperations delete(String vhost){
		
		HTTP.DELETE(String.format("/vhosts/%s", encodeSlashes(vhost)));
		
		return this;
	}
	
	public VirtualHostOperations create(VirtualHost vhost){
		
		String url = String.format("/vhosts/%s", encodeSlashes(vhost.getName()));
		
		HTTP.PUT(url, vhost);
		
		return this;
	}
	
	public Status status(){
		
		return this.status("/").get();
	}
	
	public Optional<Status> status(String vhost){
		
		return HTTP.GET(
			String.format("/aliveness-test/%s", encodeSlashes(vhost)), new GenericType<Status>(){});
	}
	
	public Collection<Permission> permissions(){
		
		return permissions("/").get();
	}
	
	public Optional<Collection<Permission>> permissions(String vhost){
		
		return HTTP.GET(String.format("/vhosts/%s/permissions", encodeSlashes(vhost)), PERMISSION_COLLECTION);
	}
	
	public Optional<Permission> permissionsForUser(String user){
		
		return permissionsForUser("/", user);
	}
	
	public Optional<Permission> permissionsForUser(String vhost, String user){
		
		return HTTP.GET(String.format("/permissions/%s/%s", encodeSlashes(vhost), user), PERMISSION);
	}
}
