package appengine.parser.instagram;

import appengine.parser.instagram.objects.UserNameAndID;
import appengine.parser.utils.DataBaseConnector;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Result;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static appengine.parser.mysqlmodels.Tables.INSTAGRAMFOLLOWERS;
import static appengine.parser.mysqlmodels.Tables.INSTAGRAMFOLLOWERSUPDATE;


public class FollowUsers {

    public String follow(String pageName, String userCurrentName) {


        DSLContext dslContext = DataBaseConnector.getDSLContext();

        Record1<Integer> followedTillRecord = dslContext.select(INSTAGRAMFOLLOWERSUPDATE.FOLLOWED_TILL).from(INSTAGRAMFOLLOWERSUPDATE)
                .where(INSTAGRAMFOLLOWERSUPDATE.FROM_USER_NAME.eq(userCurrentName).and(INSTAGRAMFOLLOWERSUPDATE.PAGENAME.
                        eq(pageName))).fetchOne();


        int followedTill = 0;

        if (followedTillRecord != null) {
            followedTill = followedTillRecord.value1();
        }


        makeRequest(pageName, userCurrentName, followedTill);

        return "";
    }

    private synchronized void makeRequest(String pageName, String userCurrentName, int followedTill) {


        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record3<Integer, String, String>> followersResult = dslContext.select(
                INSTAGRAMFOLLOWERS.ID, INSTAGRAMFOLLOWERS.USER_ID, INSTAGRAMFOLLOWERS.USER_NAME).from(INSTAGRAMFOLLOWERS).
                where(INSTAGRAMFOLLOWERS.ID.greaterThan(followedTill).and(INSTAGRAMFOLLOWERS.PAGENAME.eq(pageName))).limit(5).fetch();


        List<UserNameAndID> followersList = new ArrayList<>();

        for (int i = 0; i < followersResult.size(); i++) {
            followersList.add(new UserNameAndID(followersResult.get(i).value1(), followersResult.get(i).value2(), followersResult.get(i).value3()));
        }


        for (int i = 0; i < followersList.size(); i++) {
            try {
                boolean isSuccessFul = fireRequest(followersList.get(i));
                if (isSuccessFul) {
                    updateInDB(pageName, userCurrentName, followersList.get(i));
                    Thread.sleep(12000);
                } else {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void updateInDB(String pageName, String currentUserName, UserNameAndID userNameAndID) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(INSTAGRAMFOLLOWERSUPDATE, INSTAGRAMFOLLOWERSUPDATE.PAGENAME, INSTAGRAMFOLLOWERSUPDATE.FROM_USER_NAME,
                INSTAGRAMFOLLOWERSUPDATE.FOLLOWED_TILL).values(pageName, currentUserName, userNameAndID.id).onDuplicateKeyUpdate()
                .set(INSTAGRAMFOLLOWERSUPDATE.FOLLOWED_TILL, userNameAndID.id).execute();
    }

    private boolean fireRequest(UserNameAndID userNameAndID) {


        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(null, new byte[]{});

            Request request = new Request.Builder()
                    .url("https://www.instagram.com/web/friendships/" + userNameAndID.userID + "/follow/")
                    .post(body)
                    .addHeader("origin", "https://www.instagram.com")
                    .addHeader("x-instagram-ajax", "e804ac7f6fd6")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "*/*")
                    .addHeader("x-devtools-emulate-network-conditions-client-id", "49BC859311D65BE5D4F8F545036E450D")
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Mobile Safari/537.36")
                    .addHeader("x-csrftoken", "TU8D53WwzjRj8S6168pLVOPPlcfCj6BL")
                    .addHeader("referer", "https://www.instagram.com/i_am_sugith_raj/")
                    .addHeader("accept-language", "en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("cookie", "mid=XEja7AAEAAFoRCR1XuqIlWgVxuHC; mcd=3; fbm_124024574287414=base_domain=.instagram.com; csrftoken=TU8D53WwzjRj8S6168pLVOPPlcfCj6BL; shbid=4980; ds_user_id=5704301242; sessionid=5704301242%3A1eucECgBNfQ5IS%3A9; rur=FTW; shbts=1548571738.8765042; urlgen=\"{\\\"66.253.196.25\\\": 23473}:1gnocg:5-3NBznawRv3EfVPawnNdQn09Kk\"")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "a8da0161-10a0-84a9-1cca-0a9dfb712fd4")
                    .build();

            Response response = client.newCall(request).execute();
            String responseString = response.body().string();

            JSONObject jsonObject = new JSONObject(responseString);
            if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }


}
