package appengine.parser.twitter;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnFollowTwitterUsers {

    ArrayList<String> EXCLUDELIST = new ArrayList(Arrays.asList("1106664815429328897"/*michi*/));


    public void unFollow() {
        List<String> tempFollowingList = fetchFollowing();
        for (String user_id : tempFollowingList) {
            try {
                if (!EXCLUDELIST.contains(user_id)) {
                    unfollowRequest(user_id);
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> fetchFollowing() {

        List<String> tempFollowingList = new ArrayList<>();

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.twitter.com/1.1/friends/list.json?include_profile_interstitial_type=1&include_blocking=1&include_blocked_by=1&include_followed_by=1&include_want_retweets=1&include_mute_edge=1&include_can_dm=1&include_can_media_tag=1&skip_status=1&cursor=-1&user_id=1042608734&count=20")
                    .get()
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
            JSONArray usersArray = jsonObject.getJSONArray("users");
            for (int i = 0; i < usersArray.length(); i++) {
                String user_id = usersArray.getJSONObject(i).getString("id_str");
                tempFollowingList.add(user_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tempFollowingList;

    }


    private void unfollowRequest(String user_id) {
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
            RequestBody body = RequestBody.create(mediaType, "challenges_passed=false&handles_challenges=1&impression_id=&include_blocked_by=true&include_blocking=true&include_can_dm=true&include_followed_by=true&include_mute_edge=true&skip_status=true&user_id=" + user_id);
            Request request = new Request.Builder()
                    .url("https://api.twitter.com/1.1/friendships/destroy.json")
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
