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
import static appengine.parser.mysqlmodels.Tables.INSTAGRAMUNFOLLOWERSUPDATE;

public class UnFollowUsers {

    public String follow(String pageName, String userCurrentName) {


        DSLContext dslContext = DataBaseConnector.getDSLContext();

        Record1<Integer> unFollowedTillRecord = dslContext.select(INSTAGRAMUNFOLLOWERSUPDATE.FOLLOWED_TILL).from(INSTAGRAMUNFOLLOWERSUPDATE)
                .where(INSTAGRAMUNFOLLOWERSUPDATE.FROM_USER_NAME.eq(userCurrentName).and(INSTAGRAMUNFOLLOWERSUPDATE.PAGENAME.
                        eq(pageName))).fetchOne();


        int unFollowedTill = 0;

        if (unFollowedTillRecord != null) {
            unFollowedTill = unFollowedTillRecord.value1();
        }


        makeRequest(pageName, userCurrentName, unFollowedTill);

        return "";
    }

    private synchronized void makeRequest(String pageName, String userCurrentName, int unFollowedTill) {


        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record3<Integer, String, String>> followersResult = dslContext.select(
                INSTAGRAMFOLLOWERS.ID, INSTAGRAMFOLLOWERS.USER_ID, INSTAGRAMFOLLOWERS.USER_NAME).from(INSTAGRAMFOLLOWERS).
                where(INSTAGRAMFOLLOWERS.ID.greaterThan(unFollowedTill).and(INSTAGRAMFOLLOWERS.PAGENAME.eq(pageName))).limit(5).fetch();


        List<UserNameAndID> unFollowersList = new ArrayList<>();

        for (int i = 0; i < followersResult.size(); i++) {
            unFollowersList.add(new UserNameAndID(followersResult.get(i).value1(), followersResult.get(i).value2(), followersResult.get(i).value3()));
        }


        for (int i = 0; i < unFollowersList.size(); i++) {
            try {
                boolean isSuccessFul = fireRequest(unFollowersList.get(i));
                if (isSuccessFul) {
                    updateInDB(pageName, userCurrentName, unFollowersList.get(i));
                    Thread.sleep(12000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void updateInDB(String pageName, String currentUserName, UserNameAndID userNameAndID) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(INSTAGRAMUNFOLLOWERSUPDATE, INSTAGRAMUNFOLLOWERSUPDATE.PAGENAME, INSTAGRAMUNFOLLOWERSUPDATE.FROM_USER_NAME,
                INSTAGRAMUNFOLLOWERSUPDATE.FOLLOWED_TILL).values(pageName, currentUserName, userNameAndID.id).onDuplicateKeyUpdate()
                .set(INSTAGRAMUNFOLLOWERSUPDATE.FOLLOWED_TILL, userNameAndID.id).execute();
    }



    public boolean fireRequest(UserNameAndID userNameAndID){

        try {

            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(null, new byte[]{});


            Request request = new Request.Builder()
                    .url("https://www.instagram.com/web/friendships/"+userNameAndID.userID+"/unfollow/")
                    .post(body)
                    .addHeader("origin", "https://www.instagram.com")
                   // .addHeader("accept-encoding", "gzip, deflate, br")
                    .addHeader("accept-language", "en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Mobile Safari/537.36")
                    .addHeader("cookie", "mid=XEja7AAEAAFoRCR1XuqIlWgVxuHC; mcd=3; fbm_124024574287414=base_domain=.instagram.com; csrftoken=TU8D53WwzjRj8S6168pLVOPPlcfCj6BL; shbid=4980; shbts=1548278515.0078; ds_user_id=5704301242; sessionid=5704301242%3A1eucECgBNfQ5IS%3A9; rur=FTW; urlgen=\"{\\\"129.219.21.183\\\": 2900\\054 \\\"129.219.21.1\\\": 2900\\054 \\\"129.219.21.179\\\": 2900\\054 \\\"129.219.21.141\\\": 2900}:1gmqKP:NH1uBdx-L11aTfytiD3age5bh7g\"")
                    .addHeader("x-csrftoken", "TU8D53WwzjRj8S6168pLVOPPlcfCj6BL")
                    .addHeader("x-instagram-ajax", "52ab8dc5031f")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "*/*")
                    .addHeader("referer", "https://www.instagram.com/h4ppyboys/following/")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("content-length", "0")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "d4e59732-9a5f-424b-993a-ddee6df058a0")
                    .build();


            Response response = client.newCall(request).execute();
            String responseString = response.body().string();

            JSONObject jsonObject = new JSONObject(responseString);
            if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                return true;
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

}
