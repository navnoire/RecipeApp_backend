package info.navnoire.recipeappserver_springboot.controller.payload.response;

import java.util.Date;

public class MessageResponse {
    private int statusCode;
    private Date timestamp;
    private String message;

    public MessageResponse(int statusCode, Date timestamp, String message) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
