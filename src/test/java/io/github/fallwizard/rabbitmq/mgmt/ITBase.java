package io.github.fallwizard.rabbitmq.mgmt;

import com.google.common.collect.Lists;
import org.junit.After;

import java.util.ArrayList;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class ITBase {

    public static final String HOSTNAME = "it.rabbit.hostname";
    public static final String PORT = "it.rabbit.port";
    public static final String USERNAME = "it.rabbit.username";
    public static final String PASSWORD = "it.rabbit.password";

    ArrayList<String> queuesToDelete = Lists.newArrayList();

    @After
    public void cleanup(){

        RabbitMgmtService mgmt = getManagementService();

        for (String queue : queuesToDelete){

            mgmt.queues().delete(queue);
        }
    }

    public RabbitMgmtService getManagementService(){

        String hostname = System.getProperty(HOSTNAME);
        int port = Integer.parseInt(System.getProperty(PORT));
        String username = System.getProperty(USERNAME);
        String password = System.getProperty(PASSWORD);

        return RabbitMgmtService.builder().credentials(username,password).host(hostname).port(port).build();
    }

}
