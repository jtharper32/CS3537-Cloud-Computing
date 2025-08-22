package edu.appstate.cs.cloud.helloizer;

import java.util.Date;
import java.util.UUID;

/**
 * A Hello Message
 *
 * @author Mark Hills
 * @version 1.0
 */
public class HelloMessage {
    /** Unique ID for the hello message */
    private final UUID messageId;

    /** Text of the hello message */
    private final String message;

    /** Date and time of the message */
    private final Date messageDate;

    /**
     * Create a new Hello Message
     *
     * @param id the unique ID of the message
     * @param message the text of the message
     * @param messageDate the date and time of the message
     */
    public HelloMessage(UUID id, String message, Date messageDate) {
        messageId = id;
        this.message = message;
        this.messageDate = messageDate;
    }

    /**
     * Retrieve the ID of the message
     *
     * @return the ID of the message
     */
    public UUID getMessageId() {
        return messageId;
    }

    /**
     * Retrieve the text of the message
     *
     * @return the text of the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Retrieve the date and time of the message
     *
     * @return the date and time of the message
     */
    public Date getMessageDate() {
        return messageDate;
    }


}
