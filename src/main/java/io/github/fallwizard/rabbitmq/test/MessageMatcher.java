package io.github.fallwizard.rabbitmq.test;

import io.github.fallwizard.rabbitmq.mgmt.model.ReceivedMessage;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public interface MessageMatcher extends Matcher<ReceivedMessage> {}