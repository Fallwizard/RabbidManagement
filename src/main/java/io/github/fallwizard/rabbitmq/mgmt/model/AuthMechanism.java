package io.github.fallwizard.rabbitmq.mgmt.model;

/**
 * An Authentication Mechanism supported by the broker and whether it is enabled.
 * 
 * @author Richard Clayton (Berico Technologies)
 */
public class AuthMechanism {

	protected String name;
	protected String description;
	protected boolean enabled = true;
	
	public AuthMechanism(){}
	
	public AuthMechanism(String name, String description, boolean enabled) {
		
		this.name = name;
		this.description = description;
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String toString() {
		return "AuthMechanism [name=" + name + ", description=" + description
				+ ", enabled=" + enabled + "]";
	}
	
}
