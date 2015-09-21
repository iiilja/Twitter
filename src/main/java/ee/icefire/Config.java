package ee.icefire;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by ilja on 20.09.2015.
 */
@Configuration
@PropertySource("WEB-INF/cfg/production.properties")
public class Config {

    @Value("${hashtagNewsFeed}")
    private String hashTagNewsFeed;

    @Value("${hashtagWelcomeMessage}")
    private String hashTagWelcomeMessage;


    public String getHashTagNewsFeed() {
        return hashTagNewsFeed;
    }

    public void setHashTagNewsFeed(String hashTagNewsFeed) {
        this.hashTagNewsFeed = hashTagNewsFeed;
    }

    public String getHashTagWelcomeMessage() {
        return hashTagWelcomeMessage;
    }

    public void setHashTagWelcomeMessage(String hashTagWelcomeMessage) {
        this.hashTagWelcomeMessage = hashTagWelcomeMessage;
    }
}
