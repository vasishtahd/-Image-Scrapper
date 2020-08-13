package appengine.parser.twitter;

import appengine.parser.utils.DataBaseConnector;
import okhttp3.*;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Result;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static appengine.parser.mysqlmodels.Tables.TWITTERFOLLOWERS;

public class FollowTwitterUsers {

    private static twitter4j.Twitter mTwitterInstance;

    public void makeLimitRequest() {

        if (mTwitterInstance == null) {
            mTwitterInstance = Twitter.getInstance();
        }

        String followerOf = "ASU";


        //List<String> usersList = getNotFollowedUsers(followerOf);
        List<String> usersList = getNotFollowersUsersWithFilters(followerOf);

        for (String userId : usersList) {
            try {
                sendFollowrequest(userId);
                //mTwitterInstance.createFriendship(userId);
                System.out.println("follow request to : " + userId);
                updateRequestSent(userId);
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }


    }

    public void updateRequestSent(String userId) {
        try {
            DSLContext dslContext = DataBaseConnector.getDSLContext();
            dslContext.update(TWITTERFOLLOWERS)
                    .set(TWITTERFOLLOWERS.IS_FOLLOW_REQUEST_SENT, (byte) 1)
                    .where(TWITTERFOLLOWERS.USER_ID.eq(userId))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<String> getNotFollowedUsers(String followerOf) {

        List<String> usersList = new ArrayList<>();

        try {

            DSLContext dslContext = DataBaseConnector.getDSLContext();
            Result<Record1<String>> userIdResults = dslContext.select(TWITTERFOLLOWERS.USER_ID).from(TWITTERFOLLOWERS).
                    where(TWITTERFOLLOWERS.IS_FOLLOW_REQUEST_SENT.eq((byte) 0).and(
                            TWITTERFOLLOWERS.FOLLOWER_OF.eq(followerOf)))
                    .limit(20).fetch();

            for (Record1<String> userId : userIdResults) {
                usersList.add(userId.value1());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return usersList;
    }

    public List<String> getNotFollowersUsersWithFilters(String followerOf){

        List<String> usersList = new ArrayList<>();
        Date date_2019 = new Date(119,0,1);//starts with 1900

        try {

            DSLContext dslContext = DataBaseConnector.getDSLContext();
            Result<Record1<String>> userIdResults = dslContext.select(TWITTERFOLLOWERS.USER_ID).
                    from(TWITTERFOLLOWERS).
                    where(TWITTERFOLLOWERS.IS_FOLLOW_REQUEST_SENT.eq((byte) 0)
                            .and(TWITTERFOLLOWERS.FOLLOWER_OF.eq(followerOf)
                            .and(TWITTERFOLLOWERS.ACCOUNT_CREATED_TIME.greaterOrEqual(date_2019))
                            ))
                    .limit(20).fetch();

            for (Record1<String> userId : userIdResults) {
                usersList.add(userId.value1());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return usersList;
    }

    public boolean sendFollowrequest(String userId){

        boolean didSuccesfullySent = false;
        try{

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "include_profile_interstitial_type=1&include_blocking=1&include_blocked_by=1&include_followed_by=1&include_want_retweets=1&include_mute_edge=1&include_can_dm=1&include_can_media_tag=1&skip_status=1&id="+userId);
            Request request = new Request.Builder()
                    .url("https://api.twitter.com/1.1/friendships/create.json")
                    .post(body)
                    .addHeader("origin", "https://mobile.twitter.com")
                    .addHeader("x-csrf-token", "54798415aec479a0483a4e503a042378")
                    .addHeader("accept-language", "en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("authorization", "Bearer AAAAAAAAAAAAAAAAAAAAANRILgAAAAAAnNwIzUejRCOuH5E6I8xnZz4puTs%3D1Zv7ttfk8LF81IUq16cHjhLTvJu4FA33AGWWjCpTnA")
                    .addHeader("cookie", "tfw_exp=0; personalization_id=\"v1_U4VARhfDn1ewRkxvoLCsqg==\"; guest_id=v1%3A154866087662852891; ct0=54798415aec479a0483a4e503a042378; _ga=GA1.2.2141235875.1548660880; _gid=GA1.2.1724694965.1548660880; dnt=1; ads_prefs=\"HBISAAA=\"; kdt=dYEaEy4DNDKAzqDwjZqbpE14t49TNFXQLZmk5rIN; remember_checked_on=1; _twitter_sess=BAh7CiIKZmxhc2hJQzonQWN0aW9uQ29udHJvbGxlcjo6Rmxhc2g6OkZsYXNo%250ASGFzaHsABjoKQHVzZWR7ADoPY3JlYXRlZF9hdGwrCFelYZNoAToMY3NyZl9p%250AZCIlZGNmZjcwZTJiNzU3Y2MyMmFkYjFiNWUxNzgxYmUzY2M6B2lkIiVhNDY2%250ANzllMWFkZGU5NTE5YjcyOGNkYWFmNGJhMWRiYzoJdXNlcmkEXvIkPg%253D%253D--81dbbe09f469889992d743a9c173784c7a8b86b0; twid=\"u=1042608734\"; auth_token=5d45d64c4b674258098c3c30ce6679b028fa7e2b; csrf_same_site_set=1; csrf_same_site=1; lang=en; __utma=43838368.2141235875.1548660880.1548661336.1548661336.1; __utmc=43838368; __utmz=43838368.1548661336.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmb=43838368.1.9.1548661336")
                    .addHeader("x-twitter-auth-type", "OAuth2Session")
                    .addHeader("x-twitter-client-language", "en")
                    .addHeader("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Mobile Safari/537.36")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "*/*")
                    .addHeader("referer", "https://mobile.twitter.com/")
                    .addHeader("authority", "api.twitter.com")
                    .addHeader("x-twitter-active-user", "yes")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "32a3c7e2-a25b-46fd-a555-b3d856e6ae79")
                    .build();

            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            JSONObject jsonObject = new JSONObject(responseString);
            didSuccesfullySent = jsonObject.getBoolean("following");

        }catch (Exception e){
            e.printStackTrace();
        }

        return didSuccesfullySent;
    }
}
