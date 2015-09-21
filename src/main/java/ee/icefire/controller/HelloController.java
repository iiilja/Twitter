package ee.icefire.controller;

import ee.icefire.Config;
import ee.icefire.entity.Tweet;
import ee.icefire.entity.WelcomeMessage;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import twitter4j.*;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class HelloController {

	// My twitter application data
	private static final String TWITTER_CUSTOMER_KEY = "GkYiipGxl4rvRGRGak2z9DliB";
	private static final String TWITTER_CUSTOMER_SECRET = "RNkIDzzjO9gIswdzniCxxrvaSdFdwxqUGA1uibklW4Bn2Ip5Gj";

	@Autowired
	private Config config;

	private Twitter twitter;

	// To keep tweets
	private List<Tweet> tweets = new ArrayList<Tweet>();
	private WelcomeMessage welcomeMessage = new WelcomeMessage("","");

	// For debugging
	private String lastTimeUpdated = "not updated";
	private DateFormat df = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");

	public HelloController(){
		init();
	}


	/**
	 * Configuring Twitter and authentication
	 */
	private void init() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setApplicationOnlyAuthEnabled(true);
		cb.setOAuthConsumerKey(TWITTER_CUSTOMER_KEY)
				.setOAuthConsumerSecret(TWITTER_CUSTOMER_SECRET);

		TwitterFactory tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
		try {
			twitter.getOAuth2Token();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/")
	public String index(){
		return "redirect:/static/index.html";
	}

	/**
	 * Method for client requests
	 * @return JSONObject with feed and welcome message
	 */
	@RequestMapping("/lastTweets")
	public @ResponseBody
	String getLastTweets() {
		JSONObject result = new JSONObject();

		JSONArray statuses = new JSONArray();
		for (Tweet tweet : tweets) {
			statuses.put(tweet.toJSON());
		}
		result.put("statuses", statuses);
		result.put("lastUpdated", lastTimeUpdated);
		result.put("welcomeMessage", welcomeMessage.toJSON());
		return result.toString();
	}

	@RequestMapping("/getSettings")
	public @ResponseBody
	String getSettings() {
		JSONObject result = new JSONObject();

		result.put("welcomeTag", config.getHashTagWelcomeMessage());
		result.put("feedTag", config.getHashTagNewsFeed());
		return result.toString();
	}

	/**
	 * Method for client to change hashTags for feed and welcome message.
	 * Also updating feed and welcome message if hashTags change.
	 * @param data JSONObject with welcomeTag and feedTag
	 * @return JSONObject with result OK or ERROR
	 */
	@RequestMapping("/setSettings")
	public @ResponseBody
	String setSettings( @RequestParam String data) {
		JSONObject json = new JSONObject(data);
		JSONObject result = new JSONObject();

		boolean ok = json.has("welcomeTag") && json.has("feedTag");
		boolean updateTweets = ok
				&& (!config.getHashTagWelcomeMessage().equals(json.getString("welcomeTag"))
					|| !config.getHashTagNewsFeed().equals(json.getString("feedTag")));
		if (ok){
			config.setHashTagWelcomeMessage(json.getString("welcomeTag"));
			config.setHashTagNewsFeed(json.getString("feedTag"));

			result.put("result", "OK");
		} else {
			result.put("result","ERROR");
			result.put("reason","not OK JSON");
		}

		//updating feed and welcome message if hashTags changed.
		if (updateTweets){
			fetchLastTweets();
		}

		result.put("result", ok ? "OK" : "ERROR");
		return result.toString();
	}

	/**
	 * Scheduled update feed and welcome message.
	 * Getting last 10 feed tweets and one welcome message tweet.
	 * For feed tweets html is received using Twitter oEmbed
	 * Notice : max 180 twitter search request per 15 min
	 * This method does 1(feed)*pages + 1(welcome) requests
	 */
	@Scheduled(fixedDelay=60000)
	private void fetchLastTweets() {
		System.out.println("Fetching tweets from twitter");

		String feedTag = config.getHashTagNewsFeed();
		String welcomeTag = config.getHashTagWelcomeMessage();

		Query query = new Query(feedTag).resultType(Query.RECENT).count(10); // Getting 10 last
		Query welcomeQuery = new Query(welcomeTag).count(1).resultType(Query.RECENT); // Getting 1 last

		QueryResult queryResult;
		List<Tweet> tweets = new ArrayList<Tweet>();
		try {
			// Might be over 1 page of results, so using do while
			do {
				queryResult = twitter.search(query);
				for (Status status : queryResult.getTweets()) {
					// Getting html
					OEmbed oEmbed = twitter.tweets().getOEmbed(
							new OEmbedRequest(status.getId(),null).MaxWidth(500).align(OEmbedRequest.Align.CENTER));
					Tweet tweet = new Tweet(status.getId(), oEmbed.getHtml());
					tweets.add(tweet);
					query = queryResult.nextQuery();
				}
			} while (queryResult.hasNext() );

			lastTimeUpdated = df.format(new Date());
			this.tweets = tweets;

			queryResult = twitter.search(welcomeQuery);
			Status status = queryResult.getTweets().get(0);
			welcomeMessage = new WelcomeMessage(status.getText(), status.getUser().getName());

		} catch (TwitterException e) {
			//TODO: notice client about exception
			// try init once more
			init();
		}
		System.out.println("Fetch finished");
	}


}