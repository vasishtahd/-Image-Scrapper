package appengine.parser.twitter;

import appengine.parser.mysqlmodels.Tables;
import appengine.parser.utils.DataBaseConnector;
import okhttp3.*;
import org.jooq.DSLContext;
import org.jooq.Record1;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static appengine.parser.mysqlmodels.Tables.WEARESWIGGY;

/**
 * Created by anand.kurapati on 27/06/17.
 */
public class Twitter {

    private static int DEFAULT_COUNT = 20;

    private static twitter4j.Twitter mTwitterInstance;

    public static twitter4j.Twitter getInstance() {
        if (mTwitterInstance == null) {

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("T7j1YSoO2aZC5Ky0Ikmh9aUJC")
                    .setOAuthConsumerSecret("5atCu729CzdLansmZWl10Eqo5t3XRVMg7WHBgmqMA0a8TcB0Wj")
                    .setOAuthAccessToken("1042608734-vZKLuJBu65pR2ZNUI3oyM0dDxJMveQQB15XphLt")
                    .setOAuthAccessTokenSecret("UM5MxusC1cLkLYIeM1LkpZpMIoiXRzuMb0KpAr2JJ1uQY");

            TwitterFactory tf = new TwitterFactory(cb.build());
            mTwitterInstance = tf.getInstance();
        }
        return mTwitterInstance;
    }

    public static QueryResult getTweets(String query) {
        return getTweets(query, DEFAULT_COUNT);
    }

    public static QueryResult getTweets(String search_string, int count) {
        try {
            twitter4j.Twitter twitter = getInstance();
            Query query = new Query(search_string);
            query.count(count);
            query.setGeoCode(new GeoLocation(21.94, 76.99), 2000, Query.Unit.km);
            QueryResult queryResult = twitter.search(query);
            return queryResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getTweetsFromTimeLine() {
        twitter4j.Twitter twitter = getInstance();
        try {

            Paging paging = new Paging();
            paging.setSinceId(getLastTweet());

            ResponseList<Status> reverseStatus = twitter.getUserTimeline("WeAreSwiggy", paging);

            List<Status> statuses = new ArrayList<Status>();

            for(Status status : reverseStatus){
                statuses.add(0,status);
            }


            for (Status status : statuses) {
                updateLastTweet(String.valueOf(status.getId()));
                try {
                    List<String> urls = extractUrls(status.getText());
                    if (urls.size() > 0) {
                        String twitterLink = urls.get(urls.size() - 1);
                        postTweetOnSlack(twitterLink);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    private static void updateLastTweet(String sinceId) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(WEARESWIGGY, WEARESWIGGY.LASTTWEET, WEARESWIGGY.ISUNIQUE)
                .values(sinceId, (byte) 1).onDuplicateKeyUpdate()
                .set(WEARESWIGGY.LASTTWEET, sinceId)
                .execute();
    }

    private static long getLastTweet() {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Record1<String> twitterId = dslContext.select(Tables.WEARESWIGGY.LASTTWEET).from(Tables.WEARESWIGGY).fetchOne();
        return Long.parseLong(twitterId.value1());
    }

    private static void postTweetOnSlack(String text) {
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, "{\"text\":\"" + text + "\"}");
            Request request = new Request.Builder()
                    .url("https://hooks.slack.com/services/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")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "8fa86be2-d201-9b05-5249-2be48eeb8a59")
                    .build();
            Response response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
