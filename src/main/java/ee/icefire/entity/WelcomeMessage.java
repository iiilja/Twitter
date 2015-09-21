package ee.icefire.entity;

import org.json.JSONObject;

/**
 * Created by ilja on 20.09.2015.
 */
public class WelcomeMessage {
    private String message;
    private String sender;

    public WelcomeMessage(String message, String sender){
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("message", message);
        json.put("sender", sender);
        return json;
    }
}
