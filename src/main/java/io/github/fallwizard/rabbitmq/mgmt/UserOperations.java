package io.github.fallwizard.rabbitmq.mgmt;

import com.google.common.base.Optional;
import io.github.fallwizard.rabbitmq.mgmt.model.Permission;
import io.github.fallwizard.rabbitmq.mgmt.model.User;

import java.util.Collection;


public class UserOperations extends BaseFluent {

	public UserOperations(HttpContext httpContext, RabbitMgmtService mgmtService) {
		super(httpContext, mgmtService);
	}
	
	public Collection<User> all(){
		
		return HTTP.GET("/users", USER_COLLECTION).get();
	}
	
	public User whoAmI(){
		
		return HTTP.GET("/whoami", USER).get();
	}
	
	public Optional<Collection<Permission>> permissionsFor(String user){
		
		return HTTP.GET(String.format("/users/%s/permissions", user), PERMISSION_COLLECTION);
	}
	
	public Optional<User> get(String username){
		
		return HTTP.GET(String.format("/users/%s", username), USER);
	}
	
	public UserOperations create(User user){
		
		HTTP.PUT(String.format("/users/%s", user.getName()), user);
		
		return this;
	}
	
	public UserOperations delete(String name){
		
		HTTP.DELETE(String.format("/users/%s", name));
		
		return this;
	}
}
