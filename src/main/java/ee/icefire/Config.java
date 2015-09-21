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

    @Value("${twitterApp.customerKey}")
    private String customerKey;

    @Value("${twitterApp.customerSecret}")
    private String customerSecret;


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

    public String getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(String customerKey) {
        this.customerKey = customerKey;
    }

    public String getCustomerSecret() {
        return customerSecret;
    }

    public void setCustomerSecret(String customerSecret) {
        this.customerSecret = customerSecret;
    }
}
