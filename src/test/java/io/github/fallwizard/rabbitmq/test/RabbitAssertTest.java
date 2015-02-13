package io.github.fallwizard.rabbitmq.test;

import org.junit.Test;
import io.github.fallwizard.rabbitmq.mgmt.model.Exchange;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
/**
 * @author Richard Clayton (Berico Technologies)
 */
public class RabbitAssertTest {

    Exchange e1topic = Exchange.builder().name("e1topic").topic().build();
    Exchange e2direct = Exchange.builder().name("e2direct").direct().build();
    Exchange e3fanout = Exchange.builder().name("e3fanout").fanout().build();
    Exchange e4direct = Exchange.builder().name("e4direct").direct().build();

    ExchangeMatcher isDirect = ExchangeMatchers.isDirectType();
    ExchangeMatcher nameE1topic = ExchangeMatchers.hasExName("e1topic");
    ExchangeMatcher nameE2direct = ExchangeMatchers.hasExName("e2direct");
    ExchangeMatcher isTopic = ExchangeMatchers.isTopicType();

    @Test
    public void test_hasItemThatMatches__should_return_an_affirmative_match_result(){

        RabbitAssert.MatchResult shouldMatchE1Topic =
                RabbitAssert.hasItemThatMatches(
                        Arrays.asList(e1topic, e2direct),
                        new ExchangeMatcher[]{nameE1topic, isTopic});

        assertTrue(shouldMatchE1Topic.isMatch());
    }

    @Test
    public void test_hasItemThatMatches__should_return_a_negative_match_result(){

        RabbitAssert.MatchResult shouldNotMatchAny =
                RabbitAssert.hasItemThatMatches(
                        Arrays.asList(e1topic, e2direct),
                        new ExchangeMatcher[]{isDirect, nameE1topic});

        assertFalse(shouldNotMatchAny.isMatch());
    }

    @Test
    public void test_doesNotHaveItemThatMatches__should_return_a_negative_match_result(){

        RabbitAssert.MatchResult result =
                RabbitAssert.doesNotHaveItemThatMatches(
                        Arrays.asList(e3fanout, e4direct),
                        new ExchangeMatcher[]{nameE2direct, isTopic});

        assertFalse(result.isMatch());
    }

    @Test
    public void test_doesNotHaveItemThatMatches__should_return_an_affirmative_match_result(){

        RabbitAssert.MatchResult result =
                RabbitAssert.doesNotHaveItemThatMatches(
                        Arrays.asList(e3fanout, e4direct),
                        new ExchangeMatcher[]{isDirect});

        assertTrue(result.isMatch());
    }

    @Test
    public void test_isMatch__should_return_a_affirmative_match_result(){

        RabbitAssert.MatchResult result =
                RabbitAssert.isMatch(e4direct, new ExchangeMatcher[]{ isDirect });

        assertTrue(result.isMatch());
    }

    @Test
    public void test_isMatch__should_return_a_negative_match_result(){

        RabbitAssert.MatchResult result =
                RabbitAssert.isMatch(e1topic, new ExchangeMatcher[]{ nameE1topic, isDirect });

        assertFalse(result.isMatch());
    }

    @Test
    public void test_doesNotMatch__should_return_a_affirmative_match_result(){

        RabbitAssert.MatchResult result =
                RabbitAssert.doesNotMatch(e1topic, new ExchangeMatcher[]{ nameE1topic, isDirect });

        assertFalse(result.isMatch());
    }

    @Test
    public void test_doesNotMatch__should_return_a_negative_match_result(){

        RabbitAssert.MatchResult result =
                RabbitAssert.doesNotMatch(e2direct, new ExchangeMatcher[]{ nameE2direct, isDirect });

        assertTrue(result.isMatch());
    }

    @Test
    public void test_formatReasons(){

        String expected = "[ It's not you; it's me. | Why lord? Why? ]";

        String actual = RabbitAssert.formatReasons(Arrays.asList("It's not you; it's me.", "Why lord? Why?"));

        assertEquals(expected, actual);
    }

    @Test
    public void test_splitTags(){

        List<String> expected = Arrays.asList("admin", "service", "trusted");

        List<String> actual = RabbitAssert.splitTags("admin,service,trusted");

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
    }
}
