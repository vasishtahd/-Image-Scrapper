package appengine.parser.instagram.gram;

import appengine.parser.utils.DataBaseConnector;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jooq.DSLContext;
import org.json.JSONArray;
import org.json.JSONObject;

import static appengine.parser.mysqlmodels.Tables.GRAM_USERS;

public class GetUsers {


    public void fetchUsersInLoop() {

        for (int i = 0; i < 10; i++) {
            try {
                fetchUsers();
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchUsers() {

        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://api.axigames.com/pool/followers/7339557412/")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "7c81b8a6-0de7-3e70-c9f7-62432399ddc5")
                    .build();

            Response response = client.newCall(request).execute();

            String jsonresponseString = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonresponseString);
            JSONArray userJSONArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < userJSONArray.length(); i++) {
                JSONObject userJSONObject = userJSONArray.getJSONObject(i);

                GramUser user = new GramUser();
                user.id = userJSONObject.getString("id");
                user.userName = userJSONObject.getString("username");
                user.mediaCount = userJSONObject.getInt("media_count");
                user.followerCount = userJSONObject.getInt("follower_count");
                user.followingCount = userJSONObject.getInt("following_count");
                user.creditTake = Integer.parseInt(userJSONObject.getString("creditTake"));

                insertInDB(user);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertInDB(GramUser user) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(GRAM_USERS, GRAM_USERS.ID, GRAM_USERS.USERNAME, GRAM_USERS.MEDIACOUNT,
                GRAM_USERS.FOLLOWERCOUNT, GRAM_USERS.FOLLOWINGCOUNT, GRAM_USERS.CREDITTAKE)
                .values(user.id, user.userName, user.mediaCount, user.followerCount,
                        user.followingCount, user.creditTake).execute();
    }

    class GramUser {
        String id;
        String userName;
        int mediaCount;
        int followerCount;
        int followingCount;
        int creditTake;


    }
}
