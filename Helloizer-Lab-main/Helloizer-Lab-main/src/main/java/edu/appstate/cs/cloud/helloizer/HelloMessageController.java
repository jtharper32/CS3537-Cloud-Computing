package edu.appstate.cs.cloud.helloizer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * Controller class for messages from the Hello-izer
 *
 * Note: This was inspired by the official Spring example at
 * https://github.com/spring-guides/gs-rest-service/blob/master/complete/src/main/java/hello/GreetingController.java
 *
 * @author Mark Hills
 * @version 1.0
 */
@RestController
public class HelloMessageController {

    // Template for formatting the message text for each message
    private static final String messageTemplate = "Hello, %s it is %s";

    // New template for the new endpoint
    private static final String newMessageTemplate = "Hi there, %s it is %s";

    @RequestMapping("/hello")
    public HelloMessage sayHello(@RequestParam(value="name", defaultValue = "") String name) {
        Date now = new Date();
        UUID messageId = UUID.randomUUID();
        String messageText = String.format(messageTemplate,
                name.trim().length() == 0 ? "anonymous person" : name.trim(),
                now.toString());
        return new HelloMessage(messageId, messageText, now);
    }

    @RequestMapping("/hi")
    public HelloMessage sayHiThere(@RequestParam(value="name", defaultValue = "") String name) {
        Date now = new Date();
        UUID messageId = UUID.randomUUID();
        String messageText = String.format(newMessageTemplate,
                name.trim().length() == 0 ? "anonymous person" : name.trim(),
                now.toString());
        return new HelloMessage(messageId, messageText, now);
    }
}
