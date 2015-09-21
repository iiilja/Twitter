package ee.icefire.entity;

import org.json.JSONObject;

/**
 * Created by ilja on 20.09.2015.
 */
public class Tweet {
    private long id;
    private String html;

    public Tweet ( long tweetId, String html){
        this.id = tweetId;
        this.html = html;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("html", html);
        json.put("id", id);
        return json;
    }
}
